# app/products/public_routes.py
from fastapi import APIRouter, Depends, HTTPException, Query
from sqlalchemy.orm import Session
from typing import List, Optional
from app.products import models, schemas
from app.core.database import get_db

router = APIRouter(prefix="/products", tags=["Public Products"])

@router.get("/", response_model=List[schemas.ProductOut])
def list_products(
    category: Optional[str] = None,
    min_price: Optional[float] = None,
    max_price: Optional[float] = None,
    sort_by: Optional[str] = Query(None, regex="^(name|price)$"),
    page: int = 1,
    page_size: int = 10,
    db: Session = Depends(get_db)
):
    query = db.query(models.Product)

    # Filters
    if category:
        query = query.filter(models.Product.category.ilike(f"%{category}%"))
    if min_price is not None:
        query = query.filter(models.Product.price >= min_price)
    if max_price is not None:
        query = query.filter(models.Product.price <= max_price)

    # Sorting
    if sort_by == "price":
        query = query.order_by(models.Product.price)
    elif sort_by == "name":
        query = query.order_by(models.Product.name)

    # Pagination
    products = query.offset((page - 1) * page_size).limit(page_size).all()
    return products

@router.get("/search", response_model=List[schemas.ProductOut])
def search_products(
    keyword: str,
    db: Session = Depends(get_db)
):
    query = db.query(models.Product).filter(
        models.Product.name.ilike(f"%{keyword}%") |
        models.Product.description.ilike(f"%{keyword}%")
    )
    return query.all()

@router.get("/{product_id}", response_model=schemas.ProductOut)
def get_product_detail(product_id: int, db: Session = Depends(get_db)):
    product = db.query(models.Product).filter(models.Product.id == product_id).first()
    if not product:
        raise HTTPException(status_code=404, detail="Product not found")
    return product
