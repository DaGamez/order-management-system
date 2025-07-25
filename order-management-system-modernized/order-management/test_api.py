"""
Basic tests for the Order Management Service.
"""
import pytest
import asyncio
from httpx import AsyncClient
from sqlalchemy import create_engine
from sqlalchemy.orm import sessionmaker
from sqlalchemy.pool import StaticPool

from api import app
from db import get_db
from models import Base, Product, Order, User

SQLITE_DATABASE_URL = "sqlite:///./test_orders.db"
engine = create_engine(
    SQLITE_DATABASE_URL,
    connect_args={"check_same_thread": False},
    poolclass=StaticPool,
)
TestingSessionLocal = sessionmaker(autocommit=False, autoflush=False, bind=engine)

Base.metadata.create_all(bind=engine)

def override_get_db():
    try:
        db = TestingSessionLocal()
        yield db
    finally:
        db.close()

app.dependency_overrides[get_db] = override_get_db

@pytest.fixture(scope="module")
def anyio_backend():
    return "asyncio"

@pytest.mark.anyio
async def test_get_products():
    async with AsyncClient(app=app, base_url="http://test") as ac:
        response = await ac.get("/order-management/products")
        assert response.status_code == 200
        assert isinstance(response.json(), list)

@pytest.mark.anyio
async def test_create_order():
    # Add a user and product to the test DB
    db = next(override_get_db())
    user = User(username="testuser", enabled=1, roles="USER")
    product = Product(name="Test Product", description="desc", price=10.0)
    db.add(user)
    db.add(product)
    db.commit()
    db.refresh(user)
    db.refresh(product)
    db.close()

    async with AsyncClient(app=app, base_url="http://test") as ac:
        payload = {
            "product_id": product.id,
            "quantity": 2,
            "comments": "Test order"
        }
        response = await ac.post("/order-management/orders", params=payload)
        assert response.status_code == 201
        data = response.json()
        assert data["product"]["id"] == product.id
        assert data["quantity"] == 2
