CREATE DATABASE privateclinicmanage;

USE privateclinicmanage;

INSERT INTO role (name)
VALUES
    ('ROLE_BENHNHAN'),
    ('ROLE_BACSI'),
    ('ROLE_YTA'),
    ('ROLE_TUVAN'),
    ('ROLE_ADMIN');

-- Unit Medicine Types (complete data)
INSERT INTO unit_medicine_type (unit_name) VALUES
                                               ('Viên'),
                                               ('Gói'),
                                               ('Ống'),
                                               ('Lọ'),
                                               ('Chai'),
                                               ('Hộp'),
                                               ('Vỉ'),
                                               ('Ampule'),
                                               ('Túi'),
                                               ('Gram');

-- Medicine Groups (complete data)
INSERT INTO medicine_group (group_name) VALUES
                                            ('Kháng sinh'),
                                            ('Giảm đau'),
                                            ('Hạ sốt'),
                                            ('Tiêu hóa'),
                                            ('Vitamin'),
                                            ('Chống viêm'),
                                            ('Huyết áp'),
                                            ('Tim mạch'),
                                            ('Tiểu đường'),
                                            ('Dị ứng');

-- Status Is Approved (complete data)
INSERT INTO status_is_approved (note, status) VALUES
                                                  ('', 'FOLLOWUP'),
                                                  ('', 'PAYMENTPHASE1'),
                                                  ('', 'PAYMENTPHASE2'),
                                                  ('', 'SUCCESS'),
                                                  ('', 'FAILED'),
                                                  ('', 'FINISHED'),
                                                  ('', 'CHECKING'),
                                                  ('', 'CANCELED');


-- Medicines (complete data with all relationships)
INSERT INTO medicine (name, description, price, default_per_day, is_actived, unit_medicine_type_id, medicine_group_id) VALUES
                                                                                                                           ('Paracetamol', 'Thuốc hạ sốt, giảm đau', 5000, 3, true, 1, 3),
                                                                                                                           ('Amoxicillin', 'Kháng sinh phổ rộng', 10000, 3, true, 1, 1),
                                                                                                                           ('Omeprazole', 'Điều trị viêm loét dạ dày', 8000, 1, true, 1, 4),
                                                                                                                           ('Vitamin C', 'Bổ sung vitamin C', 3000, 1, true, 1, 5),
                                                                                                                           ('Aspirin', 'Chống viêm, giảm đau', 6000, 2, true, 1, 2),
                                                                                                                           ('Cetirizine', 'Thuốc kháng histamine', 7000, 1, true, 1, 10),
                                                                                                                           ('Metformin', 'Điều trị tiểu đường', 9000, 2, true, 1, 9),
                                                                                                                           ('Losartan', 'Điều trị huyết áp cao', 12000, 1, true, 1, 7),
                                                                                                                           ('Ibuprofen', 'Giảm đau, hạ sốt', 5500, 3, true, 1, 2),
                                                                                                                           ('Atorvastatin', 'Giảm mỡ máu', 15000, 1, true, 1, 8),
                                                                                                                           ('Dexamethasone', 'Thuốc chống viêm', 11000, 2, true, 3, 6),
                                                                                                                           ('Diazepam', 'Thuốc an thần', 8500, 1, true, 1, 2),
                                                                                                                           ('Cefixime', 'Kháng sinh đường uống', 13000, 2, true, 1, 1),
                                                                                                                           ('Esomeprazole', 'Điều trị trào ngược dạ dày', 8000, 1, true, 5, 4),
                                                                                                                           ('Multivitamin', 'Bổ sung vitamin tổng hợp', 7500, 1, true, 1, 5),
                                                                                                                           ('Ciprofloxacin', 'Kháng sinh nhóm quinolone', 12000, 2, true, 1, 1),
                                                                                                                           ('Pantoprazole', 'Điều trị loét dạ dày', 9000, 1, true, 1, 4),
                                                                                                                           ('Insulin', 'Điều trị tiểu đường', 25000, 1, true, 3, 9),
                                                                                                                           ('Amlodipine', 'Điều trị cao huyết áp', 7000, 1, true, 1, 7),
                                                                                                                           ('Fexofenadine', 'Thuốc kháng histamine', 8000, 2, true, 1, 10);

-- Users (complete data for all roles)
INSERT INTO `user` (avatar, password, email, name, gender, birthday, phone, address, is_actived, role_id) VALUES
-- Patients (ROLE_BENHNHAN)
('https://res.cloudinary.com/diwxda8bi/image/upload/v1723123220/default-avatar-icon-of-social-media-user-vector_v0hotc.jpg',
 '$2a$10$oMaE/0OvYh5kSFw0HjWbkeB4XbQh5c/pDSesKacZfnJw8i6y5fuUa', 'patient1@example.com', 'Nguyễn Văn A', 'Nam', '1990-01-15', '0901234567', 'Hà Nội', true, 1),
('https://res.cloudinary.com/diwxda8bi/image/upload/v1723123220/default-avatar-icon-of-social-media-user-vector_v0hotc.jpg',
 '$2a$10$oMaE/0OvYh5kSFw0HjWbkeB4XbQh5c/pDSesKacZfnJw8i6y5fuUa', 'patient2@example.com', 'Trần Thị B', 'Nữ', '1985-05-20', '0912345678', 'Hồ Chí Minh', true, 1),
('https://res.cloudinary.com/diwxda8bi/image/upload/v1723123220/default-avatar-icon-of-social-media-user-vector_v0hotc.jpg',
 '$2a$10$oMaE/0OvYh5kSFw0HjWbkeB4XbQh5c/pDSesKacZfnJw8i6y5fuUa', 'patient3@example.com', 'Lê Hoàng C', 'Nam', '1992-09-10', '0923456789', 'Đà Nẵng', true, 1),
('https://res.cloudinary.com/diwxda8bi/image/upload/v1723123220/default-avatar-icon-of-social-media-user-vector_v0hotc.jpg',
 '$2a$10$oMaE/0OvYh5kSFw0HjWbkeB4XbQh5c/pDSesKacZfnJw8i6y5fuUa', 'patient4@example.com', 'Phạm Thị D', 'Nữ', '1988-12-25', '0934567890', 'Hải Phòng', true, 1),
('https://res.cloudinary.com/diwxda8bi/image/upload/v1723123220/default-avatar-icon-of-social-media-user-vector_v0hotc.jpg',
 '$2a$10$oMaE/0OvYh5kSFw0HjWbkeB4XbQh5c/pDSesKacZfnJw8i6y5fuUa', 'patient5@example.com', 'Hoàng Văn E', 'Nam', '1995-08-05', '0945678901', 'Cần Thơ', true, 1),
('https://res.cloudinary.com/diwxda8bi/image/upload/v1723123220/default-avatar-icon-of-social-media-user-vector_v0hotc.jpg',
 '$2a$10$oMaE/0OvYh5kSFw0HjWbkeB4XbQh5c/pDSesKacZfnJw8i6y5fuUa', 'patient6@example.com', 'Vũ Minh F', 'Nam', '1993-02-15', '0956789012', 'Hà Nội', true, 1),
('https://res.cloudinary.com/diwxda8bi/image/upload/v1723123220/default-avatar-icon-of-social-media-user-vector_v0hotc.jpg',
 '$2a$10$oMaE/0OvYh5kSFw0HjWbkeB4XbQh5c/pDSesKacZfnJw8i6y5fuUa', 'patient7@example.com', 'Đỗ Thu G', 'Nữ', '1987-06-22', '0967890123', 'Hồ Chí Minh', true, 1),
('https://res.cloudinary.com/diwxda8bi/image/upload/v1723123220/default-avatar-icon-of-social-media-user-vector_v0hotc.jpg',
 '$2a$10$oMaE/0OvYh5kSFw0HjWbkeB4XbQh5c/pDSesKacZfnJw8i6y5fuUa', 'patient8@example.com', 'Ngô Văn H', 'Nam', '1991-11-11', '0978901234', 'Đà Nẵng', true, 1),

-- Doctors (ROLE_BACSI)
('https://res.cloudinary.com/diwxda8bi/image/upload/v1723123220/default-avatar-icon-of-social-media-user-vector_v0hotc.jpg',
 '$2a$10$oMaE/0OvYh5kSFw0HjWbkeB4XbQh5c/pDSesKacZfnJw8i6y5fuUa', 'doctor1@example.com', 'Bác sĩ Nguyễn Văn X', 'Nam', '1980-03-10', '0989012345', 'Hà Nội', true, 2),
