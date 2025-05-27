-- DB作成と初期化（必要に応じて）
DROP DATABASE IF EXISTS ettewebservice;
CREATE DATABASE ettewebservice;
USE ettewebservice;

-- 1. 権限テーブル
CREATE TABLE role (
    role_id INT PRIMARY KEY,
    role_name VARCHAR(50) NOT NULL
);

ALTER TABLE product
ADD COLUMN description VARCHAR(1000);

-- 2. キーワード
CREATE TABLE keyword (
    keyword_id INT PRIMARY KEY,
    keyword_name VARCHAR(50) NOT NULL
);
DELETE FROM category;
-- 3. カテゴリー
CREATE TABLE category (
    category_id INT PRIMARY KEY,
    category_name VARCHAR(50) NOT NULL
);

-- 4. 製造国
CREATE TABLE country (
    country_id INT PRIMARY KEY,
    country_name VARCHAR(50) NOT NULL
);

-- 5. ユーザー（userは予約語なのでuser_accountに変更推奨）
CREATE TABLE user_account (
    user_id INT AUTO_INCREMENT PRIMARY KEY,
    login_id VARCHAR(50) NOT NULL UNIQUE,
    password VARCHAR(50) NOT NULL,
    last_name VARCHAR(50) NOT NULL,
    first_name VARCHAR(50) NOT NULL,
    gender VARCHAR(10) NOT NULL,
    email VARCHAR(100),
    image_path VARCHAR(255),
    keyword_1 INT,
    keyword_2 INT,
    keyword_3 INT,
    role_id INT,
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
    updated_at DATETIME NULL,
    FOREIGN KEY (role_id) REFERENCES role(role_id),
    FOREIGN KEY (keyword_1) REFERENCES keyword(keyword_id),
    FOREIGN KEY (keyword_2) REFERENCES keyword(keyword_id),
    FOREIGN KEY (keyword_3) REFERENCES keyword(keyword_id)
);

-- 6. 商品
CREATE TABLE product (
    product_id INT AUTO_INCREMENT PRIMARY KEY,
    product_name VARCHAR(100) NOT NULL,
    category_id INT NOT NULL,
    keyword_id INT NOT NULL,
    country_id INT NOT NULL,
    manufacture_date DATE NOT NULL,
    regular_price INT NOT NULL,
    discount_rate INT NOT NULL CHECK (discount_rate BETWEEN 0 AND 100),
    sale_price INT NOT NULL,
    stock INT NOT NULL,
    imagePath VARCHAR(100) NOT NULL,
    \description VARCHAR(1000),
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
    updated_at DATETIME NULL,
    FOREIGN KEY (category_id) REFERENCES category(category_id),
    FOREIGN KEY (keyword_id) REFERENCES keyword(keyword_id),
    FOREIGN KEY (country_id) REFERENCES country(country_id)
);

CREATE TABLE recommends (
    recomend_id INT AUTO_INCREMENT PRIMARY KEY,
    product_id INT NOT NULL,
    FOREIGN KEY (product_id) REFERENCES product(product_id)
);

-- 8. 購入履歴
CREATE TABLE purchase (
    purchase_id INT AUTO_INCREMENT PRIMARY KEY,
    user_id INT NOT NULL,
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (user_id) REFERENCES user_account(user_id)
);

-- 9. 購入詳細
CREATE TABLE purchase_detail (
    purchase_detail_id INT AUTO_INCREMENT PRIMARY KEY,
    purchase_id INT NOT NULL,
    product_id INT NOT NULL,
    size_id INT NOT NULL,
    quantity INT NOT NULL,
    price INT NOT NULL,
    FOREIGN KEY (purchase_id) REFERENCES purchase(purchase_id),
    FOREIGN KEY (product_id) REFERENCES product(product_id),
    FOREIGN KEY (size_id) REFERENCES size(size_id)
);

-- 10. サイズ
CREATE TABLE size (
    size_id INT AUTO_INCREMENT PRIMARY KEY,
    size_name VARCHAR(20) NOT NULL
);

-- 11. サイズ別数量
CREATE TABLE product_size_stock (
    product_id INT NOT NULL,
    size_id INT NOT NULL,
    stock INT NOT NULL,
    PRIMARY KEY (product_id, size_id),
    FOREIGN KEY (product_id) REFERENCES product(product_id),
    FOREIGN KEY (size_id) REFERENCES size(size_id)
);