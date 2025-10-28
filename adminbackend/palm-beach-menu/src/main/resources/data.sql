-- Insert categories
INSERT IGNORE INTO categories (id, name, created_at, updated_at) VALUES
(1, 'Appetizers', NOW(), NOW()),
(2, 'Main Courses', NOW(), NOW()),
(3, 'Desserts', NOW(), NOW()),
(4, 'Beverages', NOW(), NOW()),
(5, 'Salads', NOW(), NOW()),
(6, 'Seafood Specialties', NOW(), NOW());

-- Insert sample menu items with proper images
INSERT IGNORE INTO menu_items (name, description, price, image_url, available, deleted, category_id, created_at, updated_at) VALUES
('Coconut Shrimp', 'Crispy shrimp with tropical coconut coating, served with sweet chili sauce', 14.99, 'https://images.unsplash.com/photo-1565958011703-44f9829ba187?w=400&h=300&fit=crop', TRUE, FALSE, 1, NOW(), NOW()),
('Grilled Mahi Mahi', 'Fresh mahi mahi grilled to perfection with lemon butter sauce', 24.99, 'https://images.unsplash.com/photo-1565299624946-b28f40a0ca4b?w=400&h=300&fit=crop', TRUE, FALSE, 2, NOW(), NOW()),
('Key Lime Pie', 'Classic Florida key lime pie with graham cracker crust', 8.99, 'https://images.unsplash.com/photo-1563729784474-d77dbb933a9e?w=400&h=300&fit=crop', TRUE, FALSE, 3, NOW(), NOW()),
('Tropical Sunrise', 'Fresh orange juice with grenadine and pineapple garnish', 6.99, 'https://images.unsplash.com/photo-1560518883-ce09059eeffa?w=400&h=300&fit=crop', TRUE, FALSE, 4, NOW(), NOW()),
('Beachside Caesar Salad', 'Fresh romaine with parmesan and classic Caesar dressing', 12.99, 'https://images.unsplash.com/photo-1540420773420-3366772f4999?w=400&h=300&fit=crop', TRUE, FALSE, 5, NOW(), NOW()),
('Lobster Thermidor', 'Premium lobster baked with creamy mushroom sauce', 32.99, 'https://images.unsplash.com/photo-1563379091339-03246963d8df?w=400&h=300&fit=crop', TRUE, FALSE, 6, NOW(), NOW()),
('Chocolate Cake', 'Rich and decadent dessert featuring a tender, moist crumb and a deep cocoa flavor', 8.5, 'https://static.toiimg.com/thumb/53096885.cms?imgsize=1572013&width=800&height=800', True, FALSE, 3, NOW(), NOW());