('https://res.cloudinary.com/diwxda8bi/image/upload/v1723123220/default-avatar-icon-of-social-media-user-vector_v0hotc.jpg',
 '$2a$10$oMaE/0OvYh5kSFw0HjWbkeB4XbQh5c/pDSesKacZfnJw8i6y5fuUa', 'doctor2@example.com', 'Bác sĩ Trần Thị Y', 'Nữ', '1982-06-15', '0990123456', 'Hồ Chí Minh', true, 2),
('https://res.cloudinary.com/diwxda8bi/image/upload/v1723123220/default-avatar-icon-of-social-media-user-vector_v0hotc.jpg',
 '$2a$10$oMaE/0OvYh5kSFw0HjWbkeB4XbQh5c/pDSesKacZfnJw8i6y5fuUa', 'doctor3@example.com', 'Bác sĩ Lê Hoàng Z', 'Nam', '1978-09-20', '0901234567', 'Đà Nẵng', true, 2),
('https://res.cloudinary.com/diwxda8bi/image/upload/v1723123220/default-avatar-icon-of-social-media-user-vector_v0hotc.jpg',
 '$2a$10$oMaE/0OvYh5kSFw0HjWbkeB4XbQh5c/pDSesKacZfnJw8i6y5fuUa', 'doctor4@example.com', 'Bác sĩ Phạm Minh K', 'Nam', '1981-04-25', '0912345678', 'Hải Phòng', true, 2),

-- Nurses (ROLE_YTA)
('https://res.cloudinary.com/diwxda8bi/image/upload/v1723123220/default-avatar-icon-of-social-media-user-vector_v0hotc.jpg',
 '$2a$10$oMaE/0OvYh5kSFw0HjWbkeB4XbQh5c/pDSesKacZfnJw8i6y5fuUa', 'nurse1@example.com', 'Y tá Phạm Thị M', 'Nữ', '1990-11-30', '0923456789', 'Hà Nội', true, 3),
('https://res.cloudinary.com/diwxda8bi/image/upload/v1723123220/default-avatar-icon-of-social-media-user-vector_v0hotc.jpg',
 '$2a$10$oMaE/0OvYh5kSFw0HjWbkeB4XbQh5c/pDSesKacZfnJw8i6y5fuUa', 'nurse2@example.com', 'Y tá Hoàng Văn N', 'Nam', '1992-02-18', '0934567890', 'Hồ Chí Minh', true, 3),
('https://res.cloudinary.com/diwxda8bi/image/upload/v1723123220/default-avatar-icon-of-social-media-user-vector_v0hotc.jpg',
 '$2a$10$oMaE/0OvYh5kSFw0HjWbkeB4XbQh5c/pDSesKacZfnJw8i6y5fuUa', 'nurse3@example.com', 'Y tá Trần Thị O', 'Nữ', '1993-03-20', '0945678901', 'Đà Nẵng', true, 3),

-- Consultants (ROLE_TUVAN)
('https://res.cloudinary.com/diwxda8bi/image/upload/v1723123220/default-avatar-icon-of-social-media-user-vector_v0hotc.jpg',
 '$2a$10$oMaE/0OvYh5kSFw0HjWbkeB4XbQh5c/pDSesKacZfnJw8i6y5fuUa', 'consultant1@example.com', 'Tư vấn viên Nguyễn Thị P', 'Nữ', '1988-07-22', '0956789012', 'Hà Nội', true, 4),
('https://res.cloudinary.com/diwxda8bi/image/upload/v1723123220/default-avatar-icon-of-social-media-user-vector_v0hotc.jpg',
 '$2a$10$oMaE/0OvYh5kSFw0HjWbkeB4XbQh5c/pDSesKacZfnJw8i6y5fuUa', 'consultant2@example.com', 'Tư vấn viên Trần Văn Q', 'Nam', '1985-04-15', '0967890123', 'Hồ Chí Minh', true, 4),
('https://res.cloudinary.com/diwxda8bi/image/upload/v1723123220/default-avatar-icon-of-social-media-user-vector_v0hotc.jpg',
 '$2a$10$oMaE/0OvYh5kSFw0HjWbkeB4XbQh5c/pDSesKacZfnJw8i6y5fuUa', 'consultant3@example.com', 'Tư vấn viên Lê Thị R', 'Nữ', '1986-08-25', '0978901234', 'Đà Nẵng', true, 4),

-- Admin
('https://res.cloudinary.com/diwxda8bi/image/upload/v1723123220/default-avatar-icon-of-social-media-user-vector_v0hotc.jpg',
 '$2a$10$oMaE/0OvYh5kSFw0HjWbkeB4XbQh5c/pDSesKacZfnJw8i6y5fuUa', 'admin@example.com', 'Admin', 'Nam', '1985-01-01', '0999999999', 'Hà Nội', true, 5);

-- Create wallets for all users
INSERT INTO wallet (balance, created_time, user_id) VALUES
                                                        (1000000, CURRENT_DATE, 1),
                                                        (1500000, CURRENT_DATE,2),
                                                        (800000, CURRENT_DATE,3),
                                                        (1200000,CURRENT_DATE, 4),
                                                        (900000, CURRENT_DATE,5),
                                                        (700000,CURRENT_DATE, 6),
                                                        (1100000,CURRENT_DATE, 7),
                                                        (850000, CURRENT_DATE,8),
                                                        (2000000, CURRENT_DATE,9),
                                                        (2000000,CURRENT_DATE, 10),
                                                        (2000000,CURRENT_DATE, 11),
                                                        (2000000, CURRENT_DATE,12),
                                                        (1000000,CURRENT_DATE, 13),
                                                        (1000000, CURRENT_DATE,14),
                                                        (1000000,CURRENT_DATE, 15),
                                                        (1000000,CURRENT_DATE, 16),
                                                        (1000000,CURRENT_DATE, 17),
                                                        (1000000,CURRENT_DATE, 18),
                                                        (5000000, CURRENT_DATE,19);

-- Schedules for next 30 days
INSERT INTO schedule (appointment_schedule, is_day_off, description)
SELECT
    DATE_ADD(CURRENT_DATE(), INTERVAL n DAY),
    CASE
        WHEN DAYOFWEEK(DATE_ADD(CURRENT_DATE(), INTERVAL n DAY)) IN (1, 7) THEN true -- Weekend is day off
        ELSE false
        END,
    CASE
        WHEN DAYOFWEEK(DATE_ADD(CURRENT_DATE(), INTERVAL n DAY)) IN (1, 7) THEN 'weekend day off'
        ELSE 'normal working day'
        END
FROM (
         SELECT 0 as n UNION SELECT 1 UNION SELECT 2 UNION SELECT 3 UNION SELECT 4 UNION
         SELECT 5 UNION SELECT 6 UNION SELECT 7 UNION SELECT 8 UNION SELECT 9 UNION
         SELECT 10 UNION SELECT 11 UNION SELECT 12 UNION SELECT 13 UNION SELECT 14 UNION
         SELECT 15 UNION SELECT 16 UNION SELECT 17 UNION SELECT 18 UNION SELECT 19 UNION
         SELECT 20 UNION SELECT 21 UNION SELECT 22 UNION SELECT 23 UNION SELECT 24 UNION
         SELECT 25 UNION SELECT 26 UNION SELECT 27 UNION SELECT 28 UNION SELECT 29
     ) numbers;

-- Add some past schedules
INSERT INTO schedule (appointment_schedule, is_day_off, description)
SELECT
    DATE_SUB(CURRENT_DATE(), INTERVAL n DAY),
    CASE
        WHEN DAYOFWEEK(DATE_SUB(CURRENT_DATE(), INTERVAL n DAY)) IN (1, 7) THEN true -- Weekend is day off
        ELSE false
        END,
    CASE
        WHEN DAYOFWEEK(DATE_SUB(CURRENT_DATE(), INTERVAL n DAY)) IN (1, 7) THEN 'weekend day off'
        ELSE 'normal working day'
        END
FROM (
         SELECT 1 as n UNION SELECT 2 UNION SELECT 3 UNION SELECT 4 UNION SELECT 5 UNION
         SELECT 6 UNION SELECT 7 UNION SELECT 8 UNION SELECT 9 UNION SELECT 10 UNION
         SELECT 11 UNION SELECT 12 UNION SELECT 13 UNION SELECT 14 UNION SELECT 15 UNION
         SELECT 16 UNION SELECT 17 UNION SELECT 18 UNION SELECT 19 UNION SELECT 20
     ) numbers;

