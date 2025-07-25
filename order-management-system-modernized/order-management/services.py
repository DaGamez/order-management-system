from sqlalchemy.orm import Session
from models import Order, Product, User
from datetime import date
from typing import List, Optional

class OrderService:
    def __init__(self, db: Session):
        self.db = db

    def get_all_orders(self) -> List[Order]:
        return self.db.query(Order).all()

    def get_orders_by_user(self, user_id: int) -> List[Order]:
        return self.db.query(Order).filter(Order.user_id == user_id).order_by(Order.created_at.desc()).all()

    def get_order_by_id(self, order_id: int) -> Optional[Order]:
        return self.db.query(Order).filter(Order.id == order_id).first()

    def create_order(self, user_id: int, product_id: int, quantity: int, comments: Optional[str], order_date: Optional[date] = None) -> Order:
        order = Order(
            user_id=user_id,
            product_id=product_id,
            quantity=quantity,
            comments=comments,
            order_date=order_date or date.today()
        )
        self.db.add(order)
        self.db.commit()
        self.db.refresh(order)
        return order

    def update_order(self, order_id: int, quantity: int, comments: Optional[str]) -> Optional[Order]:
        order = self.get_order_by_id(order_id)
        if order:
            order.quantity = quantity
            order.comments = comments
            self.db.commit()
            self.db.refresh(order)
        return order

    def delete_order(self, order_id: int) -> bool:
        order = self.get_order_by_id(order_id)
        if order:
            self.db.delete(order)
            self.db.commit()
            return True
        return False

class ProductService:
    def __init__(self, db: Session):
        self.db = db

    def get_all_products(self) -> List[Product]:
        return self.db.query(Product).all()

    def get_product_by_id(self, product_id: int) -> Optional[Product]:
        return self.db.query(Product).filter(Product.id == product_id).first()
