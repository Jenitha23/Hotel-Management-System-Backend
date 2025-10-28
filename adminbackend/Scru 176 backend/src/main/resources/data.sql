-- src/main/resources/data.sql

-- Customers (unique key = phone)
INSERT INTO customers (full_name, phone, email)
    VALUES ('Nimal Perera', '0771234567', 'nimal@example.com')
        AS new
ON DUPLICATE KEY UPDATE
                     full_name = new.full_name,
                     email     = new.email;

-- Rooms (unique key = room_number)
INSERT INTO rooms (room_number, type, daily_rate)
    VALUES ('101', 'DELUXE', 12000.00)
        AS new
ON DUPLICATE KEY UPDATE
                     type       = new.type,
                     daily_rate = new.daily_rate;
