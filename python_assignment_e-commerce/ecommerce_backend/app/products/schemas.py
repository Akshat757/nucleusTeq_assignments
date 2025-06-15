# app/products/schemas.py
from pydantic import BaseModel
from typing import Optional

class ProductCreate(BaseModel):
    name: str
    description: Optional[str]
    price: float
    stock: int
    category: Optional[str]
    image_url: Optional[str]

class ProductUpdate(BaseModel):
    name: Optional[str] = None
    description: Optional[str] = None
    price: Optional[float] = None
    stock: Optional[int] = None
    category: Optional[str] = None
    image_url: Optional[str] = None

class ProductOut(BaseModel):
    id: int
    name: str
    description: Optional[str]
    price: float
    stock: int
    category: Optional[str]
    image_url: Optional[str]

    class Config:
        orm_mode = True
