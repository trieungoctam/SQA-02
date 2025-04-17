CREATE TABLE IF NOT EXISTS user_ (
  id INT PRIMARY KEY,
  avatar VARCHAR(255) NOT NULL,
  password VARCHAR(255) NOT NULL,
  email VARCHAR(255) NOT NULL,
  name VARCHAR(255) NOT NULL,
  gender VARCHAR(20) NOT NULL,
  birthday DATE NOT NULL,
  phone VARCHAR(20) NOT NULL,
  address VARCHAR(255) NOT NULL
);
CREATE TABLE IF NOT EXISTS payment_detail (
  id INT PRIMARY KEY,
  amount BIGINT,
  created_date DATE
);
-- Thêm các bảng khác nếu cần
