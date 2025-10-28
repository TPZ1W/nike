-- Insert users with simplified format
INSERT INTO users (full_name, email, password_hash, role, is_active, created_at, updated_at, avatar_url)
VALUES ('DangKhoa', 'dangkhoa@example.com',
        '$2a$12$7d5RDRcYVCxxINajw.n9HOwEaIe5dyBtGGbIUfaQVujFy0IuR7Rea',
        'GUEST', TRUE, NOW(), NOW(), 'uploads/avatars/defaultAvt.jpg') ON CONFLICT DO NOTHING;

INSERT INTO users (full_name, email, password_hash, role, is_active, created_at, updated_at, avatar_url)
VALUES ('TuanKiet', 'tuankiet@example.com',
        '$2a$12$7d5RDRcYVCxxINajw.n9HOwEaIe5dyBtGGbIUfaQVujFy0IuR7Rea',
        'MEMBER', TRUE, NOW(), NOW(), 'uploads/avatars/defaultAvt.jpg') ON CONFLICT DO NOTHING;

-- Add more users following the same pattern
INSERT INTO users (full_name, email, password_hash, role, is_active, created_at, updated_at, avatar_url)
VALUES ('Admin User', 'admin@example.com',
        '$2a$12$7d5RDRcYVCxxINajw.n9HOwEaIe5dyBtGGbIUfaQVujFy0IuR7Rea',
        'ADMIN', TRUE, NOW(), NOW(), 'uploads/avatars/defaultAvt.jpg') ON CONFLICT DO NOTHING;

INSERT INTO users (full_name, email, password_hash, role, is_active, created_at, updated_at, avatar_url)
VALUES ('Root User', 'root@example.com',
        '$2a$12$7d5RDRcYVCxxINajw.n9HOwEaIe5dyBtGGbIUfaQVujFy0IuR7Rea',
        'ROOT', TRUE, NOW(), NOW(), 'uploads/avatars/defaultAvt.jpg') ON CONFLICT DO NOTHING;

INSERT INTO users (full_name, email, password_hash, role, is_active, created_at, updated_at, avatar_url)
VALUES ('Regular User', 'user@example.com',
        '$2a$12$7d5RDRcYVCxxINajw.n9HOwEaIe5dyBtGGbIUfaQVujFy0IuR7Rea',
        'GUEST', TRUE, NOW(), NOW(), 'uploads/avatars/defaultAvt.jpg') ON CONFLICT DO NOTHING;


-- Thêm dữ liệu mẫu cho bảng category
INSERT INTO category (is_delete, created_at, updated_at, description, name)
SELECT false, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP,
       'Nam.', 'Nam'
    WHERE NOT EXISTS (SELECT 1 FROM category WHERE name = 'Nam');

INSERT INTO category (is_delete, created_at, updated_at, description, name)
SELECT false, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP,
       'Nữ.', 'Nữ'
    WHERE NOT EXISTS (SELECT 1 FROM category WHERE name = 'Nữ');

INSERT INTO category (is_delete, created_at, updated_at, description, name)
SELECT false, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP,
       'Trẻ em.', 'Trẻ em'
    WHERE NOT EXISTS (SELECT 1 FROM category WHERE name = 'Nike Lifestyle');

INSERT INTO category (is_delete, created_at, updated_at, description, name)
SELECT false, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP,
       'Khác.', 'Khác'
    WHERE NOT EXISTS (SELECT 1 FROM category WHERE name = 'Nike Football');

-- Thêm dữ liệu mẫu cho bảng product
INSERT INTO product (is_delete, created_at, updated_at, name, slug, sub_title, description, price, images, category_id)
SELECT false, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP,
       'Nike Air Force 1', 'nike-air-force-1', 'Classic White Sneakers', 
       'Iconic basketball shoe with timeless style and maximum comfort.', 
       2500000.0,
       ARRAY['nike-air-force-1-white.jpg', 'nike-air-force-1-white-2.jpg'], 
       (SELECT id FROM category WHERE name = 'Nike Basketball' LIMIT 1)
    WHERE NOT EXISTS (SELECT 1 FROM product WHERE name = 'Nike Air Force 1');

