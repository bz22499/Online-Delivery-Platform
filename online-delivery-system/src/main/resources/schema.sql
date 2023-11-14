-- noinspection SqlNoDataSourceInspectionForFile

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
    role VARCHAR(50) NOT NULL
);

-- Addresses Table
CREATE TABLE addresses (
    id INT PRIMARY KEY,
    userId INT NOT NULL REFERENCES users(id),
    street VARCHAR(255) NOT NULL,
    city VARCHAR(255) NOT NULL,
    postcode VARCHAR(50) NOT NULL,
    country VARCHAR(50) NOT NULL
);