-- Payment details with phase_type
INSERT INTO paymentdetail (phase_type, amount, created_date, description, order_id, partner_code, result_code)
VALUES
-- Phase 1 payments (registration)
('PHASE1', 100000, DATE_SUB(CURRENT_DATE(), INTERVAL 20 DAY), 'Thanh toán đăng ký khám bệnh', 'ORDER_P1_001', 'VNPAY', '00'),
('PHASE1', 100000, DATE_SUB(CURRENT_DATE(), INTERVAL 19 DAY), 'Thanh toán đăng ký khám bệnh', 'ORDER_P1_002', 'MOMO', '00'),
('PHASE1', 100000, DATE_SUB(CURRENT_DATE(), INTERVAL 18 DAY), 'Thanh toán đăng ký khám bệnh', 'ORDER_P1_003', 'WALLET', '00'),
('PHASE1', 100000, DATE_SUB(CURRENT_DATE(), INTERVAL 17 DAY), 'Thanh toán đăng ký khám bệnh', 'ORDER_P1_004', 'VNPAY', '00'),
('PHASE1', 100000, DATE_SUB(CURRENT_DATE(), INTERVAL 16 DAY), 'Thanh toán đăng ký khám bệnh', 'ORDER_P1_005', 'MOMO', '00'),
('PHASE1', 100000, DATE_SUB(CURRENT_DATE(), INTERVAL 15 DAY), 'Thanh toán đăng ký khám bệnh', 'ORDER_P1_006', 'WALLET', '00'),
('PHASE1', 100000, DATE_SUB(CURRENT_DATE(), INTERVAL 14 DAY), 'Thanh toán đăng ký khám bệnh', 'ORDER_P1_007', 'VNPAY', '00'),
('PHASE1', 100000, DATE_SUB(CURRENT_DATE(), INTERVAL 13 DAY), 'Thanh toán đăng ký khám bệnh', 'ORDER_P1_008', 'MOMO', '00'),
('PHASE1', 100000, DATE_SUB(CURRENT_DATE(), INTERVAL 12 DAY), 'Thanh toán đăng ký khám bệnh', 'ORDER_P1_009', 'WALLET', '00'),
('PHASE1', 100000, DATE_SUB(CURRENT_DATE(), INTERVAL 11 DAY), 'Thanh toán đăng ký khám bệnh', 'ORDER_P1_010', 'VNPAY', '00'),
('PHASE1', 100000, DATE_SUB(CURRENT_DATE(), INTERVAL 10 DAY), 'Thanh toán đăng ký khám bệnh', 'ORDER_P1_011', 'MOMO', '00'),
('PHASE1', 100000, DATE_SUB(CURRENT_DATE(), INTERVAL 9 DAY), 'Thanh toán đăng ký khám bệnh', 'ORDER_P1_012', 'WALLET', '00'),
('PHASE1', 100000, DATE_SUB(CURRENT_DATE(), INTERVAL 8 DAY), 'Thanh toán đăng ký khám bệnh', 'ORDER_P1_013', 'VNPAY', '00'),
('PHASE1', 100000, DATE_SUB(CURRENT_DATE(), INTERVAL 7 DAY), 'Thanh toán đăng ký khám bệnh', 'ORDER_P1_014', 'MOMO', '00'),
('PHASE1', 100000, DATE_SUB(CURRENT_DATE(), INTERVAL 6 DAY), 'Thanh toán đăng ký khám bệnh', 'ORDER_P1_015', 'WALLET', '00'),
('PHASE1', 100000, DATE_SUB(CURRENT_DATE(), INTERVAL 5 DAY), 'Thanh toán đăng ký khám bệnh', 'ORDER_P1_016', 'VNPAY', '00'),
('PHASE1', 100000, DATE_SUB(CURRENT_DATE(), INTERVAL 4 DAY), 'Thanh toán đăng ký khám bệnh', 'ORDER_P1_017', 'MOMO', '00'),
('PHASE1', 100000, DATE_SUB(CURRENT_DATE(), INTERVAL 3 DAY), 'Thanh toán đăng ký khám bệnh', 'ORDER_P1_018', 'WALLET', '00'),
('PHASE1', 100000, DATE_SUB(CURRENT_DATE(), INTERVAL 2 DAY), 'Thanh toán đăng ký khám bệnh', 'ORDER_P1_019', 'VNPAY', '00'),
('PHASE1', 100000, DATE_SUB(CURRENT_DATE(), INTERVAL 1 DAY), 'Thanh toán đăng ký khám bệnh', 'ORDER_P1_020', 'MOMO', '00'),

-- Phase 2 payments (examination and prescription)
('PHASE2', 300000, DATE_SUB(CURRENT_DATE(), INTERVAL 20 DAY), 'Thanh toán khám và thuốc', 'ORDER_P2_001', 'VNPAY', '00'),
('PHASE2', 250000, DATE_SUB(CURRENT_DATE(), INTERVAL 19 DAY), 'Thanh toán khám và thuốc', 'ORDER_P2_002', 'MOMO', '00'),
('PHASE2', 400000, DATE_SUB(CURRENT_DATE(), INTERVAL 18 DAY), 'Thanh toán khám và thuốc', 'ORDER_P2_003', 'WALLET', '00'),
('PHASE2', 350000, DATE_SUB(CURRENT_DATE(), INTERVAL 17 DAY), 'Thanh toán khám và thuốc', 'ORDER_P2_004', 'VNPAY', '00'),
('PHASE2', 420000, DATE_SUB(CURRENT_DATE(), INTERVAL 16 DAY), 'Thanh toán khám và thuốc', 'ORDER_P2_005', 'MOMO', '00'),
('PHASE2', 380000, DATE_SUB(CURRENT_DATE(), INTERVAL 15 DAY), 'Thanh toán khám và thuốc', 'ORDER_P2_006', 'WALLET', '00'),
('PHASE2', 290000, DATE_SUB(CURRENT_DATE(), INTERVAL 14 DAY), 'Thanh toán khám và thuốc', 'ORDER_P2_007', 'VNPAY', '00'),
('PHASE2', 320000, DATE_SUB(CURRENT_DATE(), INTERVAL 13 DAY), 'Thanh toán khám và thuốc', 'ORDER_P2_008', 'MOMO', '00'),
('PHASE2', 410000, DATE_SUB(CURRENT_DATE(), INTERVAL 12 DAY), 'Thanh toán khám và thuốc', 'ORDER_P2_009', 'WALLET', '00'),
('PHASE2', 280000, DATE_SUB(CURRENT_DATE(), INTERVAL 11 DAY), 'Thanh toán khám và thuốc', 'ORDER_P2_010', 'VNPAY', '00'),
('PHASE2', 330000, DATE_SUB(CURRENT_DATE(), INTERVAL 10 DAY), 'Thanh toán khám và thuốc', 'ORDER_P2_011', 'MOMO', '00'),
('PHASE2', 370000, DATE_SUB(CURRENT_DATE(), INTERVAL 9 DAY), 'Thanh toán khám và thuốc', 'ORDER_P2_012', 'WALLET', '00'),
('PHASE2', 340000, DATE_SUB(CURRENT_DATE(), INTERVAL 8 DAY), 'Thanh toán khám và thuốc', 'ORDER_P2_013', 'VNPAY', '00'),
('PHASE2', 360000, DATE_SUB(CURRENT_DATE(), INTERVAL 7 DAY), 'Thanh toán khám và thuốc', 'ORDER_P2_014', 'MOMO', '00'),
('PHASE2', 310000, DATE_SUB(CURRENT_DATE(), INTERVAL 6 DAY), 'Thanh toán khám và thuốc', 'ORDER_P2_015', 'WALLET', '00');


