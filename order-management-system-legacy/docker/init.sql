-- Database initialization script
-- This script will be executed when the MySQL container starts for the first time

USE order_management_system_db;

-- Create tables if they don't exist (Spring Boot will handle this with JPA)
-- This script can be used for initial data seeding

-- Optional: Insert default data
-- INSERT INTO product (name, price, description) VALUES 
-- ('Sample Product 1', 29.99, 'This is a sample product'),
-- ('Sample Product 2', 49.99, 'This is another sample product');

-- Grant additional permissions if needed
GRANT ALL PRIVILEGES ON order_management_system_db.* TO 'appuser'@'%';
FLUSH PRIVILEGES;