INSERT INTO product (is_delete, created_at, updated_at, name, slug, sub_title, description, price, images, category_id)
SELECT false, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP,
       'Nike Air Max 90', 'nike-air-max-90', 'Retro Running Shoes', 
       'The classic Air Max 90 with visible air cushioning and retro style.', 
       3200000.0,
       ARRAY['nike-air-max-90-black.jpg', 'nike-air-max-90-black-2.jpg'], 
       (SELECT id FROM category WHERE name = 'Nike Running' LIMIT 1)
    WHERE NOT EXISTS (SELECT 1 FROM product WHERE name = 'Nike Air Max 90');

INSERT INTO product (is_delete, created_at, updated_at, name, slug, sub_title, description, price, images, category_id)
SELECT false, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP,
       'Nike Dunk Low', 'nike-dunk-low', 'Lifestyle Sneakers', 
       'Versatile basketball-inspired shoe perfect for everyday wear.', 
       2800000.0,
       ARRAY['nike-dunk-low-white-navy.jpg', 'nike-dunk-low-white-navy-2.jpg'], 
       (SELECT id FROM category WHERE name = 'Nike Lifestyle' LIMIT 1)
    WHERE NOT EXISTS (SELECT 1 FROM product WHERE name = 'Nike Dunk Low');

INSERT INTO product (is_delete, created_at, updated_at, name, slug, sub_title, description, price, images, category_id)
SELECT false, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP,
       'Air Jordan 1 High', 'air-jordan-1-high', 'Legendary Basketball Shoes', 
       'The shoe that started it all. Classic Jordan 1 with premium materials.', 
       4500000.0,
       ARRAY['jordan-1-chicago.jpg', 'jordan-1-chicago-2.jpg'], 
       (SELECT id FROM category WHERE name = 'Air Jordan' LIMIT 1)
    WHERE NOT EXISTS (SELECT 1 FROM product WHERE name = 'Air Jordan 1 High');

INSERT INTO product (is_delete, created_at, updated_at, name, slug, sub_title, description, price, images, category_id)
SELECT false, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP,
       'Nike React Infinity Run', 'nike-react-infinity-run', 'Professional Running Shoes', 
       'Designed to keep you running with maximum cushioning and support.', 
       3800000.0,
       ARRAY['nike-react-infinity-blue.jpg', 'nike-react-infinity-blue-2.jpg'], 
       (SELECT id FROM category WHERE name = 'Nike Running' LIMIT 1)
    WHERE NOT EXISTS (SELECT 1 FROM product WHERE name = 'Nike React Infinity Run');

-- Thêm dữ liệu mẫu cho bảng orders (với các trạng thái theo yêu cầu)
INSERT INTO orders (created_at, updated_at, user_id, total_amount, quantity, phone, status, shipping_address, payment_method)
SELECT CURRENT_TIMESTAMP - INTERVAL '5 days', CURRENT_TIMESTAMP - INTERVAL '5 days',
       (SELECT id FROM users WHERE email = 'dangkhoa@example.com' LIMIT 1),
       5000000.0, 2, '0901234567', 'pending', '123 Nguyen Van A, District 1, Ho Chi Minh City', 'COD'
    WHERE NOT EXISTS (SELECT 1 FROM orders WHERE user_id = (SELECT id FROM users WHERE email = 'dangkhoa@example.com' LIMIT 1) AND total_amount = 5000000.0);

INSERT INTO orders (created_at, updated_at, user_id, total_amount, quantity, phone, status, shipping_address, payment_method)
SELECT CURRENT_TIMESTAMP - INTERVAL '4 days', CURRENT_TIMESTAMP - INTERVAL '3 days',
       (SELECT id FROM users WHERE email = 'tuankiet@example.com' LIMIT 1),
       3200000.0, 1, '0901234568', 'confirmed', '456 Le Van B, District 3, Ho Chi Minh City', 'BANK_TRANSFER'
    WHERE NOT EXISTS (SELECT 1 FROM orders WHERE user_id = (SELECT id FROM users WHERE email = 'tuankiet@example.com' LIMIT 1) AND total_amount = 3200000.0);

