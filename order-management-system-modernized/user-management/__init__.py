"""
User Management Service Package.

This package implements JWT-based authentication for the Order Management System.
It replaces the legacy Spring Security-based authentication with a modern FastAPI implementation.
"""

__version__ = "2.0.0"
__author__ = "Order Management System Team"
__email__ = "support@ordermanagement.com"

from api import app
from models import *
from services import *
from db import *

__all__ = [
    "app",
    # Models
    "User",
    "LoginRequest", 
    "JWTAuthResponse",
    "UserInfo",
    "ErrorResponse",
    "LogoutResponse",
    "UserRole",
    "PasswordManager",
    # Services
    "UserService",
    "AuthenticationService",
    "get_user_service",
    "get_auth_service",
    "TokenCleanupService",
    # Database
    "get_db",
    "init_database",
    "DatabaseConfig",
    "DatabaseHealthCheck"
]
