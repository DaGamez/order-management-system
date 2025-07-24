@echo off
echo Building Order Management System for AWS Elastic Beanstalk deployment...

echo.
echo Step 1: Building Maven project...
call mvn clean package -DskipTests

if %ERRORLEVEL% neq 0 (
    echo Maven build failed!
    exit /b 1
)

echo.
echo Step 2: Building Docker image for AWS...
docker build -t order-management-system .

if %ERRORLEVEL% neq 0 (
    echo Docker build failed!
    exit /b 1
)

echo.
echo Step 3: Creating deployment package...
if exist deployment-package (
    rmdir /s /q deployment-package
)
mkdir deployment-package

copy Dockerrun.aws.json deployment-package\
copy Dockerfile deployment-package\
xcopy target\*.jar deployment-package\target\ /I

echo.
echo ===================================
echo AWS Deployment Package Ready!
echo ===================================
echo.
echo Package location: deployment-package\
echo.
echo Next steps:
echo 1. Set up your RDS MariaDB instance in AWS
echo 2. Configure environment variables in Beanstalk:
echo    - DB_URL: jdbc:mariadb://your-rds-endpoint:3306/order_management_system_db?useSSL=true
echo    - DB_USERNAME: your-username
echo    - DB_PASSWORD: your-password
echo    - AWS_REGION: your-aws-region
echo 3. Upload the deployment-package folder to Elastic Beanstalk
echo.
echo Environment Variables Template:
echo DB_URL=jdbc:mariadb://your-rds-endpoint.region.rds.amazonaws.com:3306/order_management_system_db?useSSL=true^&serverTimezone=UTC
echo DB_USERNAME=your_username
echo DB_PASSWORD=your_password
echo AWS_REGION=us-east-1
echo SERVER_PORT=5000
