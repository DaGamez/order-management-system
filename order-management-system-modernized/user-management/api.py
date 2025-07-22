"""
FastAPI User Management Service API.

This module absorbs the logic from:
- order-management-system-legacy/src/main/java/com/example/crud/controller/LoginController.java

The API implements the OpenAPI specification endpoints:
- POST /user-management/login
- POST /user-management/logout
"""

import os
from datetime import datetime
from typing import Optional, Dict, Any
from fastapi import FastAPI, Depends, HTTPException, status, Security
from fastapi.security import HTTPBearer, HTTPAuthorizationCredentials
from fastapi.middleware.cors import CORSMiddleware
from sqlalchemy.orm import Session
import logging
import uvicorn

from models import (
    LoginRequest, 
    JWTAuthResponse, 
    ErrorResponse,
    LogoutResponse,
    User
)
from services import (
    AuthenticationService, 
    get_auth_service,
    TokenCleanupService
)
from db import get_db, init_database, DatabaseHealthCheck

# Configure logging
logging.basicConfig(
    level=logging.INFO,
    format='%(asctime)s - %(name)s - %(levelname)s - %(message)s'
)
logger = logging.getLogger(__name__)

# Initialize FastAPI app
app = FastAPI(
    title="User Management Service",
    description="""
    User Management Service for the Order Management System.
    
    This service provides JWT-based authentication endpoints:
    - Login to get JWT token
    - Logout to invalidate JWT token
    
    The service implements the OpenAPI specification for user authentication
    and replaces the legacy Spring Security-based authentication system.
    """,
    version="2.0.0",
    contact={
        "name": "Order Management System Team",
        "email": "support@ordermanagement.com"
    },
    license_info={
        "name": "MIT",
        "url": "https://opensource.org/licenses/MIT"
    }
)

# Add CORS middleware
app.add_middleware(
    CORSMiddleware,
    allow_origins=["*"],  # Configure appropriately for production
    allow_credentials=True,
    allow_methods=["*"],
    allow_headers=["*"],
)

# Security scheme
security = HTTPBearer()

# Dependency to get current user from JWT token
async def get_current_user(
    credentials: HTTPAuthorizationCredentials = Security(security),
    db: Session = Depends(get_db)
) -> Dict[str, Any]:
    """
    Dependency to get current authenticated user from JWT token.
    Equivalent to Spring Security's @AuthenticationPrincipal.
    """
    try:
        token = credentials.credentials
        auth_service = get_auth_service(db)
        
        # Verify token
        payload = auth_service.verify_token(token)
        if not payload:
            raise HTTPException(
                status_code=status.HTTP_401_UNAUTHORIZED,
                detail="Invalid or expired token",
                headers={"WWW-Authenticate": "Bearer"},
            )
        
        return payload
        
    except HTTPException:
        raise
    except Exception as e:
        logger.error(f"Error getting current user: {e}")
        raise HTTPException(
            status_code=status.HTTP_401_UNAUTHORIZED,
            detail="Invalid authentication credentials",
            headers={"WWW-Authenticate": "Bearer"},
        )

# Application startup and shutdown events
@app.on_event("startup")
async def startup_event():
    """
    Application startup event.
    Initialize database and create default data.
    """
    try:
        logger.info("Starting User Management Service...")
        init_database()
        logger.info("User Management Service started successfully")
    except Exception as e:
        logger.error(f"Error during startup: {e}")
        raise

@app.on_event("shutdown")
async def shutdown_event():
    """
    Application shutdown event.
    """
    logger.info("User Management Service shutdown")

# Health check endpoints
@app.get("/health", tags=["Health"])
async def health_check():
    """
    Health check endpoint.
    """
    db_healthy = DatabaseHealthCheck.check_connection()
    
    return {
        "status": "healthy" if db_healthy else "unhealthy",
        "service": "user-management",
        "version": "2.0.0",
        "timestamp": datetime.utcnow(),
        "database": "connected" if db_healthy else "disconnected",
        "database_info": DatabaseHealthCheck.get_connection_info() if db_healthy else None
    }

@app.get("/", tags=["Root"])
async def root():
    """
    Root endpoint.
    """
    return {
        "service": "User Management Service",
        "version": "2.0.0",
        "description": "JWT-based authentication service for Order Management System",
        "endpoints": {
            "login": "/user-management/login",
            "logout": "/user-management/logout",
            "health": "/health",
            "docs": "/docs"
        }
    }