INSERT INTO medical_registry_list (name, favor, ql_url, is_canceled, created_date, is_voucher_taken, schedule_id, user_id, status_is_approved_id, payment_phase1_id)
VALUES
-- Completed examinations (usando schedule_id existentes del 1 al 15)
('Khám tổng quát', 'Đau đầu, sốt nhẹ', 'https://example.com/qr1', false, DATE_SUB(CURRENT_DATE(), INTERVAL 20 DAY), true, 1, 1, 5, 1),
('Khám đau bụng', 'Đau bụng sau khi ăn', 'https://example.com/qr2', false, DATE_SUB(CURRENT_DATE(), INTERVAL 19 DAY), true, 2, 2, 5, 2),
('Khám da liễu', 'Nổi mẩn đỏ', 'https://example.com/qr3', false, DATE_SUB(CURRENT_DATE(), INTERVAL 18 DAY), true, 3, 3, 5, 3),
('Khám tim mạch', 'Hồi hộp, tim đập nhanh', 'https://example.com/qr4', false, DATE_SUB(CURRENT_DATE(), INTERVAL 17 DAY), true, 4, 4, 5, 4),
('Khám tiêu hóa', 'Khó tiêu, ợ chua', 'https://example.com/qr5', false, DATE_SUB(CURRENT_DATE(), INTERVAL 16 DAY), true, 5, 5, 5, 5),
('Khám sức khỏe định kỳ', 'Kiểm tra sức khỏe', 'https://example.com/qr6', false, DATE_SUB(CURRENT_DATE(), INTERVAL 15 DAY), true, 6, 6, 5, 6),
('Khám thần kinh', 'Đau đầu, mất ngủ', 'https://example.com/qr7', false, DATE_SUB(CURRENT_DATE(), INTERVAL 14 DAY), true, 7, 7, 5, 7),
('Khám xương khớp', 'Đau nhức khớp gối', 'https://example.com/qr8', false, DATE_SUB(CURRENT_DATE(), INTERVAL 13 DAY), true, 8, 8, 5, 8),
('Khám mắt', 'Mờ mắt, nhức mắt', 'https://example.com/qr9', false, DATE_SUB(CURRENT_DATE(), INTERVAL 12 DAY), true, 9, 1, 5, 9),
('Khám tai mũi họng', 'Viêm họng', 'https://example.com/qr10', false, DATE_SUB(CURRENT_DATE(), INTERVAL 11 DAY), true, 10, 2, 5, 10),
('Khám ngoại khoa', 'Vết thương hở', 'https://example.com/qr11', false, DATE_SUB(CURRENT_DATE(), INTERVAL 10 DAY), true, 11, 3, 5, 11),
('Khám hô hấp', 'Ho kéo dài', 'https://example.com/qr12', false, DATE_SUB(CURRENT_DATE(), INTERVAL 9 DAY), true, 12, 4, 5, 12),
('Khám tiết niệu', 'Tiểu buốt', 'https://example.com/qr13', false, DATE_SUB(CURRENT_DATE(), INTERVAL 8 DAY), true, 13, 5, 5, 13),
('Khám nội tiết', 'Rối loạn nội tiết', 'https://example.com/qr14', false, DATE_SUB(CURRENT_DATE(), INTERVAL 7 DAY), true, 14, 6, 5, 14),
('Khám tim mạch', 'Đau ngực', 'https://example.com/qr15', false, DATE_SUB(CURRENT_DATE(), INTERVAL 6 DAY), true, 15, 7, 5, 15),

-- Approved (future) appointments (usando schedule_id del 16 al 20)
('Khám theo dõi', 'Theo dõi sau điều trị', 'https://example.com/qr16', false, CURRENT_DATE(), false, 16, 1, 2, 16),
('Khám sức khỏe định kỳ', 'Kiểm tra sức khỏe', 'https://example.com/qr17', false, CURRENT_DATE(), false, 17, 2, 2, 17),
('Khám nội khoa', 'Đau cơ, mệt mỏi', 'https://example.com/qr18', false, CURRENT_DATE(), false, 18, 3, 2, 18),
('Khám dị ứng', 'Dị ứng da, ngứa', 'https://example.com/qr19', false, CURRENT_DATE(), false, 19, 4, 2, 19),
('Khám hô hấp', 'Ho kéo dài', 'https://example.com/qr20', false, CURRENT_DATE(), false, 20, 5, 2, 20),

-- Pending appointments (usando schedule_id del 21 al 25)
('Khám mắt', 'Nhức mắt, mờ mắt', 'https://example.com/qr21', false, CURRENT_DATE(), false, 21, 6, 1, null),
('Khám tai mũi họng', 'Viêm họng', 'https://example.com/qr22', false, CURRENT_DATE(), false, 22, 7, 1, null),
('Khám nhi khoa', 'Sốt cao', 'https://example.com/qr23', false, CURRENT_DATE(), false, 23, 8, 1, null),
('Khám dinh dưỡng', 'Thiếu cân', 'https://example.com/qr24', false, CURRENT_DATE(), false, 24, 1, 1, null),
('Khám dị ứng', 'Nổi mề đay', 'https://example.com/qr25', false, CURRENT_DATE(), false, 25, 2, 1, null),

-- Canceled appointments (usando schedule_id del 26 al 30)
('Khám thần kinh', 'Đau đầu, chóng mặt', 'https://example.com/qr26', true, DATE_SUB(CURRENT_DATE(), INTERVAL 5 DAY), false, 26, 3, 3, null),
('Khám răng hàm mặt', 'Đau răng', 'https://example.com/qr27', true, DATE_SUB(CURRENT_DATE(), INTERVAL 4 DAY), false, 27, 4, 3, null),
('Khám tim mạch', 'Tăng huyết áp', 'https://example.com/qr28', true, DATE_SUB(CURRENT_DATE(), INTERVAL 3 DAY), false, 28, 5, 3, null),
('Khám dinh dưỡng', 'Suy dinh dưỡng', 'https://example.com/qr29', true, DATE_SUB(CURRENT_DATE(), INTERVAL 2 DAY), false, 29, 6, 3, null),
('Khám nhi khoa', 'Phát ban', 'https://example.com/qr30', true, DATE_SUB(CURRENT_DATE(), INTERVAL 1 DAY), false, 30, 7, 3, null);


-- Medical Examinations (with payment references to paymentdetail)
INSERT INTO medical_examination (created_date, predict, advance, symptom_process, treatment_process, duration_day, follow_up_date, user_created_id, medical_register_list_id, payment_phase2_id)
VALUES
-- Usar los IDs correctos de medicalRegistryList aquí
(DATE_SUB(CURRENT_DATE(), INTERVAL 20 DAY), 'Nhiễm virus', 'Nghỉ ngơi, uống nhiều nước', 'Sốt 38 độ, đau đầu, mệt mỏi', 'Dùng thuốc hạ sốt, thuốc giảm đau', 5, DATE_ADD(CURRENT_DATE(), INTERVAL 10 DAY), 9, 1, 21),
(DATE_SUB(CURRENT_DATE(), INTERVAL 19 DAY), 'Viêm dạ dày', 'Kiêng ăn cay, nóng', 'Đau bụng phần trên, buồn nôn', 'Dùng thuốc kháng acid, thuốc bảo vệ niêm mạc dạ dày', 7, DATE_ADD(CURRENT_DATE(), INTERVAL 11 DAY), 10, 2, 22),
(DATE_SUB(CURRENT_DATE(), INTERVAL 18 DAY), 'Dị ứng thời tiết', 'Tránh các yếu tố gây dị ứng', 'Nổi mẩn đỏ, ngứa', 'Dùng thuốc kháng histamine, kem bôi', 10, DATE_ADD(CURRENT_DATE(), INTERVAL 12 DAY), 11, 3, 23),
(DATE_SUB(CURRENT_DATE(), INTERVAL 17 DAY), 'Lo âu, stress', 'Giảm căng thẳng, tập thể dục', 'Tim đập nhanh, khó thở', 'Thuốc an thần nhẹ, thư giãn', 14, DATE_ADD(CURRENT_DATE(), INTERVAL 13 DAY), 12, 4, 24),
(DATE_SUB(CURRENT_DATE(), INTERVAL 16 DAY), 'Trào ngược dạ dày', 'Ăn chậm, không nằm ngay sau khi ăn', 'Ợ chua, cảm giác nóng rát', 'Thuốc ức chế bơm proton', 10, DATE_ADD(CURRENT_DATE(), INTERVAL 14 DAY), 9, 5, 25),
(DATE_SUB(CURRENT_DATE(), INTERVAL 15 DAY), 'Tình trạng sức khỏe bình thường', 'Duy trì chế độ ăn uống lành mạnh', 'Không có triệu chứng bất thường', 'Không cần điều trị', 0, DATE_ADD(CURRENT_DATE(), INTERVAL 180 DAY), 10, 6, 26),
(DATE_SUB(CURRENT_DATE(), INTERVAL 14 DAY), 'Đau đầu căng thẳng', 'Giảm stress, nghỉ ngơi đủ', 'Đau đầu vùng trán, cổ gáy', 'Thuốc giảm đau, massage', 7, DATE_ADD(CURRENT_DATE(), INTERVAL 15 DAY), 11, 7, 27),
(DATE_SUB(CURRENT_DATE(), INTERVAL 13 DAY), 'Viêm xương khớp', 'Tập vật lý trị liệu', 'Đau nhức khớp gối khi đi lại', 'Thuốc chống viêm, thuốc giảm đau', 14, DATE_ADD(CURRENT_DATE(), INTERVAL 16 DAY), 12, 8, 28),
(DATE_SUB(CURRENT_DATE(), INTERVAL 12 DAY), 'Mỏi mắt do làm việc quá sức', 'Hạn chế sử dụng thiết bị điện tử', 'Mờ mắt, khô mắt', 'Thuốc nhỏ mắt, nghỉ ngơi', 5, DATE_ADD(CURRENT_DATE(), INTERVAL 17 DAY), 9, 9, 29),
(DATE_SUB(CURRENT_DATE(), INTERVAL 11 DAY), 'Viêm họng cấp', 'Nghỉ ngơi, uống nhiều nước', 'Đau họng, khó nuốt', 'Kháng sinh, thuốc giảm đau họng', 7, DATE_ADD(CURRENT_DATE(), INTERVAL 18 DAY), 10, 10, 30),
(DATE_SUB(CURRENT_DATE(), INTERVAL 10 DAY), 'Vết thương bị nhiễm trùng', 'Vệ sinh vết thương hàng ngày', 'Vết thương đỏ, sưng, có mủ', 'Kháng sinh, thay băng thường xuyên', 7, DATE_ADD(CURRENT_DATE(), INTERVAL 19 DAY), 11, 11, 31),
(DATE_SUB(CURRENT_DATE(), INTERVAL 9 DAY), 'Viêm phế quản', 'Tránh khói thuốc, không khí ô nhiễm', 'Ho khan, khó thở', 'Thuốc giãn phế quản, kháng sinh', 10, DATE_ADD(CURRENT_DATE(), INTERVAL 20 DAY), 12, 12, 32),
(DATE_SUB(CURRENT_DATE(), INTERVAL 8 DAY), 'Viêm đường tiết niệu', 'Uống nhiều nước', 'Tiểu buốt, tiểu rắt', 'Kháng sinh, thuốc giảm đau', 7, DATE_ADD(CURRENT_DATE(), INTERVAL 21 DAY), 9, 13, 33),
(DATE_SUB(CURRENT_DATE(), INTERVAL 7 DAY), 'Suy giáp', 'Chế độ ăn cân bằng', 'Mệt mỏi, thay đổi cân nặng', 'Thuốc hormone tuyến giáp', 30, DATE_ADD(CURRENT_DATE(), INTERVAL 22 DAY), 10, 14, 34),
(DATE_SUB(CURRENT_DATE(), INTERVAL 6 DAY), 'Đau thắt ngực', 'Tránh các hoạt động gắng sức', 'Đau ngực khi gắng sức', 'Thuốc giãn mạch, chống đông', 14, DATE_ADD(CURRENT_DATE(), INTERVAL 23 DAY), 11, 15, 35);

