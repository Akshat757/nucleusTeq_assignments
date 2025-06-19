from sqlalchemy import create_engine
from sqlalchemy.orm import sessionmaker, declarative_base
from app.core.config import settings

# Use the PostgreSQL connection URL from .env
# this connects SQLAlchemy to the db
engine = create_engine(settings.DATABASE_URL)

# this creates DB sessions
SessionLocal = sessionmaker(autocommit=False, autoflush=False, bind=engine)

# this is the base class for all SQLAlchemy models
Base = declarative_base()

def get_db():
    db = SessionLocal()
    try:
        yield db
    finally:
        db.close()
