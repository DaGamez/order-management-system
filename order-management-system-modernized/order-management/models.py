from sqlalchemy import Column, Integer, String, Date, DateTime, ForeignKey, Float, Text
from sqlalchemy.orm import relationship
from datetime import datetime, date
from db import Base


# SQLAlchemy models

class User(Base):
    __tablename__ = "users"
    id = Column(Integer, primary_key=True, index=True)
    username = Column(String(50), unique=True, nullable=False)
    enabled = Column(Integer, default=1)
    orders = relationship("Order", back_populates="user")
    roles = relationship(
        "UserRole",
        back_populates="user",
        cascade="all, delete-orphan"
    )

class UserRole(Base):
    __tablename__ = "user_roles"
    user_id = Column(Integer, ForeignKey("users.id"), primary_key=True)
    role = Column(String(50), primary_key=True)
    user = relationship("User", back_populates="roles")

class Product(Base):
    __tablename__ = "products"
    id = Column(Integer, primary_key=True, index=True)
    name = Column(String(100), nullable=False)
    description = Column(Text)
    price = Column(Float, nullable=False)
    created_at = Column(DateTime, default=datetime.utcnow)
    orders = relationship("Order", back_populates="product")

class Order(Base):
    __tablename__ = "orders"
    id = Column(Integer, primary_key=True, index=True)
    order_date = Column(Date, default=date.today)
    user_id = Column(Integer, ForeignKey("users.id"), nullable=False)
    product_id = Column(Integer, ForeignKey("products.id"), nullable=False)
    quantity = Column(Integer, nullable=False)
    comments = Column(Text)
    created_at = Column(DateTime, default=datetime.utcnow)
    updated_at = Column(DateTime, default=datetime.utcnow, onupdate=datetime.utcnow)

    user = relationship("User", back_populates="orders")
    product = relationship("Product", back_populates="orders")

# Pydantic schemas
from pydantic import BaseModel, Field
from typing import Optional, List
from datetime import datetime, date


class UserResponse(BaseModel):
    id: int
    username: str
    enabled: bool
    roles: List[str]
    class Config:
        from_attributes = True

class ProductResponse(BaseModel):
    id: int
    name: str
    description: Optional[str]
    price: float
    created_at: datetime
    class Config:
        from_attributes = True

class OrderResponse(BaseModel):
    id: int
    order_date: date
    user: UserResponse
    product: ProductResponse
    quantity: int
    comments: Optional[str]
    created_at: datetime
    updated_at: datetime
    class Config:
        from_attributes = True
