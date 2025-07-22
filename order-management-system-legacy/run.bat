@echo off
echo Starting Order Management System Application...
echo.

REM Check if Maven is installed
where mvn >nul 2>nul
if %errorlevel% neq 0 (
    echo Maven is not installed or not in your PATH.
    echo Please install Maven before running this script.
    echo You can download it from https://maven.apache.org/download.cgi
    echo.
    pause
    exit /b 1
)

echo Building the application...
call mvn clean package -DskipTests

if %errorlevel% neq 0 (
    echo.
    echo Build failed! Please check the errors above.
    pause
    exit /b 1
)

echo.
echo Build successful!
echo.
echo Running the application...
echo.
echo The application will be available at:
echo - Web UI: http://localhost:8080/
echo - API: http://localhost:8080/api/products
echo.
echo Press Ctrl+C to stop the application when done.
echo.
call mvn spring-boot:run

pause
