# app/middlewares/exception_handler.py
from fastapi import Request
from fastapi.responses import JSONResponse
from fastapi.exceptions import RequestValidationError
from starlette.exceptions import HTTPException as StarletteHTTPException
from starlette.status import HTTP_500_INTERNAL_SERVER_ERROR

def create_error_response(message, code):
    return JSONResponse(
        status_code=code,
        content={
            "error": True,
            "message": message,
            "code": code
        }
    )

async def validation_exception_handler(request: Request, exc: RequestValidationError):
    return create_error_response("Validation Error: " + str(exc.errors()), 422)

async def http_exception_handler(request: Request, exc: StarletteHTTPException):
    return create_error_response(exc.detail, exc.status_code)

async def unhandled_exception_handler(request: Request, exc: Exception):
    return create_error_response("Internal Server Error", HTTP_500_INTERNAL_SERVER_ERROR)
