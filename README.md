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
- MySQL Server

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
