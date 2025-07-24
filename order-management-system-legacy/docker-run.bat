@echo off
echo Building Order Management System with Docker...

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
echo.
echo To view logs: docker-compose logs -f
echo To stop: docker-compose down
echo.
echo Waiting for services to be ready...
timeout /t 10

echo Checking service health...
docker-compose ps
