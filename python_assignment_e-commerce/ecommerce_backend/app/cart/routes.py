# app/cart/routes.py
from fastapi import APIRouter, Depends, HTTPException
from sqlalchemy.orm import Session
from app.core.database import get_db
from app.auth.utils import get_current_user
from app.cart import models as cart_models, schemas as cart_schemas
from app.products.models import Product

router = APIRouter(prefix="/cart", tags=["Cart"])

@router.post("/")
def add_to_cart(
    item: cart_schemas.AddToCart,
    db: Session = Depends(get_db),
    user = Depends(get_current_user)
):
    # Validate product exists
    product = db.query(Product).filter(Product.id == item.product_id).first()
    if not product:
        raise HTTPException(status_code=404, detail="Product not found")
    
    # Check if already in cart
    cart_item = db.query(cart_models.CartItem).filter_by(user_id=user.id, product_id=item.product_id).first()
    if cart_item:
        cart_item.quantity += item.quantity
    else:
        cart_item = cart_models.CartItem(
            user_id=user.id,
            product_id=item.product_id,
            quantity=item.quantity
        )
        db.add(cart_item)
    
    db.commit()
    return {"message": "Item added to cart"}

@router.get("/")
def view_cart(
    db: Session = Depends(get_db),
    user = Depends(get_current_user)
):
    cart_items = db.query(cart_models.CartItem).filter(cart_models.CartItem.user_id == user.id).all()
    response = []
    for item in cart_items:
        product = db.query(Product).filter(Product.id == item.product_id).first()
        if product:
            response.append({
                "product_id": item.product_id,
                "product_name": product.name,
                "quantity": item.quantity,
                "price": product.price,
                "total": product.price * item.quantity
            })
    return response

@router.put("/{product_id}")
def update_quantity(
    product_id: int,
    update: cart_schemas.UpdateCart,
    db: Session = Depends(get_db),
    user = Depends(get_current_user)
):
    cart_item = db.query(cart_models.CartItem).filter_by(user_id=user.id, product_id=product_id).first()
    if update.quantity <= 0:
        raise HTTPException(400, "Quantity must be greater than zero")
    if not cart_item:
        raise HTTPException(status_code=404, detail="Item not found in cart")
    cart_item.quantity = update.quantity
    db.commit()
    return {"message": "Cart updated"}

@router.delete("/{product_id}")
def remove_from_cart(
    product_id: int,
    db: Session = Depends(get_db),
    user = Depends(get_current_user)
):
    cart_item = db.query(cart_models.CartItem).filter_by(user_id=user.id, product_id=product_id).first()
    if not cart_item:
        raise HTTPException(status_code=404, detail="Item not in cart")
    db.delete(cart_item)
    db.commit()
    return {"message": "Item removed from cart"}
