CREATE TABLE products (
                          id BIGSERIAL PRIMARY KEY,
                          name VARCHAR(255) NOT NULL,
                          price DECIMAL(10, 2) NOT NULL,
                          stock INTEGER NOT NULL CHECK (stock >= 0)
);

INSERT INTO products (name, price, stock) VALUES ('iPhone 15 Pro', 999.99, 100);
INSERT INTO products (name, price, stock) VALUES ('Samsung S24', 899.50, 50);
INSERT INTO products (name, price, stock) VALUES ('PlayStation 5', 499.00, 10);