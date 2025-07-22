# User Management Service

FastAPI-based User Management microservice that implements JWT authentication for the Order Management System.

## Overview

This service replaces the legacy Spring Security-based authentication system with a modern FastAPI implementation that provides:

- JWT-based authentication
- User login and logout functionality
- Token blacklisting for secure logout
- RESTful API following OpenAPI 3.0 specification
- MySQL database integration
- Password hashing with bcrypt

## Architecture

The service is organized into the following modules:

- `api.py` - FastAPI application and endpoint definitions (replaces LoginController.java)
- `services.py` - Business logic and authentication services (replaces UserService.java and CustomUserDetailsService.java)
- `models.py` - Data models and Pydantic schemas (replaces User.java and UserRepository.java)
- `db.py` - Database configuration and connection management

## API Endpoints

### Authentication

- `POST /user-management/login` - Authenticate user and get JWT token
- `POST /user-management/logout` - Logout user and invalidate JWT token

### Health & Monitoring

- `GET /health` - Service health check
- `GET /` - Service information
- `POST /admin/cleanup-tokens` - Cleanup expired tokens (admin only)

## Installation

### Prerequisites

- Python 3.8 or higher
- MySQL 5.7 or higher
- pip (Python package manager)

### Setup

1. **Install dependencies:**
   ```bash
   pip install -r requirements.txt
   ```

2. **Configure environment:**
   ```bash
   # Copy the example environment file
   copy .env.example .env
   
   # Edit .env with your database configuration
   notepad .env
   ```

3. **Configure database:**
   - Ensure MySQL is running
   - Create database `order_management_system_db` (or configure different name in .env)
   - Update database credentials in `.env` file

## Running the Service

### Method 1: Using the start script (Windows)
```bash
start.bat
```

### Method 2: Using Python directly
```bash
python main.py
```

### Method 3: Using uvicorn directly
```bash
uvicorn api:app --host 0.0.0.0 --port 8001 --reload
```

The service will start on `http://localhost:8001`

## API Documentation

Once the service is running, you can access:

- **Interactive API documentation:** http://localhost:8001/docs
- **OpenAPI specification:** http://localhost:8001/openapi.json
- **Health check:** http://localhost:8001/health

## Usage Examples

### Login

```bash
curl -X POST "http://localhost:8001/user-management/login" \
     -H "Content-Type: application/json" \
     -d '{
       "username": "admin",
       "password": "admin123"
     }'
```

Response:
```json
{
  "token": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...",
  "tokenType": "Bearer",
  "expiresIn": 86400,
  "issuedAt": "2025-07-22T10:30:45.123Z",
  "user": {
    "id": 1,
    "username": "admin",
    "roles": ["USER", "ADMIN", "API_USER"]
  }
}
```

### Logout

```bash
curl -X POST "http://localhost:8001/user-management/logout" \
     -H "Authorization: Bearer YOUR_JWT_TOKEN"
```

Response:
```json
{
  "message": "Logout successful",
  "timestamp": "2025-07-22T10:30:45.123Z"
}
```

## Configuration

### Environment Variables

| Variable | Default | Description |
|----------|---------|-------------|
| `DB_HOST` | localhost | Database host |
| `DB_PORT` | 3306 | Database port |
| `DB_NAME` | order_management_system_db | Database name |
| `DB_USER` | root | Database username |
| `DB_PASSWORD` | (empty) | Database password |
| `JWT_SECRET_KEY` | your-secret-key-change-in-production | JWT signing key |
| `JWT_ALGORITHM` | HS256 | JWT algorithm |
| `JWT_EXPIRE_HOURS` | 24 | Token expiration time in hours |
| `HOST` | 0.0.0.0 | Server host |
| `PORT` | 8001 | Server port |

### Default Users

The service creates default users on startup:

- **Admin User:** username=`admin`, password=`admin123`
- **Regular User:** username=`user`, password=`user123`

**⚠️ Important:** Change these default credentials in production!

## Database Schema

The service automatically creates the following tables:

- `users` - User accounts
- `user_roles` - User role assignments
- `token_blacklist` - Blacklisted JWT tokens

## Security Features

- **Password Hashing:** Uses bcrypt for secure password storage
- **JWT Tokens:** Stateless authentication with configurable expiration
- **Token Blacklisting:** Secure logout by invalidating tokens
- **Role-based Access:** Support for USER, ADMIN, and API_USER roles
- **Input Validation:** Pydantic models for request validation

## Development

### Project Structure

```
user-management/
├── api.py              # FastAPI application
├── services.py         # Business logic
├── models.py           # Data models
├── db.py              # Database configuration
├── main.py            # Entry point
├── requirements.txt   # Dependencies
├── .env.example       # Environment template
├── start.bat          # Windows start script
└── README.md          # This file
```

### Testing

The service can be tested using the interactive documentation at `/docs` or with tools like:

- Postman
- curl
- httpx (Python)
- Any HTTP client

## Migration from Legacy System

This service replaces the following legacy components:

| Legacy Component | FastAPI Equivalent |
|------------------|-------------------|
| `LoginController.java` | `api.py` |
| `UserService.java` | `services.py` (UserService class) |
| `CustomUserDetailsService.java` | `services.py` (AuthenticationService class) |
| `User.java` | `models.py` (User model) |
| `UserRepository.java` | `models.py` + `services.py` |
| `SecurityConfig.java` | JWT security in `api.py` |
| `application.properties` | `.env` file |

## Troubleshooting

### Common Issues

1. **Database Connection Error:**
   - Check MySQL is running
   - Verify database credentials in `.env`
   - Ensure database exists

2. **Port Already in Use:**
   - Change PORT in `.env` file
   - Or stop other services using port 8001

3. **Import Errors:**
   - Install dependencies: `pip install -r requirements.txt`
   - Check Python version (3.8+ required)

4. **Authentication Errors:**
   - Verify JWT_SECRET_KEY is set
   - Check token format in Authorization header
   - Ensure token hasn't expired

### Logs

The service logs important events including:
- Authentication attempts
- Database operations
- Errors and warnings

Monitor logs for troubleshooting issues.

## Production Deployment

For production deployment:

1. **Security:**
   - Change default JWT_SECRET_KEY
   - Use strong database passwords
   - Configure HTTPS/TLS
   - Change default user passwords

2. **Performance:**
   - Configure database connection pooling
   - Use production WSGI server (e.g., Gunicorn)
   - Set appropriate worker processes

3. **Monitoring:**
   - Monitor `/health` endpoint
   - Set up log aggregation
   - Configure metrics collection

## Support

For issues and questions:
- Email: support@ordermanagement.com
- Check logs for error details
- Review API documentation at `/docs`