INSERT INTO orders (created_at, updated_at, user_id, total_amount, quantity, phone, status, shipping_address, payment_method)
SELECT CURRENT_TIMESTAMP - INTERVAL '3 days', CURRENT_TIMESTAMP - INTERVAL '2 days',
       (SELECT id FROM users WHERE email = 'user@example.com' LIMIT 1),
       2800000.0, 1, '0901234569', 'shipping', '789 Tran Van C, District 7, Ho Chi Minh City', 'CREDIT_CARD'
    WHERE NOT EXISTS (SELECT 1 FROM orders WHERE user_id = (SELECT id FROM users WHERE email = 'user@example.com' LIMIT 1) AND total_amount = 2800000.0);

INSERT INTO orders (created_at, updated_at, user_id, total_amount, quantity, phone, status, shipping_address, payment_method)
SELECT CURRENT_TIMESTAMP - INTERVAL '2 days', CURRENT_TIMESTAMP - INTERVAL '1 day',
       (SELECT id FROM users WHERE email = 'dangkhoa@example.com' LIMIT 1),
       4500000.0, 1, '0901234567', 'completed', '123 Nguyen Van A, District 1, Ho Chi Minh City', 'COD'
    WHERE NOT EXISTS (SELECT 1 FROM orders WHERE user_id = (SELECT id FROM users WHERE email = 'dangkhoa@example.com' LIMIT 1) AND total_amount = 4500000.0);

INSERT INTO orders (created_at, updated_at, user_id, total_amount, quantity, phone, status, shipping_address, payment_method)
SELECT CURRENT_TIMESTAMP - INTERVAL '6 days', CURRENT_TIMESTAMP - INTERVAL '6 days',
       (SELECT id FROM users WHERE email = 'tuankiet@example.com' LIMIT 1),
       2500000.0, 1, '0901234568', 'canceled', '456 Le Van B, District 3, Ho Chi Minh City', 'BANK_TRANSFER'
    WHERE NOT EXISTS (SELECT 1 FROM orders WHERE user_id = (SELECT id FROM users WHERE email = 'tuankiet@example.com' LIMIT 1) AND total_amount = 2500000.0);

INSERT INTO orders (created_at, updated_at, user_id, total_amount, quantity, phone, status, shipping_address, payment_method)
SELECT CURRENT_TIMESTAMP - INTERVAL '1 day', CURRENT_TIMESTAMP,
       (SELECT id FROM users WHERE email = 'user@example.com' LIMIT 1),
       3800000.0, 1, '0901234569', 'completed', '789 Tran Van C, District 7, Ho Chi Minh City', 'CREDIT_CARD'
    WHERE NOT EXISTS (SELECT 1 FROM orders WHERE user_id = (SELECT id FROM users WHERE email = 'user@example.com' LIMIT 1) AND total_amount = 3800000.0);

-- Thêm dữ liệu mẫu cho bảng order_item
INSERT INTO order_item (created_at, updated_at, order_id, product_id, quantity, product_price, total_price)
SELECT 
    CURRENT_TIMESTAMP, CURRENT_TIMESTAMP,
    (SELECT id FROM orders WHERE total_amount = 5000000.0 AND status = 'pending' LIMIT 1),
    (SELECT id FROM product WHERE name = 'Nike Air Force 1' LIMIT 1),
    2, 2500000.0, 5000000.0
WHERE NOT EXISTS (
    SELECT 1 FROM order_item 
    WHERE order_id = (SELECT id FROM orders WHERE total_amount = 5000000.0 AND status = 'pending' LIMIT 1)
    AND product_id = (SELECT id FROM product WHERE name = 'Nike Air Force 1' LIMIT 1)
);

INSERT INTO order_item (created_at, updated_at, order_id, product_id, quantity, product_price, total_price)
SELECT 
    CURRENT_TIMESTAMP, CURRENT_TIMESTAMP,
    (SELECT id FROM orders WHERE total_amount = 3200000.0 AND status = 'confirmed' LIMIT 1),
    (SELECT id FROM product WHERE name = 'Nike Air Max 90' LIMIT 1),
    1, 3200000.0, 3200000.0
WHERE NOT EXISTS (
    SELECT 1 FROM order_item 
    WHERE order_id = (SELECT id FROM orders WHERE total_amount = 3200000.0 AND status = 'confirmed' LIMIT 1)
    AND product_id = (SELECT id FROM product WHERE name = 'Nike Air Max 90' LIMIT 1)
);

