from fastapi import APIRouter, Depends, HTTPException, status
from sqlalchemy.orm import Session
from typing import List
from app.products import models, schemas
from app.core.database import get_db
from app.auth.utils import admin_required

router = APIRouter(prefix="/admin/products", tags=["Admin Products"])

# CREATE PRODUCT
@router.post("/", response_model=schemas.ProductResponse)
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

    # Convert Pydantic model to dictionary, create Product model
    db_product = models.Product(**product.model_dump())
    db.add(db_product)
    db.commit()
    db.refresh(db_product)
    return {
        "success": True,
        "message": "Product created successfully",
        "data": db_product 
    }

# CREATE MULTIPLE PRODUCTS
@router.post("/bulk", response_model=schemas.ProductBulkResponse)
def create_multiple_products(
    products: List[schemas.ProductCreate],
    db: Session = Depends(get_db),
    user = Depends(admin_required)
):
    db_products = []

    for product in products:
        existing = db.query(models.Product).filter(models.Product.name == product.name).first()
        if existing:
            raise HTTPException(status_code=400, detail=f"Product '{product.name}' already exists")
        
        db_product = models.Product(**product.model_dump())
        db_products.append(db_product)

    db.add_all(db_products)
    db.commit()
    return {
        "success": True,
        "message": "Product created successfully",
        "data": db_products 
    }


# GET ALL PRODUCTS (Admin only)
@router.get("/", response_model=List[schemas.ProductOut])
def get_products(
    skip: int = 0,
    limit: int = 10,
    db: Session = Depends(get_db),
    user = Depends(admin_required)
):
    # Return products, with pagination
    return db.query(models.Product).offset(skip).limit(limit).all()

# GET PRODUCT BY ID
@router.get("/{product_id}", response_model=schemas.ProductOut)
def get_product(
    product_id: int,
    db: Session = Depends(get_db),
    user = Depends(admin_required)
):
    # Find product by ID
    product = db.query(models.Product).filter(models.Product.id == product_id).first()
    if not product:
        raise HTTPException(status_code=404, detail="Product not found.")
    return product

# UPDATE PRODUCT
@router.put("/{product_id}", response_model=schemas.ProductResponse)
def update_product(
    product_id: int,
    updated: schemas.ProductUpdate,
    db: Session = Depends(get_db),
    user = Depends(admin_required)
):
    # Find product by ID
    product = db.query(models.Product).filter(models.Product.id == product_id).first()
    if not product:
        raise HTTPException(status_code=404, detail="Product not found.")

    # if product.admin_id != user.id:
    #     raise HTTPException(status_code=403, detail="You are not authorized to update this product.")

    update_data = updated.model_dump(exclude_unset=True)
    if not update_data:
        raise HTTPException(400, "No update fields provided.")

    if "price" in update_data and update_data["price"] <= 0:
        raise HTTPException(status_code=400, detail="Price must be greater than 0.")
    if "stock" in update_data and update_data["stock"] < 0:
        raise HTTPException(status_code=400, detail="Stock cannot be negative.")

    # Update only provided fields
    for key, value in update_data.items():
        setattr(product, key, value)

    db.commit()
    db.refresh(product)
    return {
        "success": True,
        "message": "Product updated successfully",
        "data": product
    }


# DELETE PRODUCT
@router.delete("/{product_id}")
def delete_product(
    product_id: int,
    db: Session = Depends(get_db),
    user = Depends(admin_required)
):
    # Find product by ID
    product = db.query(models.Product).filter(models.Product.id == product_id).first()
    if not product:
        raise HTTPException(status_code=404, detail="Product not found.")
    db.delete(product)
    db.commit()
    return {"message": "Product deleted successfully."}
