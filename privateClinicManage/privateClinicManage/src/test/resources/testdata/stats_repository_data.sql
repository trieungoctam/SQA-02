-- CREATE TABLE IF NOT EXISTS user (
--   id INT PRIMARY KEY,
--   name VARCHAR(255),
--   email VARCHAR(255)
-- );

-- CREATE TABLE IF NOT EXISTS payment_detail (
--   id INT PRIMARY KEY,
--   amount BIGINT,
--   created_date DATE
-- );

-- Thêm các bảng khác tương tự...

INSERT INTO user_ (id, avatar, password, email, name, gender, birthday, phone, address)
VALUES (1, 'avatar.png', '123456', 'a@example.com', 'Nguyen Van A', 'male', '2000-01-01', '0123456789', 'Hanoi');
INSERT INTO payment_detail (id, amount, created_date) VALUES (1, 100000, '2024-06-01');
