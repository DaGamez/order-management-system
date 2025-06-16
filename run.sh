#!/bin/bash

echo "Starting Order Management System Application..."
echo ""

# Check if Maven is installed
if ! command -v mvn &> /dev/null; then
    echo "Maven is not installed or not in your PATH."
    echo "Please install Maven before running this script."
    echo "You can download it from https://maven.apache.org/download.cgi"
    echo ""
    exit 1
fi

echo "Building the application..."
mvn clean package -DskipTests

if [ $? -ne 0 ]; then
    echo ""
    echo "Build failed! Please check the errors above."
    exit 1
fi

echo ""
echo "Build successful!"
echo ""
echo "Running the application..."
echo ""
echo "The application will be available at:"
echo "- Web UI: http://localhost:8080/"
echo "- API: http://localhost:8080/api/products"
echo ""
echo "Press Ctrl+C to stop the application when done."
echo ""
mvn spring-boot:run