INSERT INTO order_item (created_at, updated_at, order_id, product_id, quantity, product_price, total_price)
SELECT 
    CURRENT_TIMESTAMP, CURRENT_TIMESTAMP,
    (SELECT id FROM orders WHERE total_amount = 2800000.0 AND status = 'shipping' LIMIT 1),
    (SELECT id FROM product WHERE name = 'Nike Dunk Low' LIMIT 1),
    1, 2800000.0, 2800000.0
WHERE NOT EXISTS (
    SELECT 1 FROM order_item 
    WHERE order_id = (SELECT id FROM orders WHERE total_amount = 2800000.0 AND status = 'shipping' LIMIT 1)
    AND product_id = (SELECT id FROM product WHERE name = 'Nike Dunk Low' LIMIT 1)
);

INSERT INTO order_item (created_at, updated_at, order_id, product_id, quantity, product_price, total_price)
SELECT 
    CURRENT_TIMESTAMP, CURRENT_TIMESTAMP,
    (SELECT id FROM orders WHERE total_amount = 4500000.0 AND status = 'completed' LIMIT 1),
    (SELECT id FROM product WHERE name = 'Air Jordan 1 High' LIMIT 1),
    1, 4500000.0, 4500000.0
WHERE NOT EXISTS (
    SELECT 1 FROM order_item 
    WHERE order_id = (SELECT id FROM orders WHERE total_amount = 4500000.0 AND status = 'completed' LIMIT 1)
    AND product_id = (SELECT id FROM product WHERE name = 'Air Jordan 1 High' LIMIT 1)
);

INSERT INTO order_item (created_at, updated_at, order_id, product_id, quantity, product_price, total_price)
SELECT 
    CURRENT_TIMESTAMP, CURRENT_TIMESTAMP,
    (SELECT id FROM orders WHERE total_amount = 2500000.0 AND status = 'canceled' LIMIT 1),
    (SELECT id FROM product WHERE name = 'Nike Air Force 1' LIMIT 1),
    1, 2500000.0, 2500000.0
WHERE NOT EXISTS (
    SELECT 1 FROM order_item 
    WHERE order_id = (SELECT id FROM orders WHERE total_amount = 2500000.0 AND status = 'canceled' LIMIT 1)
    AND product_id = (SELECT id FROM product WHERE name = 'Nike Air Force 1' LIMIT 1)
);

INSERT INTO order_item (created_at, updated_at, order_id, product_id, quantity, product_price, total_price)
SELECT 
    CURRENT_TIMESTAMP, CURRENT_TIMESTAMP,
    (SELECT id FROM orders WHERE total_amount = 3800000.0 AND status = 'completed' LIMIT 1),
    (SELECT id FROM product WHERE name = 'Nike React Infinity Run' LIMIT 1),
    1, 3800000.0, 3800000.0
WHERE NOT EXISTS (
    SELECT 1 FROM order_item 
    WHERE order_id = (SELECT id FROM orders WHERE total_amount = 3800000.0 AND status = 'completed' LIMIT 1)
    AND product_id = (SELECT id FROM product WHERE name = 'Nike React Infinity Run' LIMIT 1)
);

-- Thêm thêm một số đơn hàng để dashboard phong phú hơn
INSERT INTO orders (created_at, updated_at, user_id, total_amount, quantity, phone, status, shipping_address, payment_method)
SELECT CURRENT_TIMESTAMP - INTERVAL '7 days', CURRENT_TIMESTAMP - INTERVAL '6 days',
       (SELECT id FROM users WHERE email = 'admin@example.com' LIMIT 1),
       6400000.0, 2, '0909876543', 'completed', '100 Admin Street, District 1, Ho Chi Minh City', 'BANK_TRANSFER'
    WHERE NOT EXISTS (SELECT 1 FROM orders WHERE user_id = (SELECT id FROM users WHERE email = 'admin@example.com' LIMIT 1) AND total_amount = 6400000.0);

