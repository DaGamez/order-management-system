"""
Basic tests for the User Management Service.

These tests validate the core functionality of the authentication service.
"""

import pytest
import asyncio
from httpx import AsyncClient
from sqlalchemy import create_engine
from sqlalchemy.orm import sessionmaker
from sqlalchemy.pool import StaticPool

from api import app
from db import get_db, Base
from models import User, PasswordManager


# Test database setup
SQLITE_DATABASE_URL = "sqlite:///./test.db"

engine = create_engine(
    SQLITE_DATABASE_URL,
    connect_args={"check_same_thread": False},
    poolclass=StaticPool,
)
TestingSessionLocal = sessionmaker(autocommit=False, autoflush=False, bind=engine)

def override_get_db():
    """Override database dependency for testing."""
    try:
        db = TestingSessionLocal()
        yield db
    finally:
        db.close()

app.dependency_overrides[get_db] = override_get_db

@pytest.fixture(scope="module")
def anyio_backend():
    return "asyncio"

@pytest.fixture(scope="module")
async def setup_database():
    """Set up test database."""
    Base.metadata.create_all(bind=engine)
    
    # Create test user
    db = TestingSessionLocal()
    test_user = User(
        username="testuser",
        password=PasswordManager.hash_password("testpass123"),
        enabled=True
    )
    db.add(test_user)
    db.commit()
    db.close()
    
    yield
    
    # Cleanup
    Base.metadata.drop_all(bind=engine)

@pytest.fixture
async def client():
    """HTTP client for testing."""
    async with AsyncClient(app=app, base_url="http://test") as ac:
        yield ac

class TestAuthentication:
    """Test authentication endpoints."""
    
    @pytest.mark.anyio
    async def test_login_success(self, client: AsyncClient, setup_database):
        """Test successful login."""
        response = await client.post(
            "/user-management/login",
            json={
                "username": "testuser",
                "password": "testpass123"
            }
        )
        
        assert response.status_code == 200
        data = response.json()
        
        assert "token" in data
        assert data["tokenType"] == "Bearer"
        assert "expiresIn" in data
        assert "user" in data
        assert data["user"]["username"] == "testuser"
    
    @pytest.mark.anyio
    async def test_login_invalid_credentials(self, client: AsyncClient, setup_database):
        """Test login with invalid credentials."""
        response = await client.post(
            "/user-management/login",
            json={
                "username": "testuser",
                "password": "wrongpassword"
            }
        )
        
        assert response.status_code == 401
        data = response.json()
        assert "message" in data
        assert "Invalid username or password" in data["message"]
    
    @pytest.mark.anyio
    async def test_login_nonexistent_user(self, client: AsyncClient, setup_database):
        """Test login with non-existent user."""
        response = await client.post(
            "/user-management/login",
            json={
                "username": "nonexistent",
                "password": "password123"
            }
        )
        
        assert response.status_code == 401
        data = response.json()
        assert "message" in data
        assert "Invalid username or password" in data["message"]
    
    @pytest.mark.anyio
    async def test_login_validation_error(self, client: AsyncClient, setup_database):
        """Test login with validation errors."""
        # Missing password
        response = await client.post(
            "/user-management/login",
            json={
                "username": "testuser"
            }
        )
        
        assert response.status_code == 422
    
    @pytest.mark.anyio
    async def test_logout_success(self, client: AsyncClient, setup_database):
        """Test successful logout."""
        # First login to get token
        login_response = await client.post(
            "/user-management/login",
            json={
                "username": "testuser",
                "password": "testpass123"
            }
        )
        
        assert login_response.status_code == 200
        token = login_response.json()["token"]
        
        # Then logout
        logout_response = await client.post(
            "/user-management/logout",
            headers={"Authorization": f"Bearer {token}"}
        )
        
        assert logout_response.status_code == 200
        data = logout_response.json()
        assert data["message"] == "Logout successful"
    
    @pytest.mark.anyio
    async def test_logout_invalid_token(self, client: AsyncClient, setup_database):
        """Test logout with invalid token."""
        response = await client.post(
            "/user-management/logout",
            headers={"Authorization": "Bearer invalid_token"}
        )
        
        assert response.status_code == 401
    
    @pytest.mark.anyio
    async def test_logout_without_token(self, client: AsyncClient, setup_database):
        """Test logout without authorization header."""
        response = await client.post("/user-management/logout")
        
        assert response.status_code == 403

class TestHealthCheck:
    """Test health check endpoints."""
    
    @pytest.mark.anyio
    async def test_health_check(self, client: AsyncClient):
        """Test health check endpoint."""
        response = await client.get("/health")
        
        assert response.status_code == 200
        data = response.json()
        
        assert "status" in data
        assert "service" in data
        assert data["service"] == "user-management"
    
    @pytest.mark.anyio
    async def test_root_endpoint(self, client: AsyncClient):
        """Test root endpoint."""
        response = await client.get("/")
        
        assert response.status_code == 200
        data = response.json()
        
        assert "service" in data
        assert data["service"] == "User Management Service"
        assert "endpoints" in data

if __name__ == "__main__":
    pytest.main([__file__])
