# 데이터베이스 테이블 설계 보고서

## 테이블 설계 상세

### 1. 사용자 (User)

- **설명**: 사용자의 기본 정보를 저장하는 테이블입니다.
- **주요 필드**:
    - `id`: 사용자 고유 식별자 (Primary Key).
    - `user_name`: 사용자의 이름.
    - `created_at`, `updated_at`: 생성 및 수정 시간.
- **활용**:
    - 잔액 관리, 주문 생성, 결제 처리와 같은 다양한 비즈니스 로직에서 사용자 식별을 위해 사용됩니다.

---

### 2. 사용자 잔고 (User Balances)

- **설명**: 특정 사용자의 잔액 정보를 관리하는 테이블입니다.
- **주요 필드**:
    - `current_balance`: 현재 사용 가능한 잔액.
    - `last_updated`: 마지막 잔액 수정 시간.
- **관계**:
    - `user_id`는 `user` 테이블의 `id`를 참조합니다.
- **활용**:
    - 결제 처리 시 사용자의 잔액 차감에 활용됩니다.

---

### 3. 상품 (Product)

- **설명**: 애플리케이션에서 판매되는 상품 정보를 저장하는 테이블입니다.
- **주요 필드**:
    - `name`: 상품 이름.
    - `description`: 상품 설명.
    - `price`: 상품 가격.
    - `created_at`: 상품이 등록된 시간.
- **활용**:
    - 주문 및 재고 관리에 핵심 역할을 합니다.

---

### 4. 주문 (Orders)

- **설명**: 사용자가 생성한 주문 정보를 저장합니다.
- **주요 필드**:
    - `user_id`: 주문을 생성한 사용자 ID.
    - `total_price`: 주문 총 금액.
    - `status`: 주문 상태 (`PENDING`, `COMPLETED`, `CANCELLED` 등).
    - `created_at`: 주문 생성 시점.
- **관계**:
    - `user_id`는 `user` 테이블의 `id`를 참조합니다.
- **활용**:
    - 주문 생성과 결제 진행 과정에서 사용됩니다.

---

### 5. 주문 상세 (Order Items)

- **설명**: 특정 주문에 포함된 상품 정보를 저장하는 테이블입니다.
- **주요 필드**:
    - `order_id`: 주문 ID.
    - `product_id`: 주문한 상품 ID.
    - `quantity`: 주문한 상품 수량.
    - `price`: 상품 단가.
    - `discount_amount`: 상품에 적용된 할인 금액.
- **관계**:
    - `order_id`는 `orders` 테이블의 `id`를 참조합니다.
    - `product_id`는 `product` 테이블의 `id`를 참조합니다.
- **활용**:
    - 주문 상세 정보 및 할인 적용 내역을 관리합니다.

---

### 6. 결제 (Payments)

- **설명**: 주문과 연계된 결제 정보를 저장하는 테이블입니다.
- **주요 필드**:
    - `order_id`: 결제된 주문 ID.
    - `user_id`: 결제한 사용자 ID.
    - `amount`: 최종 결제 금액.
    - `discount_amount`: 쿠폰으로 적용된 할인 금액.
    - `status`: 결제 상태 (`SUCCESS`, `FAILED` 등).
    - `created_at`: 결제 처리 시간.
- **관계**:
    - `order_id`는 `orders` 테이블의 `id`를 참조합니다.
    - `user_id`는 `user` 테이블의 `id`를 참조합니다.
- **활용**:
    - 결제 상태 관리 및 쿠폰, 잔액과의 연계를 처리합니다.

---

### 7. 쿠폰 (Coupons)

- **설명**: 애플리케이션에서 제공하는 쿠폰 정보를 저장하는 테이블입니다.
- **주요 필드**:
    - `name`: 쿠폰 이름.
    - `discount_type`: 할인 유형 (`PERCENTAGE`, `FIXED_AMOUNT`).
    - `discount_value`: 할인 금액.
    - `minimum_order_amount`: 최소 주문 금액 조건.
    - `limit_count`: 발급 제한 수량.
    - `issued_count`: 현재 발급된 수량.
    - `valid_from`, `valid_until`: 쿠폰 사용 가능 기간.
- **활용**:
    - 주문에 할인 혜택을 제공합니다.

---

### 8. 사용자 쿠폰 (User Coupons)

- **설명**: 특정 사용자가 보유한 쿠폰 정보를 저장합니다.
- **주요 필드**:
    - `user_id`: 쿠폰을 보유한 사용자 ID.
    - `coupon_id`: 발급된 쿠폰 ID.
    - `status`: 쿠폰 상태 (`APPLIED`, `CANCELLED` 등).
    - `used`: 사용 여부.
    - `used_at`: 사용 시점.
- **관계**:
    - `user_id`는 `user` 테이블의 `id`를 참조합니다.
    - `coupon_id`는 `coupons` 테이블의 `id`를 참조합니다.
    - `payment_id`는 `payments` 테이블의 `id`를 참조합니다.
- **활용**:
    - 사용자별 쿠폰 사용 및 상태 관리를 지원합니다.

---

### 9. 상품 재고 (Product Inventory)

- **설명**: 상품의 현재 재고 상태를 관리합니다.
- **주요 필드**:
    - `product_id`: 상품 ID.
    - `stock`: 현재 재고 수량.
    - `last_updated`: 재고 마지막 업데이트 시간.
- **관계**:
    - `product_id`는 `product` 테이블의 `id`를 참조합니다.
- **활용**:
    - 주문 시 재고 차감 및 상품 관리에 활용됩니다.

---

### 10. 판매 통계 (Product Sales)

- **설명**: 상품의 판매량을 집계한 테이블입니다.
- **주요 필드**:
    - `product_id`: 상품 ID.
    - `aggregated_date`: 집계 날짜.
    - `sold_quantity`: 판매된 수량.
    - `last_updated`: 마지막 업데이트 시간.
- **관계**:
    - `product_id`는 `product` 테이블의 `id`를 참조합니다.
- **활용**:
    - 판매량 분석 및 상위 상품 조회 기능에 사용됩니다.

---
