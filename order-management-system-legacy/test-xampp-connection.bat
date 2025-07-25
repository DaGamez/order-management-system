@echo off
echo Testing XAMPP MySQL connection for Docker...

echo.
echo Checking if XAMPP MySQL is accessible...

echo Testing connection with docker run...
docker run --rm mysql:8.0 mysql -h host.docker.internal -u root -p -e "SELECT 'Connection successful!' as result;"

if %ERRORLEVEL% neq 0 (
    echo.
    echo ===================================
    echo CONNECTION TEST FAILED
    echo ===================================
    echo.
    echo Possible issues:
    echo 1. XAMPP MySQL is not running
    echo 2. MySQL port 3306 is not accessible
    echo 3. Root password is required but not provided
    echo.
    echo Solutions:
    echo 1. Start XAMPP Control Panel and start MySQL
    echo 2. Check if port 3306 is open in firewall
    echo 3. If MySQL has a password, update docker-compose.yml
    echo.
) else (
    echo.
    echo ===================================
    echo CONNECTION SUCCESSFUL
    echo ===================================
    echo Docker can connect to XAMPP MySQL!
)

pause
