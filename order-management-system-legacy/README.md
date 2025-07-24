# Order Management System

A comprehensive Order Management System application built using Spring Boot and MySQL.

## Features

- Product management with full operations
- User management for administrators
- Role-based access control
- REST API endpoints
- Web UI using Thymeleaf
- Form validations
- Search functionality
- Responsive design

## Technologies Used

- Java 11
- Spring Boot 2.7.0
- Spring Data JPA
- MySQL Database
- Thymeleaf
- HTML/CSS/JavaScript
- Maven

## Prerequisites

- JDK 11 or higher
- Maven
- MySQL Server (for local development)
- Docker and Docker Compose (for containerized deployment)

## Setup Instructions

1. **Clone the repository**

2. **Configure MySQL Database**

   - Make sure MySQL is installed and running
   - Create a database named `order_management_system_db` (optional, the application will create it if it doesn't exist)
   - Update the database configuration in `src/main/resources/application.properties` if needed:
   
   ```
   spring.datasource.url=jdbc:mysql://localhost:3306/order_management_system_db?useSSL=false&serverTimezone=UTC&createDatabaseIfNotExist=true
   spring.datasource.username=root
   spring.datasource.password=root
   ```

3. **Build the project**

   Important to have the DB running at this point.
   
   ```
   mvn clean install
   ```

4. **Run the application**

   ```
   mvn spring-boot:run
   ```

5. **Access the application**

   - Web UI: http://localhost:8080/
   - API: http://localhost:8080/api/products

## Docker Deployment

### Local Development with Docker

For local containerized deployment using Docker and Docker Compose:

#### Quick Start

1. **Run the application with Docker**

   ```bash
   # Windows
   docker-run.bat
   
   # Or manually
   mvn clean package -DskipTests
   docker-compose up --build -d
   ```

2. **Access the application**

   - Web UI: http://localhost:5000/
   - API: http://localhost:5000/api/products
   - MySQL: localhost:3307 (external port)

3. **Stop the application**

   ```bash
   # Windows
   docker-stop.bat
   
   # Or manually
   docker-compose down
   ```

### AWS Elastic Beanstalk Deployment

For production deployment on AWS Elastic Beanstalk with external MariaDB:

#### Prerequisites

1. **AWS RDS MariaDB Instance**
   - Create a MariaDB instance in AWS RDS
   - Note the endpoint, username, and password
   - Ensure the security group allows connections from Beanstalk

2. **AWS Elastic Beanstalk Application**
   - Create a new Beanstalk application
   - Choose "Docker" as the platform

#### Deployment Steps

1. **Build deployment package**

   ```bash
   # Windows
   aws-deploy.bat
   ```

2. **Configure Environment Variables in Beanstalk**

   Set these environment variables in your Beanstalk environment:
   
   ```
   DB_URL=jdbc:mariadb://your-rds-endpoint.region.rds.amazonaws.com:3306/order_management_system_db?useSSL=true&serverTimezone=UTC
   DB_USERNAME=your_username
   DB_PASSWORD=your_password
   AWS_REGION=your-aws-region
   SERVER_PORT=5000
   ADMIN_USERNAME=your_admin_user
   ADMIN_PASSWORD=your_admin_password
   ```

3. **Deploy to Beanstalk**
   - Upload the `deployment-package` folder as a ZIP file
   - Deploy to your Beanstalk environment

4. **Access your application**
   - Web UI: http://your-beanstalk-url/
   - API: http://your-beanstalk-url/api/products

#### AWS Configuration

The application is configured for AWS with:
- **Port 5000**: Standard Beanstalk port
- **Health Checks**: Available at `/actuator/health`
- **MariaDB Support**: Compatible with AWS RDS MariaDB
- **Security**: Non-root user in container
- **Logging**: CloudWatch integration
- **Auto-scaling**: Configured for t3.small instances

### Docker Services (Local Development)

The local Docker setup includes:

- **Spring Boot App**: Order Management System
  - Port: 5000
  - Health Check: http://localhost:5000/actuator/health
  - Profile: `docker` (local) or `aws` (production)

### Docker Commands

```bash
# Build and start services (local)
docker-compose up --build -d

# View logs
docker-compose logs -f

# Stop services
docker-compose down

# Build for AWS deployment
aws-deploy.bat

# Test locally with AWS profile
docker run -e SPRING_PROFILES_ACTIVE=aws -p 5000:5000 order-management-system
```

## API Endpoints

| Method | URL                                   | Description                             |
|-----------|--------------------------------------|----------------------------------------|
| GET       | /api/products                        | Get all products                        |
| GET       | /api/products/{id}                   | Get a product by ID                     |
| POST      | /api/products                        | Create a new product                    |
| PUT       | /api/products/{id}                   | Update a product by ID                  |
| DELETE    | /api/products/{id}                   | Delete a product by ID                  |
| GET       | /api/products/search?name={keyword}  | Search products by name                 |
| GET       | /api/products/filter/price-less-than?price={price}  | Filter products by price less than |
| GET       | /api/products/filter/price-greater-than?price={price} | Filter products by price greater than |

## Web UI Routes

| URL                    | Description                             |
|------------------------|----------------------------------------|
| /products              | Display all products                    |
| /products/new          | Show form to create a new product       |
| /products/edit/{id}    | Show form to edit a product            |
| /products/delete/{id}  | Delete a product                       |
| /products/search?name={keyword} | Search products by name        |

## User Management

The application now includes a user management system that allows administrators to:

- View a list of all users
- Add new users with different roles (USER, ADMIN, API_USER)
- Edit existing users (username, password, roles, enable/disable account)
- Delete users

### Default Users

The application comes with three default users:

1. Regular User:
   - Username: user
   - Password: password
   - Role: USER

2. Admin User:
   - Username: admin
   - Password: admin
   - Roles: USER, ADMIN

3. API User:
   - Username: api
   - Password: api123
   - Role: API_USER

### Access Control

- Only users with the ADMIN role can access the user management interface
- The user management interface is accessible via the navigation menu (only visible to admins)
- RESTful API endpoints are only accessible to users with the API_USER role

## Running Tests

```
mvn test
```

## Project Structure

```
src
├── main
│   ├── java
│   │   └── com
│   │       └── example
│   │           └── crud
│   │               ├── CrudApplication.java
│   │               ├── controller
│   │               │   ├── ProductController.java
│   │               │   └── WebController.java
│   │               ├── exception
│   │               │   └── GlobalExceptionHandler.java
│   │               ├── model
│   │               │   └── Product.java
│   │               ├── repository
│   │               │   └── ProductRepository.java
│   │               └── service
│   │                   ├── ProductService.java
│   │                   └── ProductServiceImpl.java
│   └── resources
│       ├── application.properties
│       ├── static
│       │   ├── css
│       │   │   └── style.css
│       │   └── js
│       │       └── script.js
│       └── templates
│           └── products
│               ├── form.html
│               └── list.html
└── test
    └── java
        └── com
            └── example
                └── crud
                    ├── CrudApplicationTests.java
                    └── controller
                        └── ProductControllerTest.java
```
