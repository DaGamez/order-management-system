"""
User service layer for the User Management Service.

This module absorbs the logic from:
- order-management-system-legacy/src/main/java/com/example/crud/service/UserService.java
- order-management-system-legacy/src/main/java/com/example/crud/service/CustomUserDetailsService.java
- order-management-system-legacy/src/main/java/com/example/crud/service/UserServiceImpl.java
"""

import os
import jwt
from datetime import datetime, timedelta
from typing import Optional, List, Dict, Any
from sqlalchemy.orm import Session
from sqlalchemy.exc import IntegrityError
from fastapi import HTTPException, status
import logging

from models import (
    User, 
    UserCreate, 
    UserResponse, 
    LoginRequest, 
    JWTAuthResponse,
    UserInfo,
    PasswordManager,
    UserRole,
    TokenBlacklist
)

# Configure logging
logging.basicConfig(level=logging.INFO)
logger = logging.getLogger(__name__)

# JWT Configuration
class JWTConfig:
    """
    JWT configuration class.
    """
    SECRET_KEY = os.getenv("JWT_SECRET_KEY", "your-secret-key-change-in-production")
    ALGORITHM = os.getenv("JWT_ALGORITHM", "HS256")
    ACCESS_TOKEN_EXPIRE_HOURS = int(os.getenv("JWT_EXPIRE_HOURS", "24"))

jwt_config = JWTConfig()

class UserService:
    """
    User service class implementing user management operations.
    Equivalent to UserServiceImpl in the legacy system.
    """
    
    def __init__(self, db_session: Session):
        self.db = db_session
    
    def get_all_users(self) -> List[UserResponse]:
        """
        Get all users.
        Equivalent to getAllUsers() in UserServiceImpl.
        """
        try:
            users = self.db.query(User).all()
            return [self._user_to_response(user) for user in users]
        except Exception as e:
            logger.error(f"Error getting all users: {e}")
            raise HTTPException(
                status_code=status.HTTP_500_INTERNAL_SERVER_ERROR,
                detail="Error retrieving users"
            )
    
    def get_user_by_id(self, user_id: int) -> Optional[UserResponse]:
        """
        Get user by ID.
        Equivalent to getUserById() in UserServiceImpl.
        """
        try:
            user = self.db.query(User).filter(User.id == user_id).first()
            return self._user_to_response(user) if user else None
        except Exception as e:
            logger.error(f"Error getting user by id {user_id}: {e}")
            raise HTTPException(
                status_code=status.HTTP_500_INTERNAL_SERVER_ERROR,
                detail=f"Error retrieving user with id: {user_id}"
            )
    
    def get_user_by_username(self, username: str) -> Optional[User]:
        """
        Get user by username.
        Equivalent to getUserByUsername() in UserServiceImpl.
        """
        try:
            return self.db.query(User).filter(User.username == username).first()
        except Exception as e:
            logger.error(f"Error getting user by username {username}: {e}")
            raise HTTPException(
                status_code=status.HTTP_500_INTERNAL_SERVER_ERROR,
                detail=f"Error retrieving user with username: {username}"
            )
    
    def create_user(self, user_data: UserCreate) -> UserResponse:
        """
        Create a new user.
        Equivalent to createUser() in UserServiceImpl.
        """
        try:
            # Check if username already exists
            if self.username_exists(user_data.username):
                raise HTTPException(
                    status_code=status.HTTP_409_CONFLICT,
                    detail=f"Username '{user_data.username}' already exists"
                )
            
            # Create new user
            db_user = User(
                username=user_data.username,
                password=PasswordManager.hash_password(user_data.password),
                enabled=user_data.enabled
            )
            
            self.db.add(db_user)
            self.db.commit()
            self.db.refresh(db_user)
            
            logger.info(f"User created successfully: {user_data.username}")
            return self._user_to_response(db_user)
            
        except HTTPException:
            raise
        except IntegrityError:
            self.db.rollback()
            raise HTTPException(
                status_code=status.HTTP_409_CONFLICT,
                detail=f"Username '{user_data.username}' already exists"
            )
        except Exception as e:
            self.db.rollback()
            logger.error(f"Error creating user: {e}")
            raise HTTPException(
                status_code=status.HTTP_500_INTERNAL_SERVER_ERROR,
                detail="Error creating user"
            )
    
    def username_exists(self, username: str) -> bool:
        """
        Check if username exists.
        Equivalent to usernameExists() in UserServiceImpl.
        """
        try:
            return self.db.query(User).filter(User.username == username).first() is not None
        except Exception as e:
            logger.error(f"Error checking username existence: {e}")
            return False
    
    def _user_to_response(self, user: User) -> UserResponse:
        """
        Convert User model to UserResponse.
        """
        return UserResponse(
            id=user.id,
            username=user.username,
            enabled=user.enabled,
            roles=UserRole.get_default_roles()  # For now, assign default roles
        )

