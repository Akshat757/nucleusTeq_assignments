from fastapi import APIRouter, Depends, HTTPException, status
from sqlalchemy.orm import Session
from typing import List
from app.products import models, schemas
from app.core.database import get_db
from app.auth.utils import admin_required

router = APIRouter(prefix="/admin/products", tags=["Admin Products"])

# CREATE PRODUCT
@router.post("/", response_model=schemas.ProductOut)
def create_product(
    product: schemas.ProductCreate,
    db: Session = Depends(get_db),
    user = Depends(admin_required)
):
    # Check for duplicate product name
    existing = db.query(models.Product).filter(models.Product.name == product.name).first()
    if existing:
        raise HTTPException(
            status_code=status.HTTP_400_BAD_REQUEST,
            detail="A product with this name already exists."
        )

    # Validate inputs
    if product.price <= 0:
        raise HTTPException(status_code=400, detail="Price must be greater than 0.")
    if product.stock < 0:
        raise HTTPException(status_code=400, detail="Stock cannot be negative.")

    db_product = models.Product(**product.model_dump())
    db.add(db_product)
    db.commit()
    db.refresh(db_product)
    return db_product

# GET ALL PRODUCTS (Admin only)
@router.get("/", response_model=List[schemas.ProductOut])
def get_products(
    skip: int = 0,
    limit: int = 10,
    db: Session = Depends(get_db),
    user = Depends(admin_required)
):
    return db.query(models.Product).offset(skip).limit(limit).all()

# GET PRODUCT BY ID
@router.get("/{product_id}", response_model=schemas.ProductOut)
def get_product(
    product_id: int,
    db: Session = Depends(get_db),
    user = Depends(admin_required)
):
    product = db.query(models.Product).filter(models.Product.id == product_id).first()
    if not product:
        raise HTTPException(status_code=404, detail="Product not found.")
    return product

# UPDATE PRODUCT
@router.put("/{product_id}", response_model=schemas.ProductOut)
def update_product(
    product_id: int,
    updated: schemas.ProductUpdate,
    db: Session = Depends(get_db),
    user = Depends(admin_required)
):
    product = db.query(models.Product).filter(models.Product.id == product_id).first()
    if not product:
        raise HTTPException(status_code=404, detail="Product not found.")

    update_data = updated.model_dump(exclude_unset=True)

    if "price" in update_data and update_data["price"] <= 0:
        raise HTTPException(status_code=400, detail="Price must be greater than 0.")
    if "stock" in update_data and update_data["stock"] < 0:
        raise HTTPException(status_code=400, detail="Stock cannot be negative.")

    # Apply updates
    for key, value in update_data.items():
        setattr(product, key, value)

    db.commit()
    db.refresh(product)
    return product

# DELETE PRODUCT
@router.delete("/{product_id}")
def delete_product(
    product_id: int,
    db: Session = Depends(get_db),
    user = Depends(admin_required)
):
    product = db.query(models.Product).filter(models.Product.id == product_id).first()
    if not product:
        raise HTTPException(status_code=404, detail="Product not found.")
    db.delete(product)
    db.commit()
    return {"message": "Product deleted successfully."}
