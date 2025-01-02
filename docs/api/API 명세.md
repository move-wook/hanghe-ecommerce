# API 명세

## 목차

1. 잔액 충전
2. 잔액 조회
3. 상품 조회
4. 상위 상품 조회
5. 보유 쿠폰 조회
6. 쿠폰 발급
7. 주문 요청
8. 결제 요청

---

## 1. 잔액 충전

- **Endpoint**: `POST /api/v1/balance/charge`
- 사용자 잔액을 충전합니다.

### Request

- **Body**:
    
    ```json
    {
      "userId": 123,
      "amount": 10000
    }
    
    ```
    

### Response

- **HTTP 200 OK**:
    
    ```json
    {
      "status": "SUCCESS",
      "message": "잔액 충전에 성공했습니다.",
      "data": {
        "userId": 123,
        "currentBalance": 15000
      },
      "resultCode": "S000"
    }
    
    ```
    

### Error

- **400 Bad Request**:
    
    ```json
    {
      "status": "FAILURE",
      "resultCode": "E2xx",
      "message": "충전 금액이 유효하지 않습니다."
    }
    
    ```
    
- **204 NO_CONTENT**:
    
    ```json
    {
      "status": "FAILURE",
      "resultCode": "E1xx",
      "message": "사용자를 찾을 수 없습니다."
    }
    
    ```
    

---

## 2. 잔액 조회

- **Endpoint**: `GET /api/v1/balance/{userId}`
- 특정 사용자의 잔액을 조회합니다.

### Request

- **Path Parameter**:
    - `userId` (long, required): 조회할 사용자의 ID.

### Response

- **HTTP 200 OK**:
    
    ```json
    {
      "status": "SUCCESS",
      "data": {
        "userId": 123,
        "currentBalance": 15000
      },
      "resultCode": "S000"
    }
    ```
    

### Error

- **400 Bad Request**:
    
    ```json
    {
      "status": "FAILURE",
      "resultCode": "E101",
      "message": "유효하지 않은 사용자 요청입니다."
    }
    
    ```
    
- **204 NO_CONTENT**:
    
    ```json
    {
      "status": "FAILURE",
      "resultCode": "E1xx",
      "message": "사용자를 찾을 수 없습니다."
    }
    
    ```
    

---

## 3. 상품 조회

- **Endpoint**: `GET /api/v1/products/{productId}`
- 상품 목록을 조회합니다.

### Request

- **Path Parameter**:
    - `productId` (long, required): 조회할 상품 ID.

### Response

- **HTTP 200 OK**:
    
    ```json
    {
      "status": "SUCCESS",
      "data": [
        {
          "productId": 1,
          "name": "Product A",
          "price": 1000,
          "description": "상품 A 설명"
    	    "stock" : 3 //상품재고
        },
      ],
      "resultCode": "S000"
    }
    
    ```
    

### Error

- **400 Bad Request**:
    
    ```json
    {
      "status": "FAILURE",
      "resultCode": "E3xx",
      "message": "유효하지 않은 상품 요청입니다."
    }
    
    ```
    

---

## 4. 상위 상품 조회

- **Endpoint**: `GET /api/v1/products/top`
- 판매량 기준 상위 상품 5개 목록을 조회합니다.

### Response

- **HTTP 200 OK**:
    
    ```json
    {
      "status": "SUCCESS",
      "data": [
        {
          "productId": 1,
          "name": "Product A",
          "totalSold": 500
        },
        {
          "productId": 2,
          "name": "Product B",
          "totalSold": 300
        }
      ],
      "resultCode": "S000"
    }
    
    ```
    

### 

```jsx
{
  "status": "FAILURE",
  "resulCode": "INTERNAL_ERROR",
  "message": "상위 상품을 가져오는 데 실패했습니다."
}
```

---

## 5. 보유 쿠폰 조회

- **Endpoint**: `GET /api/v1/coupons/{userId}`
- 특정 사용자가 보유한 쿠폰 목록을 조회합니다.

### Request

- **Path Parameter**:
    - `userId` (long, required): 조회할 사용자의 ID.

### Response