class AuthenticationService:
    """
    Authentication service class implementing JWT authentication.
    Equivalent to CustomUserDetailsService and Spring Security configuration.
    """
    
    def __init__(self, db_session: Session):
        self.db = db_session
        self.user_service = UserService(db_session)
    
    def authenticate_user(self, login_request: LoginRequest) -> JWTAuthResponse:
        """
        Authenticate user and return JWT token.
        Equivalent to loadUserByUsername() in CustomUserDetailsService 
        combined with JWT token generation.
        """
        try:
            # Find user by username
            user = self.user_service.get_user_by_username(login_request.username)
            
            if not user:
                logger.warning(f"Authentication failed: User not found - {login_request.username}")
                raise HTTPException(
                    status_code=status.HTTP_401_UNAUTHORIZED,
                    detail="Invalid username or password"
                )
            
            # Check if user is enabled
            if not user.enabled:
                logger.warning(f"Authentication failed: User disabled - {login_request.username}")
                raise HTTPException(
                    status_code=status.HTTP_401_UNAUTHORIZED,
                    detail="User account is disabled"
                )
            
            # Verify password
            if not PasswordManager.verify_password(login_request.password, user.password):
                logger.warning(f"Authentication failed: Invalid password - {login_request.username}")
                raise HTTPException(
                    status_code=status.HTTP_401_UNAUTHORIZED,
                    detail="Invalid username or password"
                )
            
            # Generate JWT token
            token_data = self._create_access_token(user)
            
            # Create user info
            user_info = UserInfo(
                id=user.id,
                username=user.username,
                roles=self._get_user_roles(user)
            )
            
            # Create response
            auth_response = JWTAuthResponse(
                token=token_data["token"],
                tokenType="Bearer",
                expiresIn=token_data["expires_in"],
                issuedAt=token_data["issued_at"],
                user=user_info
            )
            
            logger.info(f"User authenticated successfully: {login_request.username}")
            return auth_response
            
        except HTTPException:
            raise
        except Exception as e:
            logger.error(f"Error during authentication: {e}")
            raise HTTPException(
                status_code=status.HTTP_500_INTERNAL_SERVER_ERROR,
                detail="Authentication error"
            )
    
    def logout_user(self, token: str) -> Dict[str, Any]:
        """
        Logout user by blacklisting the JWT token.
        """
        try:
            # Decode token to get expiration
            try:
                payload = jwt.decode(
                    token, 
                    jwt_config.SECRET_KEY, 
                    algorithms=[jwt_config.ALGORITHM]
                )
                exp_timestamp = payload.get("exp")
                expires_at = datetime.fromtimestamp(exp_timestamp) if exp_timestamp else datetime.utcnow() + timedelta(hours=24)
            except jwt.InvalidTokenError:
                raise HTTPException(
                    status_code=status.HTTP_401_UNAUTHORIZED,
                    detail="Error on logout: Invalid or expired token"
                )
            
            # Add token to blacklist
            blacklisted_token = TokenBlacklist(
                token=token,
                expires_at=expires_at
            )
            
            self.db.add(blacklisted_token)
            self.db.commit()
            
            logger.info("User logged out successfully")
            return {
                "message": "Logout successful",
                "timestamp": datetime.utcnow()
            }
            
        except HTTPException:
            raise
        except Exception as e:
            self.db.rollback()
            logger.error(f"Error during logout: {e}")
            raise HTTPException(
                status_code=status.HTTP_500_INTERNAL_SERVER_ERROR,
                detail="Logout error"
            )
    
    def is_token_blacklisted(self, token: str) -> bool:
        """
        Check if token is blacklisted.
        """
        try:
            blacklisted = self.db.query(TokenBlacklist).filter(TokenBlacklist.token == token).first()
            return blacklisted is not None
        except Exception as e:
            logger.error(f"Error checking token blacklist: {e}")
            return False
    
    def verify_token(self, token: str) -> Optional[Dict[str, Any]]:
        """
        Verify JWT token and return payload.
        """
        try:
            # Check if token is blacklisted
            if self.is_token_blacklisted(token):
                return None
            
            # Decode and verify token
            payload = jwt.decode(
                token, 
                jwt_config.SECRET_KEY, 
                algorithms=[jwt_config.ALGORITHM]
            )
            
            return payload
            
        except jwt.ExpiredSignatureError:
            logger.warning("Token has expired")
            return None
        except jwt.InvalidTokenError:
            logger.warning("Invalid token")
            return None
        except Exception as e:
            logger.error(f"Error verifying token: {e}")
            return None
    
    def _create_access_token(self, user: User) -> Dict[str, Any]:
        """
        Create JWT access token.
        """
        issued_at = datetime.utcnow()
        expires_delta = timedelta(hours=jwt_config.ACCESS_TOKEN_EXPIRE_HOURS)
        expires_at = issued_at + expires_delta
        
        payload = {
            "sub": str(user.id),
            "username": user.username,
            "roles": self._get_user_roles(user),
            "iat": issued_at,
            "exp": expires_at
        }
        
        token = jwt.encode(payload, jwt_config.SECRET_KEY, algorithm=jwt_config.ALGORITHM)
        
        return {
            "token": token,
            "expires_in": int(expires_delta.total_seconds()),
            "issued_at": issued_at
        }
    
    def _get_user_roles(self, user: User) -> List[str]:
        """
        Get user roles.
        For now, assign default roles based on username.
        In a real implementation, this would query a roles table.
        """
        if user.username == "admin":
            return [UserRole.USER, UserRole.ADMIN, UserRole.API_USER]
        else:
            return [UserRole.USER, UserRole.API_USER]

# Utility functions
def get_user_service(db_session: Session) -> UserService:
    """
    Factory function to get UserService instance.
    """
    return UserService(db_session)

def get_auth_service(db_session: Session) -> AuthenticationService:
    """
    Factory function to get AuthenticationService instance.
    """
    return AuthenticationService(db_session)

# Token cleanup utility
class TokenCleanupService:
    """
    Service for cleaning up expired blacklisted tokens.
    """
    
    def __init__(self, db_session: Session):
        self.db = db_session
    
    def cleanup_expired_tokens(self) -> int:
        """
        Remove expired tokens from blacklist.
        Returns the number of tokens removed.
        """
        try:
            now = datetime.utcnow()
            expired_tokens = self.db.query(TokenBlacklist).filter(
                TokenBlacklist.expires_at < now
            )
            
            count = expired_tokens.count()
            expired_tokens.delete()
            self.db.commit()
            
            logger.info(f"Cleaned up {count} expired blacklisted tokens")
            return count
            
        except Exception as e:
            self.db.rollback()
            logger.error(f"Error cleaning up expired tokens: {e}")
            raise
