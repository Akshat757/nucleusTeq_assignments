# app/auth/schemas.py
from pydantic import BaseModel, EmailStr
from enum import Enum

class Role(str, Enum):
    admin = "admin"
    user = "user"

class UserCreate(BaseModel):
    name: str
    email: EmailStr
    password: str
    role: Role = Role.user

class UserLogin(BaseModel):
    email: EmailStr
    password: str

class Token(BaseModel):
    access_token: str
    token_type: str = "bearer"