- **HTTP 200 OK**:
    
    ```json
    {
      "status": "SUCCESS",
      "data": [
        {
          "couponId": 101,
          "name": "10% 할인 쿠폰",
          "status": "AVAILABLE",
          "validUntil": "2025-12-31"
        },
        {
          "couponId": 102,
          "name": "5000원 할인 쿠폰",
          "status": "AVAILABLE",
          "validUntil": "2025-12-31"
        }
      ],
      "resultCode": "S000"
    }
    
    ```
    

### Error

- **204 Not Found**:
    
    ```json
    {
      "status": "FAILURE",
      "resultCode": "E1xx",
      "message": "사용자를 찾을 수 없습니다."
    }
    
    ```
    

---

## 6. 쿠폰 발급

- **Endpoint**: `POST /api/v1/coupons/issue`
- 특정 사용자에게 쿠폰을 발급합니다.

### Request

- **Body**:
    
    ```json
    {
      "userId": 123,
      "couponId": 101
    }
    
    ```
    

### Response

- **HTTP 200 OK**:
    
    ```json
    {
      "status": "SUCCESS",
      "message": "쿠폰 발급에 성공했습니다.",
      "data": {
        "userId": 123,
        "couponId": 101
      },
      "resultCode": "S000"
    }
    
    ```
    

### Error

- **400 Bad Request**:
    
    ```json
    {
      "status": "FAILURE",
      "resultCode": "E4xx",
      "message": "유효하지 않은 쿠폰 요청입니다."
    }
    
    ```
    
- **204** NO_CONTENT:
    
    ```json
    {
      "status": "FAILURE",
      "resultCode": "E4xx",
      "message": "쿠폰에 재고가 없습니다."
    }
    
    ```
    

---

## 7. 주문 요청

- **Endpoint**: `POST /api/v1/orders`
- 특정 사용자의 주문을 생성합니다.

### Request

- **Body**:
    
    ```json
    {
      "userId": 123,
      "orderItems": [
        {
          "productId": 1,
          "quantity": 2
        },
        {
          "productId": 2,
          "quantity": 1
        }
      ],
      "couponId": 101
    }
    
    ```
    

### Response

- **HTTP 201 Created**:
    
    ```json
    {
      "status": "SUCCESS",
      "message": "주문 생성에 성공했습니다.",
      "data": {
        "orderId": 456,
        "totalPrice": 3000,
        "status": "PENDING"
      },
      "resultCode": "S000"
    }
    
    ```
    

### Error

- **400 Bad Request**:
    
    ```json
    {
      "status": "FAILURE",
      "resultCode": "E5xx",
      "message": "주문 정보가 유효하지 않습니다."
    }
    
    ```
    

---

## 8. 결제 요청

- **Endpoint**: `POST /api/v1/payments`
- 주문에 대한 결제를 요청합니다.

### Request

- **Body**:
    
    ```json
    {
      "orderId": 456,
      "userId": 123,
    }
    
    ```
    

### Response

- **HTTP 200 OK**:
    
    ```json
    {
      "status": "SUCCESS",
      "message": "결제에 성공했습니다.",
      "data": {
        "paymentId": 789,
        "orderId": 456,
        "amount": 2700,
        "status": "COMPLETED"
      },
      "resultCode": "S000"
    }
    
    ```
    

### Error

- **400 Bad Request**:
    
    ```json
    {
      "status": "FAILURE",
      "resultCode": "E6xx",
      "message": "결제 정보가 유효하지 않습니다."
    }
    
    ```
    

---

## 공통 응답 형식

```json
{
  "status": "SUCCESS" | "FAILURE",
  "message": "요청에 대한 응답 메시지",
  "resultCode": "SUCCESS | FAILURE | 기타 세부 코드",
  "data": { ... }
}

```

### resultCode

1. S000 : 성공
2. E1xx : 사용자 관련 에러코드
3. E2xx : 잔액관련 에러코드
4. E3xx: 상품관련 에러 코드
5. E4xx: 쿠폰관련 에러코드
6. E5xx: 주문관련 에러코드
7. E6xx: 결제관련 에러코드