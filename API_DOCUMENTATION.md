# Order Management System API Documentation

This document provides details about the RESTful API endpoints available for the Order Management System.

## Base URL

```
http://localhost:8080/api
```

## Authentication

API endpoints are secured with HTTP Basic Authentication.

```
Username: api
Password: api123
```

Include these credentials with each request to authenticate.

## Endpoints

### Products

### Get All Products

Retrieves a list of all products.

**URL**: `/products`

**Method**: `GET`

**Example Request**:
```bash
curl -X GET http://localhost:8080/api/products -u api:api123
```

**Example Response**:
```json
[
  {
    "id": 1,
    "name": "Laptop",
    "description": "High-performance laptop with SSD",
    "price": 1200.00,
    "createdAt": "2025-06-12T10:30:45.123+00:00"
  },
  {
    "id": 2,
    "name": "Smartphone",
    "description": "Latest model with advanced camera",
    "price": 800.00,
    "createdAt": "2025-06-12T10:30:45.456+00:00"
  }
]
```

### Get Product by ID

Retrieves a specific product by its ID.

**URL**: `/products/{id}`

**Method**: `GET`

**URL Parameters**: `id=[long]` - The ID of the product to retrieve

**Example Request**:
```bash
curl -X GET http://localhost:8080/api/products/1 -u api:api123
```

**Example Success Response**:
```json
{
  "id": 1,
  "name": "Laptop",
  "description": "High-performance laptop with SSD",
  "price": 1200.00,
  "createdAt": "2025-06-12T10:30:45.123+00:00"
}
```

**Example Error Response**:
```json
{
  "message": "Product not found with id: 1"
}
```

### Create Product

Creates a new product.

**URL**: `/products`

**Method**: `POST`

**Request Body**:
```json
{
  "name": "Monitor",
  "description": "27-inch 4K display",
  "price": 450.00
}
```

**Example Request**:
```bash
curl -X POST http://localhost:8080/api/products \
  -u api:api123 \
  -H "Content-Type: application/json" \
  -d '{"name":"Monitor","description":"27-inch 4K display","price":450.00}'
```

**Example Response**:
```json
{
  "id": 3,
  "name": "Monitor",
  "description": "27-inch 4K display",
  "price": 450.00,
  "createdAt": "2025-06-12T14:20:11.789+00:00"
}
```

### Update Product

Updates an existing product.

**URL**: `/products/{id}`

**Method**: `PUT`

**URL Parameters**: `id=[long]` - The ID of the product to update

**Request Body**:
```json
{
  "name": "Updated Monitor",
  "description": "32-inch 4K display",
  "price": 550.00
}
```

**Example Request**:
```bash
curl -X PUT http://localhost:8080/api/products/3 \
  -u api:api123 \
  -H "Content-Type: application/json" \
  -d '{"name":"Updated Monitor","description":"32-inch 4K display","price":550.00}'
```

**Example Success Response**:
```json
{
  "id": 3,
  "name": "Updated Monitor",
  "description": "32-inch 4K display",
  "price": 550.00,
  "createdAt": "2025-06-12T14:20:11.789+00:00"
}
```

**Example Error Response**:
```json
{
  "message": "Product not found with id: 3"
}
```

### Delete Product

Deletes a product.

**URL**: `/products/{id}`

**Method**: `DELETE`

**URL Parameters**: `id=[long]` - The ID of the product to delete

**Example Request**:
```bash
curl -X DELETE http://localhost:8080/api/products/3 -u api:api123
```

**Example Success Response**:
```json
{
  "message": "Product successfully deleted"
}
```

**Example Error Response**:
```json
{
  "message": "Product not found with id: 3"
}
```

### Search Products by Name

Searches for products with names containing the provided string (case-insensitive).

**URL**: `/products/search`

**Method**: `GET`

**Query Parameters**: `name=[string]` - The search string

**Example Request**:
```bash
curl -X GET "http://localhost:8080/api/products/search?name=laptop" -u api:api123
```

**Example Response**:
```json
[
  {
    "id": 1,
    "name": "Laptop",
    "description": "High-performance laptop with SSD",
    "price": 1200.00,
    "createdAt": "2025-06-12T10:30:45.123+00:00"
  }
]
```

### Filter Products by Price Less Than

Retrieves products with a price less than the provided value.

**URL**: `/products/filter/price-less-than`

**Method**: `GET`

**Query Parameters**: `price=[double]` - The maximum price

**Example Request**:
```bash
curl -X GET "http://localhost:8080/api/products/filter/price-less-than?price=1000" -u api:api123
```

**Example Response**:
```json
[
  {
    "id": 2,
    "name": "Smartphone",
    "description": "Latest model with advanced camera",
    "price": 800.00,
    "createdAt": "2025-06-12T10:30:45.456+00:00"
  }
]
```

### Filter Products by Price Greater Than

Retrieves products with a price greater than the provided value.

**URL**: `/products/filter/price-greater-than`

**Method**: `GET`

**Query Parameters**: `price=[double]` - The minimum price

**Example Request**:
```bash
curl -X GET "http://localhost:8080/api/products/filter/price-greater-than?price=1000" -u api:api123
```

**Example Response**:
```json
[
  {
    "id": 1,
    "name": "Laptop",
    "description": "High-performance laptop with SSD",
    "price": 1200.00,
    "createdAt": "2025-06-12T10:30:45.123+00:00"
  }
]
```

### User Management

> Note: User management is only accessible to users with ADMIN role.

#### Get All Users

Retrieves a list of all users.

**URL**: `/users` (Web Interface)

**Method**: `GET`

**Required Role**: `ADMIN`

**Response**: Returns the user management page with a list of all users.

#### Add New User

Shows the form to create a new user.

**URL**: `/users/new` (Web Interface)

**Method**: `GET`

**Required Role**: `ADMIN`

**Response**: Returns the user form page.

#### Save User

Creates a new user or updates an existing user.

**URL**: `/users/save` (Web Interface)

**Method**: `POST`

**Required Role**: `ADMIN`

**Form Data**:
- id (null for new users)
- username (required, unique)
- password (required for new users)
- roles (optional, multiple checkboxes)
- enabled (boolean)

**Response**: Redirects to the user management page with a success message.

#### Edit User

Shows the form to edit an existing user.

**URL**: `/users/edit/{id}` (Web Interface)

**Method**: `GET`

**Required Role**: `ADMIN`

**Path Parameters**:
- id (required): The ID of the user to edit.

**Response**: Returns the user form page with user data pre-filled.

#### Delete User

Deletes an existing user.

**URL**: `/users/delete/{id}` (Web Interface)

**Method**: `GET`

**Required Role**: `ADMIN`

**Path Parameters**:
- id (required): The ID of the user to delete.

**Response**: Redirects to the user management page with a success message.