-- Prescription Items for all medical examinations (comprehensive)
INSERT INTO prescription_items (prognosis, medical_usage, medicine_id, medical_examination_id)
VALUES
-- For examination 1 (Nhiễm virus)
(15, 'Uống sau ăn', 1, 1),  -- Paracetamol
(5, 'Uống trước ăn', 4, 1), -- Vitamin C

-- For examination 2 (Viêm dạ dày)
(21, 'Uống sau ăn', 3, 2),  -- Omeprazole
(7, 'Uống sau ăn', 14, 2), -- Esomeprazole

-- For examination 3 (Dị ứng thời tiết)
(10, 'Uống sau ăn', 6, 3),  -- Cetirizine
(6, 'Uống trước khi ngủ', 11, 3), -- Dexamethasone

-- For examination 4 (Lo âu, stress)
(14, 'Uống khi cần', 12, 4), -- Diazepam
(14, 'Uống sau ăn', 4, 4),   -- Vitamin C

-- For examination 5 (Trào ngược dạ dày)
(10, 'Uống trước ăn 30 phút', 3, 5),  -- Omeprazole
(10, 'Uống sau ăn', 15, 5), -- Multivitamin

-- For examination 6 (Khám sức khỏe định kỳ)
(30, 'Uống 1 viên/ngày', 15, 6), -- Multivitamin

-- For examination 7 (Đau đầu căng thẳng)
(15, 'Uống khi đau', 1, 7), -- Paracetamol
(7, 'Uống trước khi ngủ', 12, 7), -- Diazepam

-- For examination 8 (Viêm xương khớp)
(21, 'Uống sau ăn', 9, 8), -- Ibuprofen
(14, 'Bôi 2 lần/ngày', 11, 8), -- Dexamethasone

-- For examination 9 (Mỏi mắt)
(5, 'Uống 1 viên/ngày', 4, 9), -- Vitamin C
(30, 'Uống 1 viên/ngày', 15, 9), -- Multivitamin

-- For examination 10 (Viêm họng cấp)
(14, 'Uống sau ăn', 2, 10), -- Amoxicillin
(10, 'Ngậm 4 lần/ngày', 5, 10), -- Aspirin

-- For examination 11 (Vết thương nhiễm trùng)
(14, 'Uống sau ăn', 2, 11), -- Amoxicillin
(14, 'Uống sau ăn', 16, 11), -- Ciprofloxacin
(7, 'Bôi 2 lần/ngày', 11, 11), -- Dexamethasone

-- For examination 12 (Viêm phế quản)
(14, 'Uống sau ăn', 2, 12), -- Amoxicillin
(30, 'Uống khi ho', 9, 12), -- Ibuprofen
(5, 'Uống 1 lần/ngày', 4, 12), -- Vitamin C

-- For examination 13 (Viêm đường tiết niệu)
(10, 'Uống sau ăn', 16, 13), -- Ciprofloxacin
(10, 'Uống nhiều nước', 4, 13), -- Vitamin C

-- For examination 14 (Suy giáp)
(30, 'Uống buổi sáng lúc đói', 15, 14), -- Multivitamin
(30, 'Uống sau ăn', 4, 14), -- Vitamin C

-- For examination 15 (Đau thắt ngực)
(30, 'Uống sau ăn', 10, 15), -- Atorvastatin
(30, 'Uống sau ăn', 8, 15); -- Losartan

-- user_voucher table
INSERT INTO voucher_condition (id, expried_date, percent_sale) VALUES
                                                                   (1, DATE_ADD(CURRENT_DATE, INTERVAL 90 DAY), 5),
                                                                   (2, DATE_ADD(CURRENT_DATE, INTERVAL 120 DAY), 10),
                                                                   (3, DATE_ADD(CURRENT_DATE, INTERVAL 150 DAY), 20),
                                                                   (4, DATE_ADD(CURRENT_DATE, INTERVAL 180 DAY), 15),
                                                                   (5, DATE_ADD(CURRENT_DATE, INTERVAL 60 DAY), 30),
                                                                   (6, DATE_ADD(CURRENT_DATE, INTERVAL 100 DAY), 15),
                                                                   (7, DATE_ADD(CURRENT_DATE, INTERVAL 130 DAY), 15),
                                                                   (8, DATE_ADD(CURRENT_DATE, INTERVAL 160 DAY), 25);

-- Vouchers
INSERT INTO voucher (code, description, is_actived, voucher_condition_id)
VALUES
    ('NEWUSER', 'Khám lần đầu - Giảm 50.000đ', true, 1),
    ('SAVE10', 'Giảm 10% cho hóa đơn từ 200.000đ', true, 2),
    ('SAVE20', 'Giảm 20% cho hóa đơn từ 500.000đ', true, 3),
    ('CHECKUP', 'Khám định kỳ - Giảm 100.000đ', true, 4),
    ('VIPUSER', 'Giảm 30% cho khách hàng VIP', true, 5),
    ('BIRTHDAY', 'Giảm 150.000đ nhân dịp sinh nhật', true, 6),
    ('SAVE15', 'Giảm 15% cho khách hàng quay lại', true, 7),
    ('SPECIAL', 'Chăm sóc đặc biệt - Giảm 200.000đ', true, 8);

-- MRL Vouchers (linking vouchers to medical registry lists)
INSERT INTO mrl_voucher (mrl_id, voucher_id)
VALUES
    (1, 1), -- Patient 1's first exam used the NEWUSER voucher
    (2, 1), -- Patient 2's first exam used the NEWUSER voucher
    (2, 3), -- Patient 2's first exam also used the SAVE20 voucher
    (3, 1), -- Patient 3's first exam used the NEWUSER voucher
    (4, 1), -- Patient 4's first exam used the NEWUSER voucher
    (5, 1), -- Patient 5's first exam used the NEWUSER voucher
    (6, 1), -- Patient 6's first exam used the NEWUSER voucher
    (6, 4), -- Patient 6's first exam also used the CHECKUP voucher
    (7, 1), -- Patient 7's first exam used the NEWUSER voucher
    (8, 1), -- Patient 8's first exam used the NEWUSER voucher
    (10, 4), -- Patient 2's second exam used the CHECKUP voucher
    (12, 3), -- Patient 4's second exam used the SAVE20 voucher
    (14, 5), -- Patient 6's second exam used the VIP voucher
    (15, 3); -- Patient 7's second exam used the SAVE20 voucher

