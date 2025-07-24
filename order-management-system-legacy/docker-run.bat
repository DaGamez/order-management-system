@echo off
echo Building Order Management System with Docker (connecting to XAMPP MySQL)...

echo.
echo ===================================
echo PRE-REQUISITES CHECK
echo ===================================
echo Make sure XAMPP MySQL is running:
echo 1. Start XAMPP Control Panel
echo 2. Start MySQL service
echo 3. MySQL should be running on port 3306
echo.
pause

echo.
echo Step 1: Building Maven project...
call mvn clean package -DskipTests

if %ERRORLEVEL% neq 0 (
    echo Maven build failed!
    exit /b 1
)

echo.
echo Step 2: Building Docker images...
docker-compose build

if %ERRORLEVEL% neq 0 (
    echo Docker build failed!
    exit /b 1
)

echo.
echo Step 3: Starting services...
docker-compose up -d

if %ERRORLEVEL% neq 0 (
    echo Docker compose up failed!
    exit /b 1
)

echo.
echo ===================================
echo Order Management System is starting!
echo ===================================
echo.
echo Web UI: http://localhost:8080
echo API: http://localhost:8080/api/products
echo Database: Connected to XAMPP MySQL on localhost:3306
echo.
echo To view logs: docker-compose logs -f
echo To stop: docker-compose down
echo.
echo Waiting for services to be ready...
timeout /t 15

echo Checking service health...
docker-compose ps

echo.
echo Testing database connection...
timeout /t 5
echo If you see connection errors, make sure:
echo 1. XAMPP MySQL is running
echo 2. Port 3306 is not blocked by firewall
echo 3. Database 'order_management_system_db' exists (it will be created automatically)
