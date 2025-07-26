from fastapi import FastAPI, Depends, HTTPException, status
from pydantic import BaseModel
from fastapi.security import OAuth2PasswordBearer
from sqlalchemy.orm import Session
from typing import List, Optional
from db import get_db
from models import Order, Product, User, OrderResponse, ProductResponse, UserResponse
from services import OrderService, ProductService
from datetime import date

app = FastAPI()
oauth2_scheme = OAuth2PasswordBearer(tokenUrl="/user-management/login")

# Dummy user authentication for demonstration
# Replace with JWT validation and user lookup

def get_current_user(token: str = Depends(oauth2_scheme), db: Session = Depends(get_db)) -> User:
    # TODO: Implement JWT validation and user lookup
    user = db.query(User).filter(User.username == "admin").first()
    if not user:
        raise HTTPException(status_code=status.HTTP_401_UNAUTHORIZED, detail="Unauthorized access - please login")
    return user


# Pydantic model for order creation
class OrderCreateRequest(BaseModel):
    order_date: Optional[date] = None
    product_id: int
    quantity: int
    comments: Optional[str] = None

@app.post("/order-management/orders", response_model=OrderResponse, status_code=status.HTTP_201_CREATED)
def create_order(
    order: OrderCreateRequest,
    db: Session = Depends(get_db),
    current_user: User = Depends(get_current_user)
):
    product_service = ProductService(db)
    product = product_service.get_product_by_id(order.product_id)
    if not product:
        raise HTTPException(status_code=status.HTTP_404_NOT_FOUND, detail=f"Product not found with id: {order.product_id}")
    order_service = OrderService(db)
    new_order = order_service.create_order(
        user_id=current_user.id,
        product_id=order.product_id,
        quantity=order.quantity,
        comments=order.comments,
        order_date=order.order_date
    )
    return OrderResponse(
        id=new_order.id,
        order_date=new_order.order_date,
        user=UserResponse(
            id=current_user.id,
            username=current_user.username,
            enabled=bool(current_user.enabled),
            roles=[r.role for r in current_user.roles] if current_user.roles else []
        ),
        product=ProductResponse(
            id=product.id,
            name=product.name,
            description=product.description,
            price=product.price,
            created_at=product.created_at
        ),
        quantity=new_order.quantity,
        comments=new_order.comments,
        created_at=new_order.created_at,
        updated_at=new_order.updated_at
    )

@app.get("/order-management/orders", response_model=List[OrderResponse])
def get_all_orders(db: Session = Depends(get_db), current_user: User = Depends(get_current_user)):
    order_service = OrderService(db)
    orders = order_service.get_all_orders()
    return [OrderResponse(
        id=o.id,
        order_date=o.order_date,
        user=UserResponse(
            id=o.user.id,
            username=o.user.username,
            enabled=bool(o.user.enabled),
            roles=[r.role for r in o.user.roles] if o.user.roles else []
        ),
        product=ProductResponse(
            id=o.product.id,
            name=o.product.name,
            description=o.product.description,
            price=o.product.price,
            created_at=o.product.created_at
        ),
        quantity=o.quantity,
        comments=o.comments,
        created_at=o.created_at,
        updated_at=o.updated_at
    ) for o in orders]

@app.get("/order-management/products", response_model=List[ProductResponse])
def get_all_products(db: Session = Depends(get_db), current_user: User = Depends(get_current_user)):
    product_service = ProductService(db)
    products = product_service.get_all_products()
    return [ProductResponse(
        id=p.id,
        name=p.name,
        description=p.description,
        price=p.price,
        created_at=p.created_at
    ) for p in products]
