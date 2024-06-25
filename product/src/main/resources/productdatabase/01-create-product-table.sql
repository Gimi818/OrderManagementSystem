--liquibase formatted sql
--changeset wgmiterek:1
CREATE TABLE IF NOT EXISTS product (
                                       id BIGSERIAL PRIMARY KEY,
                                       name VARCHAR(255) NOT NULL,
                                       price NUMERIC(19, 2) NOT NULL,
                                       stock_quantity INT NOT NULL,
                                       product_category VARCHAR(255) NOT NULL
);
