
INSERT INTO `user` (id, user_name, created_at, updated_at) VALUES
    (1, '임동욱', '2025-01-05 13:52:36', '2025-01-05 13:52:36');

INSERT INTO `user_balance` (id, user_id, current_balance, last_updated) VALUES
    (1, 1, 200000.00, '2025-01-05 13:52:36');

INSERT INTO product (name,description,price,created_at) VALUES
('티셔츠','부드럽고 편안한 면 티셔츠',19900,'2025-01-05 05:38:09'),
('청바지','스타일리시한 데님 청바지',49900,'2025-01-05 05:38:09'),
('자켓','캐주얼한 멋스러움을 더해주는 가죽 자켓',149900,'2025-01-05 05:38:09'),
('원피스','가볍고 시원한 여름 원피스',39900,'2025-01-05 05:38:09');

INSERT INTO product_inventory (product_id,stock,last_updated) VALUES
     (1,100,'2025-01-09 08:37:50'),
     (2,50,'2025-01-08 16:29:41'),
     (3,40,'2025-01-05 14:18:56'),
     (4,60,'2025-01-05 14:18:56');


INSERT INTO coupon (name,discount_type,discount_value,minimum_order_amount,limit_count,issued_count,valid_from,valid_until) VALUES
('10% 할인 쿠폰','PERCENTAGE',10.00,100.00,20,0,'2025-01-01 00:00:00','2025-12-31 23:59:59'),
('20% 할인 쿠폰','PERCENTAGE',20.00,200.00,20,0,'2025-01-01 00:00:00','2025-12-31 23:59:59');

INSERT INTO issued_coupon (user_id,coupon_id,used,used_at,created_at) VALUES
 (1,1,0,NULL,'2025-01-06 13:50:27'),
 (2,2,0,NULL,'2025-01-06 13:50:27'),
 (1,2,0,NULL,'2025-01-07 03:51:05'),
 (1,1,0,NULL,'2025-01-08 08:52:37'),
 (1,1,0,NULL,'2025-01-09 07:18:05'),
 (1,1,0,NULL,'2025-01-09 15:35:32');


