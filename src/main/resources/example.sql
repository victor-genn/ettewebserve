-- 権限
INSERT INTO role (role_id, role_name) VALUES
(1, '管理者'),
(2, '一般ユーザー');

-- キーワード（服のスタイル）
INSERT INTO keyword (keyword_id, keyword_name) VALUES
(1, 'カジュアル'),
(2, 'フォーマル'),
(3, 'フェミニン'),
(4, 'ストリート'),
(5, 'スポーティー'),
(6, 'モード'),
(7, 'ナチュラル'),
(8, 'ヴィンテージ'),
(9, 'エレガント'),
(10, 'トラッド');

-- カテゴリー
INSERT INTO category (category_id, category_name) VALUES
(1, 'レディースファッション'),
(2, 'メンズファッション'),
(3, 'キッズ・ベビー・マタニティ'),
(4, '靴・シューズ'),
(5, 'バッグ・小物・ブランド雑貨'),
(6, 'アクセサリー・ジュエリー'),
(7, '下着・ルームウェア'),
(8, '和服・着物'),
(9, '家電'),
(10, '食品'),
(11, 'コスメ・美容'),
(12, '本・雑誌・ゲーム');

-- 国（10件）
INSERT INTO country (country_id, country_name) VALUES
(1, '日本'),
(2, '韓国'),
(3, '中国'),
(4, 'アメリカ'),
(5, 'フランス'),
(6, 'ドイツ'),
(7, 'イタリア'),
(8, 'ベトナム'),
(9, 'イギリス'),
(10, 'カナダ');

-- ユーザー（20名）
INSERT INTO user_account (
    login_id, password, last_name, first_name, gender, email,
    image_path, keyword_1, keyword_2, keyword_3, role_id
) VALUES
('user01', 'pass01', '佐藤', '太郎', '男', 'user01@example.com', '/images/user1.png', 1, 2, 3, 2),
('user02', 'pass02', '鈴木', '花子', '女', 'user02@example.com', '/images/user2.png', 2, 3, 4, 2),
('user03', 'pass03', '高橋', '健', '男', 'user03@example.com', '/images/user3.png', 3, 4, 5, 2),
('user04', 'pass04', '田中', '優子', '女', 'user04@example.com', '/images/user4.png', 4, 5, 6, 2),
('user05', 'pass05', '伊藤', '翔', '男', 'user05@example.com', '/images/user5.png', 5, 6, 7, 2),
('user06', 'pass06', '渡辺', '陽子', '女', 'user06@example.com', '/images/user6.png', 6, 7, 8, 2),
('user07', 'pass07', '山本', '大輔', '男', 'user07@example.com', '/images/user7.png', 7, 8, 9, 2),
('user08', 'pass08', '中村', '彩', '女', 'user08@example.com', '/images/user8.png', 8, 9, 10, 2),
('user09', 'pass09', '小林', '光', '男', 'user09@example.com', '/images/user9.png', 9, 10, 1, 2),
('user10', 'pass10', '加藤', '未来', '女', 'user10@example.com', '/images/user10.png', 10, 1, 2, 2),
('user11', 'pass11', '井上', '悠真', '男', 'user11@example.com', '/images/user11.png', 1, 3, 5, 2),
('user12', 'pass12', '木村', '紗季', '女', 'user12@example.com', '/images/user12.png', 2, 4, 6, 2),
('user13', 'pass13', '林', '亮', '男', 'user13@example.com', '/images/user13.png', 3, 5, 7, 2),
('user14', 'pass14', '斎藤', '恵美', '女', 'user14@example.com', '/images/user14.png', 4, 6, 8, 2),
('user15', 'pass15', '清水', '健二', '男', 'user15@example.com', '/images/user15.png', 5, 7, 9, 2),
('user16', 'pass16', '山田', '真由美', '女', 'user16@example.com', '/images/user16.png', 6, 8, 10, 2),
('user17', 'pass17', '松本', '大地', '男', 'user17@example.com', '/images/user17.png', 7, 9, 1, 2),
('user18', 'pass18', '西村', '愛', '女', 'user18@example.com', '/images/user18.png', 8, 10, 2, 2),
('user19', 'pass19', '藤田', '悠', '男', 'user19@example.com', '/images/user19.png', 9, 1, 3, 2),
('user20', 'pass20', '岡田', '遥', '女', 'user20@example.com', '/images/user20.png', 10, 2, 4, 2);

-- 商品（20件）
INSERT INTO product (
    product_name, category_id, keyword_id, country_id, manufacture_date,
    regular_price, discount_rate, sale_price, stock, imagePath
) VALUES
('カジュアルTシャツ', 1, 1, 2, '2024-04-01', 3000, 10, 2700, 50, '/images/products/01.jpg'),
('フォーマルジャケット', 2, 2, 1, '2024-01-15', 12000, 25, 9000, 30, '/images/products/02.jpg'),
('フェミニンワンピース', 1, 3, 5, '2024-03-05', 8000, 20, 6400, 20, '/images/products/03.jpg'),
('ストリートパーカー', 2, 4, 3, '2023-12-10', 7000, 15, 5950, 25, '/images/products/04.jpg'),
('スポーティージャージ', 1, 5, 6, '2024-02-01', 5000, 5, 4750, 40, '/images/products/05.jpg'),
('モードシャツ', 1, 6, 7, '2023-11-11', 8500, 30, 5950, 15, '/images/products/06.jpg'),
('ナチュラルブラウス', 1, 7, 8, '2024-05-20', 6000, 0, 6000, 35, '/images/products/07.jpg'),
('ヴィンテージコート', 2, 8, 4, '2023-10-25', 15000, 40, 9000, 10, '/images/products/08.jpg'),
('エレガントドレス', 1, 9, 9, '2024-03-15', 20000, 50, 10000, 12, '/images/products/09.jpg'),
('トラッドジャケット', 2, 10, 10, '2023-09-30', 10000, 20, 8000, 18, '/images/products/10.jpg'),
('サマーニット', 1, 1, 2, '2024-04-10', 3500, 10, 3150, 30, '/images/products/11.jpg'),
('ビジネスシャツ', 2, 2, 1, '2024-02-10', 6500, 15, 5525, 22, '/images/products/12.jpg'),
('レースブラウス', 1, 3, 5, '2024-01-01', 5800, 5, 5510, 40, '/images/products/13.jpg'),
('フード付きスウェット', 2, 4, 6, '2023-11-20', 7500, 20, 6000, 35, '/images/products/14.jpg'),
('ジムウェアセット', 1, 5, 8, '2024-04-18', 9800, 15, 8330, 28, '/images/products/15.jpg'),
('ブラックモードスーツ', 2, 6, 7, '2024-03-09', 18000, 35, 11700, 8, '/images/products/16.jpg'),
('リネンシャツ', 1, 7, 9, '2024-05-01', 6200, 10, 5580, 32, '/images/products/17.jpg'),
('レトロカーディガン', 1, 8, 10, '2023-10-01', 7200, 25, 5400, 18, '/images/products/18.jpg'),
('シフォンワンピース', 1, 9, 3, '2024-05-05', 8400, 10, 7560, 26, '/images/products/19.jpg'),
('チェックブレザー', 2, 10, 4, '2023-12-25', 11000, 20, 8800, 14, '/images/products/20.jpg');

INSERT INTO recommends (product_id) VALUES
(1), (2), (3), (4), (5);

-- サイズ
INSERT INTO size (size_name) VALUES
('S'), ('M'), ('L'), ('XL');
