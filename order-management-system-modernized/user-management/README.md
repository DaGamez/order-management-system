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
   - Ensure database `order_management_system_db` exists with required tables and users
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

The service expects the following users to exist in the database:

- **Admin User:** username=`admin`, password=`admin123`
- **Regular User:** username=`user`, password=`user123`

**⚠️ Important:** Change these default credentials in production!

## Database Schema

The service expects the following tables to exist in the database:

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

#### Unit Testing

The service includes comprehensive unit tests using **pytest** with async support. The tests cover authentication, error handling, and API endpoints.

##### Test Setup

1. **Activate virtual environment (if using venv2):**
   ```bash
   # Windows PowerShell
   .\venv2\Scripts\Activate.ps1
   
   # Windows CMD
   venv2\Scripts\activate.bat
   
   # Linux/macOS
   source venv2/bin/activate
   ```

2. **Install test dependencies:**
   ```bash
   pip install -r requirements.txt
   ```

##### Running Tests

**Run all tests:**
```bash
python -m pytest test_api.py -v
```

**Run specific test class:**
```bash
# Authentication tests only
python -m pytest test_api.py::TestAuthentication -v

# Health check tests only
python -m pytest test_api.py::TestHealthCheck -v
```

**Run specific test:**
```bash
# Test successful login
python -m pytest test_api.py::TestAuthentication::test_login_success -v

# Test invalid credentials
python -m pytest test_api.py::TestAuthentication::test_login_invalid_credentials -v
```

**Run tests with coverage:**
```bash
python -m pytest test_api.py --cov=. --cov-report=html
```

##### Test Cases

**Authentication Tests (`TestAuthentication`):**
- ✅ `test_login_success` - Successful login with valid credentials
- ✅ `test_login_invalid_credentials` - Login with wrong password
- ✅ `test_login_nonexistent_user` - Login with non-existent username
- ✅ `test_login_validation_error` - Login with missing fields
- ✅ `test_logout_success` - Successful logout with valid token
- ✅ `test_logout_invalid_token` - Logout with invalid token
- ✅ `test_logout_without_token` - Logout without authorization header

**Health Check Tests (`TestHealthCheck`):**
- ✅ `test_health_check` - Health endpoint functionality
- ✅ `test_root_endpoint` - Root endpoint information

##### Test Environment

The tests use:
- **SQLite** in-memory database for isolation
- **pytest-asyncio** for async test support
- **httpx.AsyncClient** for HTTP requests
- **Automatic setup/teardown** of test data
- **Isolated test environment** with proper cleanup

##### Sample Test Output

```bash
========================= test session starts =========================
platform win32 -- Python 3.10.16, pytest-7.4.3
plugins: anyio-3.7.1, asyncio-0.21.1, cov-4.1.0
collecting ... collected 9 items

test_api.py::TestAuthentication::test_login_success PASSED      [ 11%]
test_api.py::TestAuthentication::test_login_invalid_credentials PASSED [ 22%]
test_api.py::TestAuthentication::test_login_nonexistent_user PASSED [ 33%]
test_api.py::TestAuthentication::test_login_validation_error PASSED [ 44%]
test_api.py::TestAuthentication::test_logout_success PASSED    [ 55%]
test_api.py::TestAuthentication::test_logout_invalid_token PASSED [ 66%]
test_api.py::TestAuthentication::test_logout_without_token PASSED [ 77%]
test_api.py::TestHealthCheck::test_health_check PASSED         [ 88%]
test_api.py::TestHealthCheck::test_root_endpoint PASSED        [100%]

====================== 9 passed in 2.34s =====================
```

##### Writing New Tests

To add new tests, follow this pattern:

```python
@pytest.mark.anyio
async def test_new_feature(self, client: AsyncClient, setup_database):
    """Test description."""
    # Arrange
    test_data = {"field": "value"}
    
    # Act
    response = await client.post("/endpoint", json=test_data)
    
    # Assert
    assert response.status_code == 200
    data = response.json()
    assert "expected_field" in data
```

##### Integration Testing

For manual API testing, use the interactive documentation:

- **Swagger UI:** http://localhost:8001/docs
- **ReDoc:** http://localhost:8001/redoc

Or command-line tools:

**Test login with curl:**
```bash
curl -X POST "http://localhost:8001/user-management/login" \
     -H "Content-Type: application/json" \
     -d '{"username": "admin", "password": "admin123"}'
```

**Test with httpx (Python):**
```python
import httpx
import asyncio

async def test_login():
    async with httpx.AsyncClient() as client:
        response = await client.post(
            "http://localhost:8001/user-management/login",
            json={"username": "admin", "password": "admin123"}
        )
        print(response.status_code)
        print(response.json())

asyncio.run(test_login())
```

##### Test Database

Tests use an isolated SQLite database (`test.db`) that is:
- **Created automatically** before each test session
- **Populated with test data** (username: "testuser", password: "testpass123")
- **Cleaned up automatically** after tests complete
- **Independent** from the production MySQL database

##### Continuous Integration

For CI/CD pipelines, add this test command:

```yaml
# GitHub Actions example
- name: Run tests
  run: |
    pip install -r requirements.txt
    python -m pytest test_api.py -v --junitxml=test-results.xml
```

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