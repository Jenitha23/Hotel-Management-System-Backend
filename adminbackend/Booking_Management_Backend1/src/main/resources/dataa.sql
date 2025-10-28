-- Insert sample customers
INSERT INTO customers (name, email, phone) VALUES
                                               ('John Doe', 'john.doe@example.com', '123-456-7890'),
                                               ('Jane Smith', 'jane.smith@example.com', '098-765-4321'),
                                               ('Robert Johnson', 'robert.johnson@example.com', '555-123-4567');

-- Insert sample rooms
INSERT INTO rooms (room_number, type, price, available) VALUES
                                                            ('101', 'Standard', 100.00, true),
                                                            ('102', 'Standard', 100.00, true),
                                                            ('201', 'Deluxe', 150.00, true),
                                                            ('202', 'Deluxe', 150.00, true),
                                                            ('301', 'Suite', 250.00, true);