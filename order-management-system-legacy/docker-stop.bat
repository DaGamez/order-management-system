@echo off
echo Stopping Order Management System Docker containers...

docker-compose down

if %ERRORLEVEL% neq 0 (
    echo Failed to stop containers!
    exit /b 1
)

echo.
echo Containers stopped successfully!
echo.
echo To remove volumes as well, run: docker-compose down -v
echo To remove images as well, run: docker-compose down --rmi all
