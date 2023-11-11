-- ENUM Types
CREATE TYPE role_type AS ENUM ('customer', 'vendor', 'delivery');
CREATE TYPE payment_type AS ENUM ('credit_card', 'debit_card');
CREATE TYPE payment_status AS ENUM ('processed', 'refunded');
CREATE TYPE order_status AS ENUM ('received', 'collection', 'delivery', 'delivered');

-- Users Table
CREATE TABLE users (
    id INT PRIMARY KEY,
    first_name VARCHAR(255) NOT NULL,
    last_name VARCHAR(255) NOT NULL,
    email VARCHAR(255) UNIQUE NOT NULL,
    password VARCHAR(255) NOT NULL,
    address_id INT NOT NULL,
    role role_type NOT NULL,
    created_at TIMESTAMP NOT NULL DEFAULT NOW(),
    updated_at TIMESTAMP NOT NULL DEFAULT NOW()
);

-- Addresses Table
CREATE TABLE addresses (
    id UUID PRIMARY KEY,
    street VARCHAR(255) NOT NULL,
    city VARCHAR(255) NOT NULL
);

-- Payment Methods Table
CREATE TABLE payment_methods (
     id UUID PRIMARY KEY,
     user_id UUID NOT NULL REFERENCES users(id),
     type payment_type NOT NULL,
     provider VARCHAR(50) NOT NULL,
     card_number VARCHAR(255) NOT NULL,
     card_holder VARCHAR(255) NOT NULL,
     expiration_date DATE NOT NULL,
     cvv VARCHAR(4) NOT NULL
);

-- Delivery Person Table
CREATE TABLE delivery_person (
     id UUID PRIMARY KEY,
     user_id UUID NOT NULL REFERENCES users(id),
     rating FLOAT NOT NULL
);

-- Vendor Table
CREATE TABLE vendor (
    id UUID PRIMARY KEY,
    user_id UUID NOT NULL REFERENCES users(id),
    address_id UUID NOT NULL REFERENCES addresses(id),
    description VARCHAR(255) NOT NULL,
    rating FLOAT NOT NULL
);

-- Menu Items Table
CREATE TABLE menu_items (
    id UUID PRIMARY KEY,
    vendor_id UUID NOT NULL REFERENCES vendor(id),
    name VARCHAR(255) NOT NULL,
    description VARCHAR(255) NOT NULL,
    price FLOAT NOT NULL
);

-- Orders Table
CREATE TABLE orders (
    id UUID PRIMARY KEY,
    customer_id UUID NOT NULL REFERENCES users(id),
    delivery_person_id UUID NOT NULL REFERENCES delivery_person(id),
    delivery_address_id UUID NOT NULL REFERENCES addresses(id),
    vendor_address_id UUID NOT NULL REFERENCES addresses(id),
    total_price FLOAT NOT NULL,
    status order_status NOT NULL,
    created_at TIMESTAMP NOT NULL DEFAULT NOW()
);

-- Transactions Table
CREATE TABLE transactions (
    id UUID PRIMARY KEY,
    order_id UUID NOT NULL REFERENCES orders(id),
    payment_method_id UUID NOT NULL REFERENCES payment_methods(id),
    amount FLOAT NOT NULL,
    status payment_status NOT NULL,
    created_at TIMESTAMP NOT NULL DEFAULT NOW()
);

-- Order Items Table
CREATE TABLE order_items (
    id UUID PRIMARY KEY,
    order_id UUID NOT NULL REFERENCES orders(id),
    menu_item_id UUID NOT NULL REFERENCES menu_items(id),
    quantity INT NOT NULL
);
