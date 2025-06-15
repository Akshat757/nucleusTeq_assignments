# app/cart/schemas.py
from pydantic import BaseModel

class AddToCart(BaseModel):
    product_id: int
    quantity: int

class UpdateCart(BaseModel):
    quantity: int
