# app/orders/schemas.py
from pydantic import BaseModel
from typing import List
from datetime import datetime

class OrderItemSchema(BaseModel):
    product_id: int
    quantity: int
    price_at_purchase: float

    class Config:
        orm_mode = True

class OrderSchema(BaseModel):
    id: int
    total_amount: float
    status: str
    created_at: datetime
    items: List[OrderItemSchema]

    class Config:
        orm_mode = True
