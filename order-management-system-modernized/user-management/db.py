"""
Database configuration and connection management for the User Management Service.

This module handles database connectivity and session management,
equivalent to the Spring Data JPA configuration in the legacy system.
"""

import os
from typing import Generator
from sqlalchemy import create_engine, MetaData, text
from sqlalchemy.ext.declarative import declarative_base
from sqlalchemy.orm import sessionmaker, Session
from sqlalchemy.pool import StaticPool
import logging

# Configure logging
logging.basicConfig(level=logging.INFO)
logger = logging.getLogger(__name__)

# Database configuration
class DatabaseConfig:
    """
    Database configuration class.
    Reads configuration from environment variables with fallback defaults.
    """
    
    def __init__(self):
        # Database connection parameters (matching legacy application.properties)
        self.DB_HOST = os.getenv("DB_HOST", "localhost")
        self.DB_PORT = os.getenv("DB_PORT", "3306")
        self.DB_NAME = os.getenv("DB_NAME", "order_management_system_db")
        self.DB_USER = os.getenv("DB_USER", "root")
        self.DB_PASSWORD = os.getenv("DB_PASSWORD", "")
        
        # Additional connection parameters
        self.DB_CHARSET = os.getenv("DB_CHARSET", "utf8mb4")
        self.DB_TIMEZONE = os.getenv("DB_TIMEZONE", "UTC")
        
        # Connection pool settings
        self.POOL_SIZE = int(os.getenv("DB_POOL_SIZE", "5"))
        self.MAX_OVERFLOW = int(os.getenv("DB_MAX_OVERFLOW", "10"))
        self.POOL_TIMEOUT = int(os.getenv("DB_POOL_TIMEOUT", "30"))
        self.POOL_RECYCLE = int(os.getenv("DB_POOL_RECYCLE", "3600"))
        
        # Build database URL
        self.DATABASE_URL = self._build_database_url()
    
    def _build_database_url(self) -> str:
        """Build the database URL from configuration parameters."""
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

# Initialize database configuration
db_config = DatabaseConfig()

# Create SQLAlchemy engine
engine = create_engine(
    db_config.DATABASE_URL,
    pool_size=db_config.POOL_SIZE,
    max_overflow=db_config.MAX_OVERFLOW,
    pool_timeout=db_config.POOL_TIMEOUT,
    pool_recycle=db_config.POOL_RECYCLE,
    echo=os.getenv("DB_ECHO", "false").lower() == "true",  # Equivalent to spring.jpa.show-sql
    echo_pool=False,
    pool_pre_ping=True,  # Verify connections before use
)

# Create SessionLocal class
SessionLocal = sessionmaker(autocommit=False, autoflush=False, bind=engine)

# Create Base class for declarative models
Base = declarative_base()

def get_database_session() -> Generator[Session, None, None]:
    """
    Dependency function to get database session.
    This function provides a database session for FastAPI dependency injection.
    Equivalent to Spring's @Autowired repository injection.
    """
    session = SessionLocal()
    try:
        yield session
    finally:
        session.close()

class DatabaseHealthCheck:
    """
    Database health check utility.
    """
    
    @staticmethod
    def check_connection() -> bool:
        """
        Check if database connection is healthy.
        """
        try:
            session = SessionLocal()
            session.execute(text("SELECT 1"))
            session.close()
            return True
        except Exception as e:
            logger.error(f"Database health check failed: {e}")
            return False
    
    @staticmethod
    def get_connection_info() -> dict:
        """
        Get database connection information.
        """
        return {
            "host": db_config.DB_HOST,
            "port": db_config.DB_PORT,
            "database": db_config.DB_NAME,
            "user": db_config.DB_USER,
            "pool_size": db_config.POOL_SIZE,
            "max_overflow": db_config.MAX_OVERFLOW,
            "engine_pool_size": engine.pool.size(),
            "engine_pool_checked_in": engine.pool.checkedin(),
            "engine_pool_checked_out": engine.pool.checkedout(),
        }

# Database session dependency for FastAPI
def get_db() -> Generator[Session, None, None]:
    """
    FastAPI dependency for database sessions.
    """
    session = SessionLocal()
    try:
        yield session
    except Exception as e:
        session.rollback()
        logger.error(f"Database session error: {e}")
        raise
    finally:
        session.close()

# Export commonly used items
__all__ = [
    "engine",
    "SessionLocal", 
    "Base",
    "get_db",
    "get_database_session",
    "DatabaseConfig",
    "DatabaseHealthCheck"
]
