-- Drop the database if it exists and create a new one
DROP DATABASE IF EXISTS hotel_db;
CREATE DATABASE hotel_db;

-- Use the database
USE hotel_db;

-- Create Customer table
CREATE TABLE customer (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    name VARCHAR(100) NOT NULL,
    email VARCHAR(100) NOT NULL UNIQUE,
    phone VARCHAR(20)
);

-- Create Room table
CREATE TABLE room (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    room_number VARCHAR(10) NOT NULL UNIQUE,
    room_type VARCHAR(50) NOT NULL,
    price_per_night DECIMAL(10,2) NOT NULL,
    available BOOLEAN DEFAULT TRUE
);

-- Create Booking table
CREATE TABLE booking (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    customer_id BIGINT NOT NULL,
    room_id BIGINT NOT NULL,
    check_in_date DATE NOT NULL,
    check_out_date DATE NOT NULL,
    number_of_guests INT NOT NULL,
    status VARCHAR(20) NOT NULL,
    FOREIGN KEY (customer_id) REFERENCES customer(id),
    FOREIGN KEY (room_id) REFERENCES room(id)
);

-- Insert sample data
INSERT INTO customer (name, email, phone) VALUES
('John Doe', 'john@example.com', '+1234567890'),
('Jane Smith', 'jane@example.com', '+0987654321');

INSERT INTO room (room_number, room_type, price_per_night, available) VALUES
('101', 'STANDARD', 100.00, true),
('102', 'DELUXE', 150.00, true),
('201', 'SUITE', 250.00, true),
('202', 'STANDARD', 100.00, true);