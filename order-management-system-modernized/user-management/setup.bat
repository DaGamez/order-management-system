@echo off
echo ============================================
echo User Management Service - Setup Script
echo ============================================

REM Check Python installation
echo Checking Python installation...
python --version >nul 2>&1
if errorlevel 1 (
    echo ERROR: Python is not installed or not in PATH
    echo Please install Python 3.8 or higher from https://python.org
    pause
    exit /b 1
)

REM Display Python version
echo Python version:
python --version

REM Check pip installation
echo Checking pip installation...
pip --version >nul 2>&1
if errorlevel 1 (
    echo ERROR: pip is not available
    echo Please ensure pip is installed with Python
    pause
    exit /b 1
)

REM Install dependencies
echo.
echo Installing dependencies...
pip install -r requirements.txt
if errorlevel 1 (
    echo ERROR: Failed to install dependencies
    echo Please check your internet connection and try again
    pause
    exit /b 1
)

REM Create environment file
echo.
echo Setting up environment configuration...
if not exist .env (
    if exist .env.example (
        copy .env.example .env
        echo Created .env file from .env.example
        echo Please edit .env file with your database configuration
    ) else (
        echo WARNING: .env.example not found
    )
) else (
    echo .env file already exists
)

REM Check database connection (optional)
echo.
echo ============================================
echo Setup completed successfully!
echo ============================================
echo.
echo Next steps:
echo 1. Edit .env file with your database configuration
echo 2. Ensure MySQL is running and accessible
echo 3. Ensure database tables and users already exist
echo 4. Run 'start.bat' to start the service
echo.
echo Service will be available at: http://localhost:8001
echo API Documentation: http://localhost:8001/docs
echo.
pause