# Authentication endpoints
@app.post(
    "/user-management/login",
    response_model=JWTAuthResponse,
    status_code=status.HTTP_200_OK,
    responses={
        401: {
            "model": ErrorResponse,
            "description": "Authentication failed",
            "content": {
                "application/json": {
                    "example": {
                        "message": "Invalid username or password",
                        "timestamp": "2025-07-22T10:30:00Z",
                        "status": 401
                    }
                }
            }
        }
    },
    tags=["Authentication"],
    summary="JWT Authentication login",
    description="""
    Authenticates a user and returns a JWT token for API access. The token should be 
    included in the Authorization header as 'Bearer {token}' for subsequent API calls.
    Tokens have a configurable expiration time (default 24 hours).
    """,
    operation_id="authenticateJWT"
)
async def login(
    login_request: LoginRequest,
    db: Session = Depends(get_db)
) -> JWTAuthResponse:
    """
    JWT Authentication login endpoint.
    
    Equivalent to the authentication functionality in the legacy LoginController
    combined with Spring Security authentication.
    """
    try:
        logger.info(f"Login attempt for user: {login_request.username}")
        
        # Get authentication service
        auth_service = get_auth_service(db)
        
        # Authenticate user and generate JWT token
        auth_response = auth_service.authenticate_user(login_request)
        
        logger.info(f"Login successful for user: {login_request.username}")
        return auth_response
        
    except HTTPException:
        raise
    except Exception as e:
        logger.error(f"Error during login: {e}")
        raise HTTPException(
            status_code=status.HTTP_500_INTERNAL_SERVER_ERROR,
            detail="Internal server error during authentication"
        )

@app.post(
    "/user-management/logout",
    response_model=LogoutResponse,
    status_code=status.HTTP_200_OK,
    responses={
        401: {
            "model": ErrorResponse,
            "description": "Invalid or expired token",
            "content": {
                "application/json": {
                    "example": {
                        "message": "Error on logout: Invalid or expired token",
                        "timestamp": "2025-07-22T10:30:00Z",
                        "status": 401
                    }
                }
            }
        }
    },
    tags=["Authentication"],
    summary="Logout and invalidate JWT token",
    description="""
    Logs out the current user and invalidates the JWT token. The token will be added to a blacklist
    and cannot be used for further API calls.
    """,
    operation_id="logoutJWT"
)
async def logout(
    current_user: Dict[str, Any] = Depends(get_current_user),
    credentials: HTTPAuthorizationCredentials = Security(security),
    db: Session = Depends(get_db)
) -> LogoutResponse:
    """
    Logout endpoint to invalidate JWT token.
    
    Equivalent to Spring Security's logout functionality but using JWT token blacklisting.
    """
    try:
        token = credentials.credentials
        logger.info(f"Logout attempt for user: {current_user.get('username')}")
        
        # Get authentication service
        auth_service = get_auth_service(db)
        
        # Logout user (blacklist token)
        logout_result = auth_service.logout_user(token)
        
        logger.info(f"Logout successful for user: {current_user.get('username')}")
        
        return LogoutResponse(
            message=logout_result["message"],
            timestamp=logout_result["timestamp"]
        )
        
    except HTTPException:
        raise
    except Exception as e:
        logger.error(f"Error during logout: {e}")
        raise HTTPException(
            status_code=status.HTTP_500_INTERNAL_SERVER_ERROR,
            detail="Internal server error during logout"
        )

# Admin endpoints for token management
@app.post("/admin/cleanup-tokens", tags=["Admin"])
async def cleanup_expired_tokens(
    current_user: Dict[str, Any] = Depends(get_current_user),
    db: Session = Depends(get_db)
):
    """
    Admin endpoint to cleanup expired blacklisted tokens.
    Requires admin role.
    """
    # Check if user has admin role
    user_roles = current_user.get("roles", [])
    if "ADMIN" not in user_roles:
        raise HTTPException(
            status_code=status.HTTP_403_FORBIDDEN,
            detail="Admin access required"
        )
    
    try:
        cleanup_service = TokenCleanupService(db)
        cleaned_count = cleanup_service.cleanup_expired_tokens()
        
        return {
            "message": f"Cleaned up {cleaned_count} expired tokens",
            "timestamp": datetime.utcnow(),
            "cleaned_tokens": cleaned_count
        }
        
    except Exception as e:
        logger.error(f"Error cleaning up tokens: {e}")
        raise HTTPException(
            status_code=status.HTTP_500_INTERNAL_SERVER_ERROR,
            detail="Error cleaning up expired tokens"
        )

# Exception handlers
@app.exception_handler(HTTPException)
async def http_exception_handler(request, exc: HTTPException):
    """
    Global HTTP exception handler.
    """
    return {
        "message": exc.detail,
        "timestamp": datetime.utcnow(),
        "status": exc.status_code
    }

@app.exception_handler(Exception)
async def general_exception_handler(request, exc: Exception):
    """
    Global exception handler for unhandled exceptions.
    """
    logger.error(f"Unhandled exception: {exc}")
    return {
        "message": "Internal server error",
        "timestamp": datetime.utcnow(),
        "status": 500
    }

# Main entry point
def main():
    """
    Main entry point for the application.
    """
    # Configuration
    host = os.getenv("HOST", "0.0.0.0")
    port = int(os.getenv("PORT", "8001"))
    reload = os.getenv("RELOAD", "true").lower() == "true"
    
    logger.info(f"Starting User Management Service on {host}:{port}")
    
    # Run the application
    uvicorn.run(
        "api:app",
        host=host,
        port=port,
        reload=reload,
        log_level="info"
    )

if __name__ == "__main__":
    main()
