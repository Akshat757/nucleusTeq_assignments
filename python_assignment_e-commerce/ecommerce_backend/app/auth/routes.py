# app/auth/routes.py
from fastapi import APIRouter, Depends, HTTPException, status
from sqlalchemy.orm import Session
from app.auth import schemas, models, utils
from app.core.database import SessionLocal
from app.core.database import Base, engine
from jose import JWTError
from pydantic import EmailStr
from app.auth.schemas import ForgotPasswordRequest
from app.auth.models import User
from app.utils.email_utils import send_email


models.Base.metadata.create_all(bind=engine)

router = APIRouter(prefix="/auth", tags=["Authentication"])

def get_db():
    db = SessionLocal()
    try:
        yield db
    finally:
        db.close()

@router.post("/signup", status_code=201)
def signup(user: schemas.UserCreate, db: Session = Depends(get_db)):
     # fetch user by email
    existing_user = db.query(models.User).filter(models.User.email == user.email).first()
    if existing_user:
        raise HTTPException(status_code=400, detail="An account with this email already exists.")

    # hash the password before storing
    hashed_pwd = utils.hash_password(user.password)

    # create a new user object
    new_user = models.User(
        name=user.name,
        email=user.email,
        hashed_password=hashed_pwd,
        role=user.role
    )
    db.add(new_user)
    db.commit()
    db.refresh(new_user)
    return {"message": "User registered successfully", "user_id": new_user.id} 

@router.post("/signin", response_model=schemas.Token)
def signin(user: schemas.UserLogin, db: Session = Depends(get_db)):
     # fetch user by email
    db_user = db.query(models.User).filter(models.User.email == user.email).first()
    if not db_user or not utils.verify_password(user.password, db_user.hashed_password):
        raise HTTPException(status_code=401, detail="Invalid credentials")
    
    # generate JWT token
    token = utils.create_access_token(data={"sub": db_user.email})
    return {"message": "User logged in successfully", "access_token": token}

@router.post("/forgot-password")
def forgot_password(request: ForgotPasswordRequest, db: Session = Depends(get_db)):
    email = request.email
    # find user
    user = db.query(User).filter(User.email == email).first()
    if not user:
        raise HTTPException(404, "User not found")

    import secrets
    from datetime import datetime, timedelta

    # generate rset token
    token = secrets.token_urlsafe(32)

    # save token and expiry time to db
    user.reset_token = token
    user.token_expiry = datetime.utcnow() + timedelta(minutes=15)
    db.commit()

    reset_link = f"http://localhost:8000/auth/reset-password?token={token}"

    # send mail
    send_email(
        user.email,
        "Reset your password",
        f"Click this link to reset your password: {reset_link}"
    )

    return {"message": "Password reset link sent to your email."}

@router.post("/reset-password")
def reset_password(token: str, new_password: str, db: Session = Depends(get_db)):
    # find user
    user = db.query(User).filter(User.reset_token == token).first()
    if not user or not user.token_expiry or user.token_expiry < datetime.utcnow():
        raise HTTPException(400, "Invalid or expired token")
    
    # hash new password
    user.password = utils.hash_password(new_password)

    # remove the token
    user.reset_token = None
    user.token_expiry = None
    db.commit()

    return {"message": "Password reset successful."}
