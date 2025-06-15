# app/main.py
from fastapi import FastAPI
from app.core.database import Base, engine
from app.products.models import Product
from app.auth.models import User
from app.cart.models import CartItem
from app.orders.models import Order, OrderItem
from app.auth.routes import router as auth_router
from app.products.routes import router as product_router
from app.products.public_routes import router as public_product_router
from app.cart.routes import router as cart_router
from app.orders.routes import router as orders_router
from fastapi.exceptions import RequestValidationError
from starlette.exceptions import HTTPException as StarletteHTTPException
from app.middlewares.exception_handler import (
    validation_exception_handler,
    http_exception_handler,
    unhandled_exception_handler
)

app = FastAPI(title="E-commerce API")

app.include_router(auth_router)
app.include_router(product_router)
app.include_router(public_product_router)
app.include_router(cart_router)
app.include_router(orders_router)

app.add_exception_handler(RequestValidationError, validation_exception_handler)
app.add_exception_handler(StarletteHTTPException, http_exception_handler)
app.add_exception_handler(Exception, unhandled_exception_handler)

Base.metadata.create_all(bind=engine)