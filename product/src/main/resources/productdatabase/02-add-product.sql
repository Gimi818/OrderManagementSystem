--liquibase formatted sql
--changeset wgmiterek:2
INSERT INTO product (name, price, stock_quantity, product_category) VALUES
('iPhone 13', 3200.00, 35, 'PHONE'),
('Samsung Galaxy S21', 2199.99, 20, 'PHONE'),
('Google Pixel 6', 4100.00, 10, 'PHONE'),
('OnePlus 9', 1799.99, 12, 'PHONE'),
('Sony Xperia 1 II', 2499.99, 20, 'PHONE'),
('Xiaomi Mi 11', 2799.99, 10, 'PHONE'),
('Oppo Find X3 Pro', 1049.99, 15, 'PHONE'),
('Huawei P40 Pro', 2499.99, 20, 'PHONE'),
('iPhone 12', 2799.99, 25, 'PHONE'),
('Samsung Q60T', 4999.99, 20, 'TV'),
('LG CX OLED', 3999.99, 15, 'TV'),
('Sony Bravia X90J', 3800.00, 40, 'TV'),
('Vizio P-Series Quantum', 2999.99, 18, 'TV'),
('Samsung The Frame', 3500.00, 25, 'TV'),
('Sony A8H OLED', 3700, 20, 'TV'),
('TCL 6-Series R635', 1650.00, 12, 'TV'),
('Philips 805 OLED', 3999.99, 10, 'TV');