-- Create blogs
INSERT INTO blog (title, content, created_date, updated_date, user_id)
VALUES
    ('Cách phòng ngừa bệnh mùa hè', 'Mùa hè là thời điểm nhiều bệnh dễ phát sinh, đặc biệt là các bệnh về đường tiêu hóa và da liễu. Bài viết này sẽ hướng dẫn bạn cách phòng ngừa hiệu quả...\n\nMùa hè với thời tiết nóng bức là điều kiện thuận lợi cho vi khuẩn phát triển, đặc biệt trong thực phẩm. Vì vậy, việc bảo quản thực phẩm đúng cách là rất quan trọng. Ngoài ra, da của chúng ta cũng dễ bị tổn thương do tia UV. Hãy áp dụng những biện pháp sau để bảo vệ sức khỏe:\n\n1. Giữ vệ sinh cá nhân\n2. Ăn chín uống sôi\n3. Bảo quản thực phẩm đúng cách\n4. Bôi kem chống nắng\n5. Uống đủ nước', DATE_SUB(CURRENT_DATE(), INTERVAL 20 DAY), DATE_SUB(CURRENT_DATE(), INTERVAL 20 DAY), 9),
    ('Chế độ dinh dưỡng cho người cao tuổi', 'Người cao tuổi cần có chế độ dinh dưỡng đặc biệt để duy trì sức khỏe. Bài viết này sẽ giới thiệu các loại thực phẩm phù hợp và cách chế biến...\n\nKhi về già, cơ thể chúng ta trải qua nhiều thay đổi đáng kể về mặt sinh lý. Quá trình trao đổi chất chậm lại, khả năng hấp thu dinh dưỡng giảm sút, và các bệnh mãn tính có thể xuất hiện. Vì vậy, chế độ ăn uống cần được điều chỉnh phù hợp.\n\nNgười cao tuổi nên ưu tiên các thực phẩm sau:\n\n1. Thực phẩm giàu canxi như sữa, phô mai, cá hộp\n2. Protein dễ tiêu hóa từ thịt ức gà, cá, đậu phụ\n3. Trái cây và rau xanh\n4. Ngũ cốc nguyên hạt\n5. Thực phẩm giàu Omega-3', DATE_SUB(CURRENT_DATE(), INTERVAL 15 DAY), DATE_SUB(CURRENT_DATE(), INTERVAL 15 DAY), 10),
    ('Tác dụng của việc tập thể dục hàng ngày', 'Tập thể dục đều đặn không chỉ giúp duy trì vóc dáng mà còn có nhiều lợi ích sức khỏe khác. Bài viết này sẽ phân tích chi tiết những lợi ích đó...\n\nTập thể dục đều đặn mang lại vô số lợi ích cho sức khỏe thể chất và tinh thần. Nghiên cứu đã chứng minh rằng chỉ với 30 phút tập thể dục mỗi ngày có thể cải thiện đáng kể chất lượng cuộc sống.\n\nLợi ích của tập thể dục hàng ngày:\n\n1. Tăng cường sức khỏe tim mạch\n2. Kiểm soát cân nặng\n3. Giảm nguy cơ mắc bệnh mãn tính\n4. Cải thiện tâm trạng và giảm stress\n5. Tăng cường chất lượng giấc ngủ\n6. Nâng cao sức đề kháng', DATE_SUB(CURRENT_DATE(), INTERVAL 10 DAY), DATE_SUB(CURRENT_DATE(), INTERVAL 10 DAY), 11),
    ('Những hiểu lầm phổ biến về vaccine', 'Nhiều người còn hiểu lầm về tác dụng và tác hại của vaccine. Bài viết này sẽ giải thích khoa học và đưa ra những bằng chứng để giải tỏa những lo ngại...\n\nVaccine là một trong những phát minh quan trọng nhất trong lịch sử y học, đã cứu sống hàng triệu người. Tuy nhiên, vẫn còn nhiều hiểu lầm phổ biến về vaccine làm ảnh hưởng đến quyết định tiêm chủng.\n\nMột số hiểu lầm thường gặp:\n\n1. Vaccine gây ra bệnh tự kỷ - Đây là thông tin sai lệch, đã bị bác bỏ bởi nhiều nghiên cứu khoa học\n2. Vaccine chứa những chất độc hại - Các thành phần trong vaccine đều được kiểm nghiệm nghiêm ngặt và an toàn\n3. Tự nhiên mắc bệnh tốt hơn tiêm phòng - Không đúng, nhiều bệnh truyền nhiễm có thể gây biến chứng nguy hiểm, thậm chí tử vong\n4. Vaccine làm suy yếu hệ miễn dịch - Ngược lại, vaccine giúp tăng cường hệ miễn dịch', DATE_SUB(CURRENT_DATE(), INTERVAL 5 DAY), DATE_SUB(CURRENT_DATE(), INTERVAL 5 DAY), 12),
    ('Cách nhận biết các dấu hiệu đột quỵ', 'Đột quỵ là tình trạng nguy hiểm cần được xử lý nhanh chóng. Bài viết này sẽ giúp bạn nhận biết các dấu hiệu sớm của đột quỵ và cách xử lý ban đầu...\n\nĐột quỵ xảy ra khi máu không thể đến được một phần của não, gây tổn thương tế bào não. Đây là trường hợp cấp cứu y tế, mỗi phút trôi qua đều có thể ảnh hưởng nghiêm trọng đến khả năng phục hồi.\n\nĐể nhận biết đột quỵ, hãy nhớ kỹ thuật FAST:\n\nF (Face) - Mặt: Yêu cầu người đó cười. Nếu một bên mặt bị trễ xuống, đó có thể là dấu hiệu đột quỵ.\n\nA (Arms) - Cánh tay: Yêu cầu người đó giơ cả hai tay lên. Nếu một tay bị yếu và không thể giơ lên hoặc bị rơi xuống, đó có thể là dấu hiệu đột quỵ.\n\nS (Speech) - Nói: Yêu cầu người đó nói một câu đơn giản. Nếu lời nói không rõ ràng hoặc khó hiểu, đó có thể là dấu hiệu đột quỵ.\n\nT (Time) - Thời gian: Nếu bạn nhận thấy bất kỳ dấu hiệu nào trên, hãy gọi cấp cứu ngay lập tức.', CURRENT_DATE(), CURRENT_DATE(), 9),
    ('Hướng dẫn chăm sóc người bệnh tại nhà', 'Chăm sóc người bệnh tại nhà đòi hỏi kiến thức và kỹ năng cơ bản. Bài viết này cung cấp hướng dẫn chi tiết để giúp bạn chăm sóc người thân hiệu quả...\n\nChăm sóc người bệnh tại nhà là một trách nhiệm quan trọng đòi hỏi sự kiên nhẫn, thấu hiểu và kỹ năng thực hành. Dưới đây là một số hướng dẫn cơ bản:\n\n1. Tạo môi trường thoải mái và an toàn\n2. Tuân thủ đúng lịch uống thuốc\n3. Theo dõi các triệu chứng và dấu hiệu bất thường\n4. Đảm bảo chế độ dinh dưỡng phù hợp\n5. Giữ vệ sinh cá nhân cho người bệnh\n6. Hỗ trợ vận động và tập luyện phù hợp\n7. Chăm sóc tinh thần, tạo không khí vui vẻ', DATE_SUB(CURRENT_DATE(), INTERVAL 3 DAY), DATE_SUB(CURRENT_DATE(), INTERVAL 3 DAY), 10),
    ('Dinh dưỡng cho trẻ em trong giai đoạn phát triển', 'Giai đoạn từ 1-5 tuổi là thời kỳ quan trọng cho sự phát triển của trẻ. Bài viết này sẽ hướng dẫn cha mẹ về chế độ dinh dưỡng cân bằng cho trẻ...\n\nDinh dưỡng đóng vai trò then chốt trong sự phát triển thể chất và trí tuệ của trẻ em. Thiếu hụt dinh dưỡng trong giai đoạn này có thể để lại hậu quả lâu dài.\n\nMột chế độ dinh dưỡng cân bằng cho trẻ cần bao gồm:\n\n1. Protein từ thịt, cá, trứng, sữa và các sản phẩm từ đậu\n2. Carbohydrate từ gạo, bánh mì, khoai tây\n3. Chất béo lành mạnh từ dầu ô liu, cá hồi, bơ\n4. Vitamin và khoáng chất từ rau củ quả\n5. Canxi từ sữa và các sản phẩm từ sữa\n\nNgoài ra, cần hạn chế thực phẩm chế biến sẵn, đồ ngọt và thức ăn nhanh.', DATE_SUB(CURRENT_DATE(), INTERVAL 7 DAY), DATE_SUB(CURRENT_DATE(), INTERVAL 7 DAY), 11),
    ('Phòng ngừa và điều trị bệnh tiểu đường', 'Tiểu đường đang trở thành một trong những bệnh phổ biến nhất hiện nay. Bài viết này cung cấp thông tin về cách phòng ngừa và kiểm soát bệnh tiểu đường...\n\nBệnh tiểu đường là một bệnh mãn tính ảnh hưởng đến cách cơ thể chuyển hóa đường glucose. Nếu không được kiểm soát tốt, bệnh có thể dẫn đến nhiều biến chứng nghiêm trọng như bệnh tim, suy thận, mù lòa và tổn thương thần kinh.\n\nĐể phòng ngừa và kiểm soát bệnh tiểu đường:\n\n1. Duy trì cân nặng hợp lý\n2. Tập thể dục đều đặn, ít nhất 30 phút mỗi ngày\n3. Ăn chế độ ăn cân bằng, giàu chất xơ, hạn chế đường và tinh bột\n4. Kiểm tra đường huyết định kỳ\n5. Tuân thủ điều trị nếu đã được chẩn đoán mắc bệnh\n6. Hạn chế rượu bia và tuyệt đối không hút thuốc', DATE_SUB(CURRENT_DATE(), INTERVAL 8 DAY), DATE_SUB(CURRENT_DATE(), INTERVAL 8 DAY), 12);

