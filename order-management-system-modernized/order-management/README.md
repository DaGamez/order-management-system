# Order Management Service

FastAPI-based Order Management microservice for the Order Management System.

## Overview

Este servicio reemplaza la lógica legacy de gestión de órdenes y productos con una implementación moderna en FastAPI:

- API RESTful para gestión de órdenes y productos
- Integración con autenticación JWT (a través de user-management)
- ORM SQLAlchemy para acceso a base de datos
- Cumplimiento de OpenAPI 3.0

## Architecture

El servicio está organizado en los siguientes módulos:

- `api.py` - Aplicación FastAPI y definición de endpoints (migrado de OrderController.java)
- `services.py` - Lógica de negocio para órdenes y productos (migrado de OrderService.java)
- `models.py` - Modelos de datos SQLAlchemy y esquemas Pydantic (migrado de Order.java/Product.java)
- `db.py` - Configuración y conexión a la base de datos

## API Endpoints

- `POST /order-management/orders` - Crear nueva orden
- `GET /order-management/orders` - Listar todas las órdenes
- `GET /order-management/products` - Listar todos los productos

## Installation

### Prerequisitos

- Python 3.12+
- Docker (opcional)

### Setup

1. Instala dependencias:
   ```bash
   pip install -r requirements.txt
   ```
2. Configura la base de datos (por defecto SQLite, configurable por env):
   - Edita la variable `DATABASE_URL` en `db.py` o usa variable de entorno

## Running the Service

### Local
```bash
uvicorn api:app --host 0.0.0.0 --port 8002 --reload
```

### Docker
```bash
docker build -t order-management:latest .
docker-compose up
```

El servicio estará disponible en `http://localhost:8002`

## API Documentation

- **Swagger UI:** http://localhost:8002/docs
- **OpenAPI spec:** http://localhost:8002/openapi.json

## Usage Examples

### Crear Orden
```bash
curl -X POST "http://localhost:8002/order-management/orders" \
     -H "Authorization: Bearer YOUR_JWT_TOKEN" \
     -d '{"product_id": 1, "quantity": 2, "comments": "Entrega urgente"}'
```

### Listar Productos
```bash
curl -X GET "http://localhost:8002/order-management/products" \
     -H "Authorization: Bearer YOUR_JWT_TOKEN"
```

## Configuration

| Variable         | Default                | Description                  |
|------------------|------------------------|------------------------------|
| `DATABASE_URL`   | sqlite:///./orders.db  | DB connection string         |
| `HOST`           | 0.0.0.0                | Server host                  |
| `PORT`           | 8002                   | Server port                  |

## Project Structure

```
order-management/
├── api.py              # FastAPI application
├── services.py         # Business logic
├── models.py           # Data models & schemas
├── db.py               # Database config
├── main.py             # Entry point
├── requirements.txt    # Dependencies
├── Dockerfile          # Docker build
├── docker-compose.yml  # Docker compose
├── test_api.py         # Basic tests
└── README.md           # This file
```

## Testing

El servicio incluye pruebas básicas con **pytest** y **httpx**.

```bash
python -m pytest test_api.py -v
```

## Migration from Legacy System

Este microservicio reemplaza los siguientes componentes legacy:

| Legacy Component                | FastAPI Equivalent         |
|---------------------------------|----------------------------|
| `OrderController.java`          | `api.py`                   |
| `OrderService.java`             | `services.py`              |
| `Order.java`/`Product.java`     | `models.py`                |
| `OrderRepository.java`          | `models.py` + `services.py`|
| `application.properties`        | `db.py`/env                |

## Troubleshooting

1. **Import Errors:**
   - Instala dependencias: `pip install -r requirements.txt`
   - Verifica versión de Python (3.12+)
2. **Database Issues:**
   - Verifica la cadena de conexión en `DATABASE_URL`
   - Asegura que la base de datos existe y es accesible
3. **Port Already in Use:**
   - Cambia el puerto en `db.py` o variable de entorno

## Support

Para dudas o problemas:
- Email: support@ordermanagement.com
- Consulta los logs y la documentación en `/docs`