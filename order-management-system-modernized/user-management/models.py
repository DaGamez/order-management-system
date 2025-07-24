"""
User models and data structures for the User Management Service.

This module absorbs the logic from:
- order-management-system-legacy/src/main/java/com/example/crud/model/User.java
- order-management-system-legacy/src/main/java/com/example/crud/repository/UserRepository.java
"""

from datetime import datetime
from typing import List, Optional, Set
from pydantic import BaseModel, Field, validator
from sqlalchemy import Column, Integer, String, Boolean, DateTime, Table, ForeignKey
from sqlalchemy.ext.declarative import declarative_base
from sqlalchemy.orm import relationship
from passlib.context import CryptContext

Base = declarative_base()

# Association table for user roles
user_roles_table = Table(
    'user_roles',
    Base.metadata,
    Column('user_id', Integer, ForeignKey('users.id')),
    Column('role', String(50))
)

class User(Base):
    """
    SQLAlchemy User model representing the users table.
    Equivalent to the Java User entity.
    Compatible with legacy database schema.
    """
    __tablename__ = "users"
    
    id = Column(Integer, primary_key=True, index=True, autoincrement=True)
    username = Column(String(50), unique=True, index=True, nullable=False)
    password = Column(String(100), nullable=False)
    enabled = Column(Boolean, default=True, nullable=False)

# Pydantic models for API requests/responses

class LoginRequest(BaseModel):
    """
    Login request model matching the OpenAPI specification.
    """
    username: str = Field(..., min_length=3, max_length=50, description="Username for authentication")
    password: str = Field(..., min_length=1, description="Password for authentication")
    
    @validator('username')
    def username_must_be_valid(cls, v):
        if not v.strip():
            raise ValueError('Username cannot be empty')
        return v.strip()

class UserInfo(BaseModel):
    """
    User info model for JWT response matching OpenAPI specification.
    """
    id: int = Field(..., description="Unique identifier for the user")
    username: str = Field(..., description="Username of the user")
    roles: List[str] = Field(..., description="List of roles assigned to the user")

class JWTAuthResponse(BaseModel):
    """
    JWT authentication response model matching OpenAPI specification.
    """
    token: str = Field(..., description="JWT access token")
    tokenType: str = Field(default="Bearer", description="Type of the token")
    expiresIn: int = Field(..., description="Token expiration time in seconds")
    issuedAt: datetime = Field(..., description="Timestamp when the token was issued")
    user: UserInfo = Field(..., description="User information")

class ErrorResponse(BaseModel):
    """
    Error response model matching OpenAPI specification.
    """
    message: str = Field(..., description="Error message describing what went wrong")
    timestamp: datetime = Field(default_factory=datetime.utcnow, description="Timestamp when the error occurred")
    status: int = Field(..., description="HTTP status code")

class LogoutResponse(BaseModel):
    """
    Logout response model.
    """
    message: str = Field(default="Logout successful", description="Logout confirmation message")
    timestamp: datetime = Field(default_factory=datetime.utcnow, description="Timestamp when logout occurred")

# User roles management
class UserRole:
    """
    User roles enumeration and management.
    """
    USER = "USER"
    ADMIN = "ADMIN"
    API_USER = "API_USER"
    
    @classmethod
    def get_default_roles(cls) -> List[str]:
        """Get default roles for new users."""
        return [cls.USER]
    
    @classmethod
    def get_all_roles(cls) -> List[str]:
        """Get all available roles."""
        return [cls.USER, cls.ADMIN, cls.API_USER]

# Password hashing configuration
# Match legacy Spring Security configuration exactly (10 rounds, $2a$ format)
pwd_context = CryptContext(
    schemes=["bcrypt"], 
    deprecated="auto",
    bcrypt__rounds=10,        # Match legacy Spring Security rounds
    bcrypt__min_rounds=10,    # Accept legacy 10-round hashes
    bcrypt__ident="2a"        # Use $2a$ format to match Spring Security exactly
)

class PasswordManager:
    """
    Password hashing and verification utility.
    Equivalent to Spring Security's PasswordEncoder.
    Now perfectly aligned with legacy Spring Security (10 rounds, $2a$ format).
    """
    
    @staticmethod
    def hash_password(password: str) -> str:
        """Hash a password using bcrypt."""
        return pwd_context.hash(password)
    
    @staticmethod
    def verify_password(plain_password: str, hashed_password: str) -> bool:
        """Verify a password against its hash."""
        return pwd_context.verify(plain_password, hashed_password)

# Database models for user operations
class UserCreate(BaseModel):
    """Model for creating new users."""
    username: str = Field(..., min_length=3, max_length=50)
    password: str = Field(..., min_length=6, max_length=100)
    roles: Optional[List[str]] = Field(default_factory=UserRole.get_default_roles)
    enabled: bool = Field(default=True)

class UserResponse(BaseModel):
    """Model for user responses."""
    id: int
    username: str
    enabled: bool
    roles: List[str]
    
    class Config:
        from_attributes = True

# Token blacklist model for logout functionality
class TokenBlacklist(Base):
    """
    SQLAlchemy model for blacklisted tokens.
    Used to implement logout functionality by invalidating tokens.
    """
    __tablename__ = "token_blacklist"
    
    id = Column(Integer, primary_key=True, index=True, autoincrement=True)
    token = Column(String(500), unique=True, index=True, nullable=False)
    blacklisted_at = Column(DateTime, default=datetime.utcnow, nullable=False)
    expires_at = Column(DateTime, nullable=False)
