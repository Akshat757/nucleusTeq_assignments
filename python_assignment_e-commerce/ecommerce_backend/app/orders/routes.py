# app/orders/routes.py
from typing import List
from fastapi import APIRouter, Depends, HTTPException
from sqlalchemy.orm import Session
from app.auth.utils import get_current_user
from app.core.database import get_db
from app.cart.models import CartItem
from app.products.models import Product
from app.orders import models as order_models, schemas as order_schemas

router = APIRouter(prefix="/orders", tags=["Orders"])

@router.post("/checkout")
def checkout(
    db: Session = Depends(get_db),
    user = Depends(get_current_user)
):
    cart_items = db.query(CartItem).filter(CartItem.user_id == user.id).all()
    if not cart_items:
        raise HTTPException(status_code=400, detail="Cart is empty")

    total = 0
    order_items = []

    for item in cart_items:
        product = db.query(Product).filter(Product.id == item.product_id).first()
        if not product:
            continue
        subtotal = product.price * item.quantity
        total += subtotal
        order_items.append(order_models.OrderItem(
            product_id=item.product_id,
            quantity=item.quantity,
            price_at_purchase=product.price
        ))

    order = order_models.Order(
        user_id=user.id,
        total_amount=total,
        status="paid",
        items=order_items
    )

    db.add(order)
    db.query(CartItem).filter(CartItem.user_id == user.id).delete()  # Clear cart
    db.commit()
    return {"message": "Order placed successfully", "order_id": order.id}


@router.get("/", response_model=List[order_schemas.OrderSchema])
def order_history(
    db: Session = Depends(get_db),
    user = Depends(get_current_user)
):
    return db.query(order_models.Order).filter(order_models.Order.user_id == user.id).all()


@router.get("/{order_id:int}", response_model=order_schemas.OrderSchema)
def order_detail(
    order_id: int,  # restricts it to integer
    db: Session = Depends(get_db),
    user = Depends(get_current_user)
):
    order = db.query(order_models.Order).filter_by(id=order_id, user_id=user.id).first()
    if not order:
        raise HTTPException(status_code=404, detail="Order not found")
    return order
