
import os
from typing import Generator
from sqlalchemy import create_engine, text
from sqlalchemy.ext.declarative import declarative_base
from sqlalchemy.orm import sessionmaker, Session
import logging

# Configure logging
logging.basicConfig(level=logging.INFO)
logger = logging.getLogger(__name__)

# Database configuration
class DatabaseConfig:
    def __init__(self):
        self.DB_HOST = os.getenv("DB_HOST", "localhost")
        self.DB_PORT = os.getenv("DB_PORT", "3306")
        self.DB_NAME = os.getenv("DB_NAME", "order_management_system_db")
        self.DB_USER = os.getenv("DB_USER", "root")
        self.DB_PASSWORD = os.getenv("DB_PASSWORD", "")
        self.DB_CHARSET = os.getenv("DB_CHARSET", "utf8mb4")
        self.DB_TIMEZONE = os.getenv("DB_TIMEZONE", "UTC")
        self.POOL_SIZE = int(os.getenv("DB_POOL_SIZE", "5"))
        self.MAX_OVERFLOW = int(os.getenv("DB_MAX_OVERFLOW", "10"))
        self.POOL_TIMEOUT = int(os.getenv("DB_POOL_TIMEOUT", "30"))
        self.POOL_RECYCLE = int(os.getenv("DB_POOL_RECYCLE", "3600"))
        self.DATABASE_URL = self._build_database_url()

    def _build_database_url(self) -> str:
        if self.DB_PASSWORD:
            auth = f"{self.DB_USER}:{self.DB_PASSWORD}"
        else:
            auth = self.DB_USER
        url = (
            f"mysql+pymysql://{auth}@{self.DB_HOST}:{self.DB_PORT}/"
            f"{self.DB_NAME}?charset={self.DB_CHARSET}"
        )
        logger.info(f"Database URL configured: mysql+pymysql://{self.DB_USER}:***@{self.DB_HOST}:{self.DB_PORT}/{self.DB_NAME}")
        return url

db_config = DatabaseConfig()

engine = create_engine(
    db_config.DATABASE_URL,
    pool_size=db_config.POOL_SIZE,
    max_overflow=db_config.MAX_OVERFLOW,
    pool_timeout=db_config.POOL_TIMEOUT,
    pool_recycle=db_config.POOL_RECYCLE,
    echo=os.getenv("DB_ECHO", "false").lower() == "true",
    echo_pool=False,
    pool_pre_ping=True,
)

SessionLocal = sessionmaker(autocommit=False, autoflush=False, bind=engine)
Base = declarative_base()

def get_db() -> Generator[Session, None, None]:
    session = SessionLocal()
    try:
        yield session
    except Exception as e:
        session.rollback()
        logger.error(f"Database session error: {e}")
        raise
    finally:
        session.close()