-- Comments on blogs
INSERT INTO comment (content, created_date, updated_date, user_id)
VALUES
    ('Bài viết rất hữu ích, cảm ơn bác sĩ đã chia sẻ!', DATE_SUB(CURRENT_DATE(), INTERVAL 19 DAY), DATE_SUB(CURRENT_DATE(), INTERVAL 19 DAY),1),
    ('Tôi đã áp dụng và thấy sức khỏe cải thiện rõ rệt', DATE_SUB(CURRENT_DATE(), INTERVAL 18 DAY),DATE_SUB(CURRENT_DATE(), INTERVAL 18 DAY), 2),
    ('Xin hỏi bác sĩ có thể chia sẻ thêm về chế độ dinh dưỡng cho người bị tiểu đường không?', DATE_SUB(CURRENT_DATE(), INTERVAL 14 DAY), DATE_SUB(CURRENT_DATE(), INTERVAL 14 DAY),3),
    ('Bài viết rất chi tiết và dễ hiểu', DATE_SUB(CURRENT_DATE(), INTERVAL 9 DAY),DATE_SUB(CURRENT_DATE(), INTERVAL 9 DAY), 4),
    ('Cảm ơn bác sĩ đã giải thích rõ ràng về vấn đề này', DATE_SUB(CURRENT_DATE(), INTERVAL 4 DAY), DATE_SUB(CURRENT_DATE(), INTERVAL 4 DAY),5),
    ('Tôi đã từng bị và may mắn được cấp cứu kịp thời', CURRENT_DATE(),CURRENT_DATE(), 1),
    ('Cảm ơn bác sĩ về bài viết hữu ích', DATE_SUB(CURRENT_DATE(), INTERVAL 2 DAY),DATE_SUB(CURRENT_DATE(), INTERVAL 2 DAY), 2),
    ('Tôi muốn hỏi thêm về chế độ dinh dưỡng cho trẻ biếng ăn', DATE_SUB(CURRENT_DATE(), INTERVAL 6 DAY),DATE_SUB(CURRENT_DATE(), INTERVAL 6 DAY), 3),
    ('Bố mẹ tôi đều bị tiểu đường, tôi rất lo lắng về nguy cơ di truyền', DATE_SUB(CURRENT_DATE(), INTERVAL 7 DAY),DATE_SUB(CURRENT_DATE(), INTERVAL 7 DAY), 4),
    ('Bài viết rất bổ ích', DATE_SUB(CURRENT_DATE(), INTERVAL 17 DAY), DATE_SUB(CURRENT_DATE(), INTERVAL 17 DAY),6),
    ('Có thể cho tôi thêm thông tin về các loại vitamin tốt cho người cao tuổi?', DATE_SUB(CURRENT_DATE(), INTERVAL 13 DAY), DATE_SUB(CURRENT_DATE(), INTERVAL 13 DAY),7),
    ('Tôi tập thể dục mỗi ngày và thấy hiệu quả rõ rệt', DATE_SUB(CURRENT_DATE(), INTERVAL 8 DAY),DATE_SUB(CURRENT_DATE(), INTERVAL 8 DAY),8),
    ('Tin giả về vaccine thực sự rất nguy hiểm', DATE_SUB(CURRENT_DATE(), INTERVAL 3 DAY), DATE_SUB(CURRENT_DATE(), INTERVAL 3 DAY),6),
    ('Đã lưu lại kỹ thuật FAST, rất cảm ơn bác sĩ', DATE_SUB(CURRENT_DATE(), INTERVAL 0 DAY),DATE_SUB(CURRENT_DATE(), INTERVAL 0 DAY), 7),
    ('Đang chăm sóc mẹ già tại nhà, bài viết này giúp ích rất nhiều', DATE_SUB(CURRENT_DATE(), INTERVAL 2 DAY),DATE_SUB(CURRENT_DATE(), INTERVAL 2 DAY), 8),
    ('Con tôi rất kén ăn, cảm ơn bác sĩ về những lời khuyên', DATE_SUB(CURRENT_DATE(), INTERVAL 5 DAY), DATE_SUB(CURRENT_DATE(), INTERVAL 5 DAY),1),
    ('Gia đình tôi có tiền sử bệnh tiểu đường, bài viết này rất hữu ích', DATE_SUB(CURRENT_DATE(), INTERVAL 6 DAY), DATE_SUB(CURRENT_DATE(), INTERVAL 6 DAY),2);

-- Comment on blogs
INSERT INTO comment_blog (comment_id, blog_id)
VALUES
    (1, 1),  -- Bình luận có id=1 liên kết với bài viết có id=1
    (2, 1),  -- Bình luận có id=2 liên kết với bài viết có id=1
    (3, 2),  -- Bình luận có id=3 liên kết với bài viết có id=2
    (4, 3),  -- Bình luận có id=4 liên kết với bài viết có id=3
    (5, 4),  -- Bình luận có id=5 liên kết với bài viết có id=4
    (6, 5),  -- Bình luận có id=6 liên kết với bài viết có id=5
    (7, 6),  -- Bình luận có id=7 liên kết với bài viết có id=6
    (8, 7),  -- Bình luận có id=8 liên kết với bài viết có id=7
    (9, 8),  -- Bình luận có id=9 liên kết với bài viết có id=8
    (10, 1), -- Bình luận có id=10 liên kết với bài viết có id=1
    (11, 2), -- Bình luận có id=11 liên kết với bài viết có id=2
    (12, 3), -- Bình luận có id=12 liên kết với bài viết có id=3
    (13, 4), -- Bình luận có id=13 liên kết với bài viết có id=4
    (14, 5), -- Bình luận có id=14 liên kết với bài viết có id=5
    (15, 6), -- Bình luận có id=15 liên kết với bài viết có id=6
    (16, 7), -- Bình luận có id=16 liên kết với bài viết có id=7
    (17, 8); -- Bình luận có id=17 liên kết với bài viết có id=8