INSERT INTO orders (created_at, updated_at, user_id, total_amount, quantity, phone, status, shipping_address, payment_method)
SELECT CURRENT_TIMESTAMP - INTERVAL '3 hours', CURRENT_TIMESTAMP - INTERVAL '3 hours',
       (SELECT id FROM users WHERE email = 'root@example.com' LIMIT 1),
       2800000.0, 1, '0908765432', 'confirmed', '200 Root Avenue, District 2, Ho Chi Minh City', 'CREDIT_CARD'
    WHERE NOT EXISTS (SELECT 1 FROM orders WHERE user_id = (SELECT id FROM users WHERE email = 'root@example.com' LIMIT 1) AND total_amount = 2800000.0);

INSERT INTO orders (created_at, updated_at, user_id, total_amount, quantity, phone, status, shipping_address, payment_method)
SELECT CURRENT_TIMESTAMP - INTERVAL '1 hour', CURRENT_TIMESTAMP - INTERVAL '1 hour',
       (SELECT id FROM users WHERE email = 'dangkhoa@example.com' LIMIT 1),
       3200000.0, 1, '0901234567', 'pending', '123 Nguyen Van A, District 1, Ho Chi Minh City', 'COD'
    WHERE NOT EXISTS (SELECT 1 FROM orders WHERE user_id = (SELECT id FROM users WHERE email = 'dangkhoa@example.com' LIMIT 1) AND total_amount = 3200000.0);

-- Thêm order items cho các đơn hàng mới
INSERT INTO order_item (created_at, updated_at, order_id, product_id, quantity, product_price, total_price)
SELECT 
    CURRENT_TIMESTAMP, CURRENT_TIMESTAMP,
    (SELECT id FROM orders WHERE total_amount = 6400000.0 AND status = 'completed' LIMIT 1),
    (SELECT id FROM product WHERE name = 'Nike Air Max 90' LIMIT 1),
    2, 3200000.0, 6400000.0
WHERE NOT EXISTS (
    SELECT 1 FROM order_item 
    WHERE order_id = (SELECT id FROM orders WHERE total_amount = 6400000.0 AND status = 'completed' LIMIT 1)
    AND product_id = (SELECT id FROM product WHERE name = 'Nike Air Max 90' LIMIT 1)
);

INSERT INTO order_item (created_at, updated_at, order_id, product_id, quantity, product_price, total_price)
SELECT 
    CURRENT_TIMESTAMP, CURRENT_TIMESTAMP,
    (SELECT id FROM orders WHERE total_amount = 2800000.0 AND status = 'confirmed' AND user_id = (SELECT id FROM users WHERE email = 'root@example.com' LIMIT 1) LIMIT 1),
    (SELECT id FROM product WHERE name = 'Nike Dunk Low' LIMIT 1),
    1, 2800000.0, 2800000.0
WHERE NOT EXISTS (
    SELECT 1 FROM order_item 
    WHERE order_id = (SELECT id FROM orders WHERE total_amount = 2800000.0 AND status = 'confirmed' AND user_id = (SELECT id FROM users WHERE email = 'root@example.com' LIMIT 1) LIMIT 1)
    AND product_id = (SELECT id FROM product WHERE name = 'Nike Dunk Low' LIMIT 1)
);

INSERT INTO order_item (created_at, updated_at, order_id, product_id, quantity, product_price, total_price)
SELECT 
    CURRENT_TIMESTAMP, CURRENT_TIMESTAMP,
    (SELECT id FROM orders WHERE total_amount = 3200000.0 AND status = 'pending' AND user_id = (SELECT id FROM users WHERE email = 'dangkhoa@example.com' LIMIT 1) AND created_at > CURRENT_TIMESTAMP - INTERVAL '2 hours' LIMIT 1),
    (SELECT id FROM product WHERE name = 'Nike Air Max 90' LIMIT 1),
    1, 3200000.0, 3200000.0
WHERE NOT EXISTS (
    SELECT 1 FROM order_item 
    WHERE order_id = (SELECT id FROM orders WHERE total_amount = 3200000.0 AND status = 'pending' AND user_id = (SELECT id FROM users WHERE email = 'dangkhoa@example.com' LIMIT 1) AND created_at > CURRENT_TIMESTAMP - INTERVAL '2 hours' LIMIT 1)
    AND product_id = (SELECT id FROM product WHERE name = 'Nike Air Max 90' LIMIT 1)
);



