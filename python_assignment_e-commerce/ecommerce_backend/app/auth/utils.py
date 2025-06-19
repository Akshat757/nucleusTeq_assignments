# app/auth/utils.py
from passlib.context import CryptContext
from jose import JWTError, jwt
from datetime import datetime, timedelta
from app.core.config import settings
from fastapi import Depends, HTTPException, status
from fastapi.security import OAuth2PasswordBearer
from sqlalchemy.orm import Session
from app.core.database import SessionLocal
from app.auth.models import User

# password hashing and verification
pwd_context = CryptContext(schemes=["bcrypt"], deprecated="auto")

# hash the password before saving
def hash_password(password: str) -> str:
    return pwd_context.hash(password)

# verify password
def verify_password(plain_password: str, hashed_password: str) -> bool:
    return pwd_context.verify(plain_password, hashed_password)

# create access token for login
def create_access_token(data: dict, expires_delta: timedelta = None):
    to_encode = data.copy()

    # set expiry time (30 min default)
    expire = datetime.utcnow() + (expires_delta or timedelta(minutes=settings.ACCESS_TOKEN_EXPIRE_MINUTES))
    to_encode.update({"exp": expire})

    # encode the token with secret and algorithm
    return jwt.encode(to_encode, settings.SECRET_KEY, algorithm=settings.ALGORITHM)

# tell FastAPI where token comes from
oauth2_scheme = OAuth2PasswordBearer(tokenUrl="auth/signin")

def get_db():
    db = SessionLocal()
    try:
        yield db
    finally:
        db.close()

# get current logged in user
def get_current_user(token: str = Depends(oauth2_scheme), db: Session = Depends(get_db)) -> User:
    try:
        # decode the JWT token
        payload = jwt.decode(token, settings.SECRET_KEY, algorithms=[settings.ALGORITHM])
        email = payload.get("sub") # 'sub' holds the user email

        if email is None:
            raise HTTPException(status_code=401, detail="Invalid token")

        # Fetch user from DB
        user = db.query(User).filter(User.email == email).first()
        if not user:
            raise HTTPException(status_code=401, detail="User not found")
        return user
    except JWTError:
        raise HTTPException(status_code=401, detail="Invalid token")

def admin_required(user: User = Depends(get_current_user)):
    if user.role != "admin":
        raise HTTPException(status_code=403, detail="Admin access required")
    return user