-- Like blogs
-- Like blogs (với cột has_liked)
INSERT INTO like_blog (has_liked, blog_id, user_id)
VALUES
    (1, 1, 1), -- Dòng gốc là (true, 1, 1), đổi true thành 1
    (1, 1, 2), -- Dòng gốc là (1, 2), giả định là (true, 1, 2)
    (1, 1, 3), -- Dòng gốc là (1, 3), giả định là (true, 1, 3)
    (1, 1, 4), -- Dòng gốc là (1, 4), giả định là (true, 1, 4)
    (1, 1, 5), -- Dòng gốc là (1, 5), giả định là (true, 1, 5)
    (1, 1, 6), -- Dòng gốc là (1, 6), giả định là (true, 1, 6)
    (1, 2, 2), -- Dòng gốc là (2, 2), giả định là (true, 2, 2)
    (1, 2, 4), -- Dòng gốc là (2, 4), giả định là (true, 2, 4)
    (1, 2, 6), -- Dòng gốc là (2, 6), giả định là (true, 2, 6)
    (1, 2, 8), -- Dòng gốc là (2, 8), giả định là (true, 2, 8)
    (1, 2, 1), -- Dòng gốc là (2, 1), giả định là (true, 2, 1)
    (1, 3, 1), -- Dòng gốc là (3, 1), giả định là (true, 3, 1)
    (1, 3, 3), -- Dòng gốc là (3, 3), giả định là (true, 3, 3)
    (1, 3, 5), -- Dòng gốc là (3, 5), giả định là (true, 3, 5)
    (1, 3, 7), -- Dòng gốc là (3, 7), giả định là (true, 3, 7)
    (1, 3, 2), -- Dòng gốc là (3, 2), giả định là (true, 3, 2)
    (1, 4, 2), -- Dòng gốc là (4, 2), giả định là (true, 4, 2)
    (1, 4, 4), -- Dòng gốc là (4, 4), giả định là (true, 4, 4)
    (1, 4, 5), -- Dòng gốc là (4, 5), giả định là (true, 4, 5)
    (1, 4, 6), -- Dòng gốc là (4, 6), giả định là (true, 4, 6)
    (1, 4, 8), -- Dòng gốc là (4, 8), giả định là (true, 4, 8)
    (1, 4, 3), -- Dòng gốc là (4, 3), giả định là (true, 4, 3)
    (1, 5, 1), -- Dòng gốc là (5, 1), giả định là (true, 5, 1)
    (1, 5, 3), -- Dòng gốc là (5, 3), giả định là (true, 5, 3)
    (1, 5, 5), -- Dòng gốc là (5, 5), giả định là (true, 5, 5)
    (1, 5, 7), -- Dòng gốc là (5, 7), giả định là (true, 5, 7)
    (1, 5, 2), -- Dòng gốc là (5, 2), giả định là (true, 5, 2)
    (1, 5, 4), -- Dòng gốc là (5, 4), giả định là (true, 5, 4)
    (1, 5, 6), -- Dòng gốc là (5, 6), giả định là (true, 5, 6)
    (1, 6, 2), -- Dòng gốc là (6, 2), giả định là (true, 6, 2)
    (1, 6, 4), -- Dòng gốc là (6, 4), giả định là (true, 6, 4)
    (1, 6, 6), -- Dòng gốc là (6, 6), giả định là (true, 6, 6)
    (1, 6, 8), -- Dòng gốc là (6, 8), giả định là (true, 6, 8)
    (1, 6, 1), -- Dòng gốc là (6, 1), giả định là (true, 6, 1)
    (1, 6, 3), -- Dòng gốc là (6, 3), giả định là (true, 6, 3)
    (1, 7, 1), -- Dòng gốc là (7, 1), giả định là (true, 7, 1)
    (1, 7, 3), -- Dòng gốc là (7, 3), giả định là (true, 7, 3)
    (1, 7, 5), -- Dòng gốc là (7, 5), giả định là (true, 7, 5)
    (1, 7, 7), -- Dòng gốc là (7, 7), giả định là (true, 7, 7)
    (1, 7, 2), -- Dòng gốc là (7, 2), giả định là (true, 7, 2)
    (1, 7, 4), -- Dòng gốc là (7, 4), giả định là (true, 7, 4)
    (1, 8, 2), -- Dòng gốc là (8, 2), giả định là (true, 8, 2)
    (1, 8, 4), -- Dòng gốc là (8, 4), giả định là (true, 8, 4)
    (1, 8, 6), -- Dòng gốc là (8, 6), giả định là (true, 8, 6)
    (1, 8, 8), -- Dòng gốc là (8, 8), giả định là (true, 8, 8)
    (1, 8, 1), -- Dòng gốc là (8, 1), giả định là (true, 8, 1)
    (1, 8, 3), -- Dòng gốc là (8, 3), giả định là (true, 8, 3)
    (1, 8, 5); -- Dòng gốc là (8, 5), giả định là (true, 8, 5)

-- Wallet History
INSERT INTO wallet_history (created_date, note, withdrawal, wallet_id) VALUES
-- Depósitos iniciales
(DATE_SUB(CURRENT_DATE(), INTERVAL 20 DAY), 'Nạp tiền ban đầu', 500000, 1),
(DATE_SUB(CURRENT_DATE(), INTERVAL 20 DAY), 'Nạp tiền ban đầu', 1000000, 2),
(DATE_SUB(CURRENT_DATE(), INTERVAL 20 DAY), 'Nạp tiền ban đầu', 1500000, 3),
(DATE_SUB(CURRENT_DATE(), INTERVAL 20 DAY), 'Nạp tiền ban đầu', 2000000, 4),
(DATE_SUB(CURRENT_DATE(), INTERVAL 20 DAY), 'Nạp tiền ban đầu', 2500000, 5),
(DATE_SUB(CURRENT_DATE(), INTERVAL 20 DAY), 'Nạp tiền ban đầu', 3000000, 6),
(DATE_SUB(CURRENT_DATE(), INTERVAL 20 DAY), 'Nạp tiền ban đầu', 3500000, 7),
(DATE_SUB(CURRENT_DATE(), INTERVAL 20 DAY), 'Nạp tiền ban đầu', 4000000, 8),
(DATE_SUB(CURRENT_DATE(), INTERVAL 20 DAY), 'Nạp tiền ban đầu', 4500000, 9),
(DATE_SUB(CURRENT_DATE(), INTERVAL 20 DAY), 'Nạp tiền ban đầu', 5000000, 10),

-- Retiros para pagos médicos
(DATE_SUB(CURRENT_DATE(), INTERVAL 10 DAY), 'Thanh toán khám bệnh', -150000, 1),
(DATE_SUB(CURRENT_DATE(), INTERVAL 10 DAY), 'Thanh toán thuốc', -250000, 1),
(DATE_SUB(CURRENT_DATE(), INTERVAL 10 DAY), 'Thanh toán khám bệnh', -180000, 2),
(DATE_SUB(CURRENT_DATE(), INTERVAL 10 DAY), 'Thanh toán thuốc', -320000, 2),
(DATE_SUB(CURRENT_DATE(), INTERVAL 30 DAY), 'Thanh toán khám bệnh', -200000, 3),
(DATE_SUB(CURRENT_DATE(), INTERVAL 30 DAY), 'Thanh toán thuốc', -400000, 3),
(DATE_SUB(CURRENT_DATE(), INTERVAL 30 DAY), 'Thanh toán khám bệnh', -220000, 4),
(DATE_SUB(CURRENT_DATE(), INTERVAL 30 DAY), 'Thanh toán thuốc', -350000, 4),
(DATE_SUB(CURRENT_DATE(), INTERVAL 30 DAY), 'Thanh toán khám bệnh', -190000, 5),
(DATE_SUB(CURRENT_DATE(), INTERVAL 30 DAY), 'Thanh toán thuốc', -280000, 5),

-- Depósitos adicionales
(DATE_SUB(CURRENT_DATE(), INTERVAL 40 DAY), 'Nạp thêm tiền', 800000, 1),
(DATE_SUB(CURRENT_DATE(), INTERVAL 40 DAY), 'Nạp thêm tiền', 1200000, 3),
(DATE_SUB(CURRENT_DATE(), INTERVAL 10 DAY), 'Nạp thêm tiền', 1500000, 5),
(DATE_SUB(CURRENT_DATE(), INTERVAL 20 DAY), 'Nạp thêm tiền', 900000, 7),
(DATE_SUB(CURRENT_DATE(), INTERVAL 5 DAY), 'Nạp thêm tiền', 700000, 9),
(DATE_SUB(CURRENT_DATE(), INTERVAL 5 DAY), 'Nạp thêm tiền', 1000000, 2),

-- Transacciones más recientes
(DATE_SUB(CURRENT_DATE(), INTERVAL 20 DAY), 'Thanh toán khám bệnh', -170000, 6),
(DATE_SUB(CURRENT_DATE(), INTERVAL 20 DAY), 'Thanh toán thuốc', -290000, 6),
(DATE_SUB(CURRENT_DATE(), INTERVAL 15 DAY), 'Thanh toán khám bệnh', -210000, 7),
(DATE_SUB(CURRENT_DATE(), INTERVAL 15 DAY), 'Thanh toán thuốc', -380000, 7),
(DATE_SUB(CURRENT_DATE(), INTERVAL 15 DAY), 'Thanh toán khám bệnh', -160000, 8),
(DATE_SUB(CURRENT_DATE(), INTERVAL 15 DAY), 'Thanh toán thuốc', -310000, 8),
(DATE_SUB(CURRENT_DATE(), INTERVAL 15 DAY), 'Thanh toán khám bệnh', -195000, 9),
(DATE_SUB(CURRENT_DATE(), INTERVAL 15 DAY), 'Thanh toán thuốc', -340000, 9),
(DATE_SUB(CURRENT_DATE(), INTERVAL 15 DAY), 'Thanh toán khám bệnh', -205000, 10),
(DATE_SUB(CURRENT_DATE(), INTERVAL 15 DAY), 'Thanh toán thuốc', -370000, 10);




INSERT INTO status_is_approved (note, status)
VALUES ('', 'CHECKING'), ('', 'CANCELED')
ON DUPLICATE KEY UPDATE status = VALUES(status);