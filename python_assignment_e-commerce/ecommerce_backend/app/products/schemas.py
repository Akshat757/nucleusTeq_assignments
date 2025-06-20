# app/products/schemas.py
from pydantic import BaseModel
from typing import Optional
from typing import List

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

class ProductResponse(BaseModel):
    success: bool
    message: str
    data: ProductOut

class ProductBulkResponse(BaseModel):
    success: bool
    message: str
    data: List[ProductOut]

    class Config:
        orm_mode = True
