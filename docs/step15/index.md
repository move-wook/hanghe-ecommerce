# **MySQL 인덱스(Index) 개념 정리**

---

## **1. 인덱스(Index)란?**
인덱스(Index)는 **데이터베이스에서 검색 속도를 향상시키기 위한 자료구조**이다.
- 도서관에서 책을 찾을 때 목차(인덱스)를 이용하는 원리와 같음.
- 테이블의 특정 컬럼에 인덱스를 생성하면, 해당 컬럼을 기준으로 데이터를 빠르게 조회할 수 있음.
- MySQL에서는 **B-Tree, Hash** 등의 자료구조를 사용하여 인덱스를 관리함.
- 인덱스가 없으면 전체 데이터를 하나씩 검색해야 하지만, 인덱스를 사용하면 특정 값을 빠르게 찾을 수 있음.
---

## **2. 인덱스의 종류**
### **1) 기본 인덱스 유형**
MySQL에서 사용되는 주요 인덱스 유형은 다음과 같다.

| 인덱스 종류 | 설명 |
|------------|------|
| **PRIMARY KEY** | 기본 키 인덱스로, 중복을 허용하지 않으며 자동 정렬 |
| **UNIQUE INDEX** | 고유한 값만 저장할 수 있는 인덱스 |
| **FULLTEXT INDEX** | 대량의 텍스트 데이터에서 빠르게 검색할 수 있도록 도와주는 인덱스 |
| **SPATIAL INDEX** | 공간(지리 정보) 데이터를 빠르게 검색하기 위한 인덱스 |

✅ **`PRIMARY KEY`는 자동으로 인덱스가 생성되며, `UNIQUE INDEX`를 사용하면 중복값을 방지할 수 있음.**

---

### **2) 단일 인덱스 vs. 복합 인덱스**
인덱스는 **단일 인덱스(Single Index)** 와 **복합 인덱스(Composite Index)** 로 나뉜다.

#### ✅ **단일 인덱스(Single Index)**
- 하나의 컬럼에 대해 인덱스를 적용하는 방식.
- 특정 컬럼을 기준으로 검색 속도를 향상시킬 수 있음.
- 주로 `WHERE` 절에서 단일 컬럼으로 자주 검색할 때 사용됨.

#### ✅ **복합 인덱스(Composite Index)**
- 두 개 이상의 컬럼을 결합하여 하나의 인덱스로 관리하는 방식.
- **자주 함께 조회되는 컬럼을 조합하여 검색 성능을 최적화할 수 있음.**
- **주의할 점: 컬럼의 순서에 따라 인덱스의 효율성이 달라질 수 있음.**
    - 예를 들어 `(user_id, created_at)` 순서로 설정된 복합 인덱스는 **user_id를 먼저 검색한 후 created_at을 필터링할 때 효과적**임.
    - 하지만 `created_at` 단독 검색에는 인덱스가 효율적으로 사용되지 않을 수 있음.

✅ **복합 인덱스는 특정 컬럼을 우선 검색할 때만 효과적이며, 컬럼 순서가 중요하다.**
---
## **3. 인덱스를 사용하는 이유**
### ✅ **인덱스의 장점**
| 장점 | 설명 |
|------|------|
| **검색 속도 향상** | 데이터를 빠르게 조회할 수 있음 |
| **정렬 속도 향상** | `ORDER BY` 사용 시 정렬 속도 증가 |
| **JOIN 최적화** | 여러 테이블을 조인할 때 성능 향상 |

✅ **인덱스를 사용하면 조회 성능이 획기적으로 개선될 수 있음.**

---

### ❌ **인덱스의 단점**
| 단점 | 설명 |
|------|------|
| **쓰기 성능 저하** | `INSERT`, `UPDATE`, `DELETE` 시 인덱스도 함께 갱신해야 하므로 성능 저하 발생 |
| **디스크 공간 증가** | 인덱스가 저장되기 때문에 추가적인 공간이 필요함 |
| **잘못 사용 시 성능 저하** | 과도한 인덱스 사용은 오히려 쿼리 성능을 낮출 수 있음 |

✅ **인덱스를 무조건 많이 만들면 좋은 것이 아니라, 필요한 경우에만 최적화하여 사용해야 함.**

---

## **4. 언제 인덱스를 사용해야 할까?**
### ✅ **인덱스 사용이 적절한 경우**
✔ `WHERE` 절에서 자주 검색하는 컬럼  
✔ `JOIN`에서 연결되는 컬럼  
✔ `ORDER BY`, `GROUP BY` 연산이 자주 사용되는 컬럼

✅ **자주 검색되는 컬럼이나 정렬이 필요한 컬럼에 인덱스를 걸면 성능이 향상됨.**

---

### 🚨 **인덱스를 사용하면 안 되는 경우**
❌ 테이블의 데이터 개수가 적을 때 (전체 검색이 더 빠를 수도 있음)  
❌ 자주 변경되는 컬럼 (`UPDATE`, `DELETE`가 많을 경우 오버헤드 발생)  
❌ 중복 값이 많은 컬럼 (`LOW CARDINALITY`, 예: 성별 컬럼)

✅ **인덱스가 모든 경우에 최적이진 않으며, 적절한 경우에만 사용해야 함.**

---

## 🎯 **결론**
- **인덱스는 검색 속도를 향상시키지만, 관리 비용이 있음.**
- **잘못된 인덱스 사용은 오히려 성능 저하를 초래할 수 있음.**
- **인덱스가 필요한 경우와 불필요한 경우를 구분하여 최적화해야 함.**

## 인덱스 적용 시나리오 (상위 상품 조회)
## 다른 단순 시나리오에 비해 조인쿼리등을 사용하고 있어서 적절하다고 판단

## 주문300만, 주문아이템 테이블에 각 800만건에 더미 데이터를 넣어서 테스트를 진행합니다.
## 각 로컬 환경은 다르 생각하여 많은 더미데이터로 하려하였으나 부하가 심해 소량에 데이터 진행하게 되었습니디
```sql
Limit: 5 row(s)  (actual time=10988..10988 rows=0 loops=1)
Sort: total_sold DESC, limit input to 5 row(s) per chunk  (actual time=10988..10988 rows=0 loops=1)
        -> Table scan on <temporary>  (actual time=10988..10988 rows=0 loops=1)
            -> Aggregate using temporary table  (actual time=10988..10988 rows=0 loops=1)
                -> Nested loop inner join  (cost=9.78e+6 rows=388606) (actual time=10988..10988 rows=0 loops=1)
                    -> Nested loop inner join  (cost=9.35e+6 rows=388606) (actual time=10988..10988 rows=0 loops=1)
                        -> Table scan on oi  (cost=801570 rows=7.77e+6) (actual time=2.67..2391 rows=8e+6 loops=1)
                        -> Filter: ((o.`status` = 'COMPLETED') and (o.created_at between <cache>((now() - interval 3 day)) and <cache>(now())))  (cost=1 rows=0.05) (actual time=1e-3..1e-3 rows=0 loops=8e+6)
                            -> Single-row index lookup on o using PRIMARY (id=oi.order_id)  (cost=1 rows=1) (actual time=911e-6..911e-6 rows=0 loops=8e+6)
                    -> Single-row index lookup on p using PRIMARY (id=oi.product_id)  (cost=1 rows=1) (never executed)

```
### ❌ 문제점 분석
## ✅ **주요 문제점**
## **1️⃣ `order_item (oi)` 테이블에서 테이블 풀 스캔 발생**
- `Table scan on oi  (cost=801570 rows=7.77e+6)` → **`order_item` 테이블이 800만 건 이상 스캔됨**
- **원인:**
    - `order_id` 또는 `product_id` 인덱스가 적절히 활용되지 않음
    - `oi.order_id = o.id` 조인 시 `Nested Loop Join`이 발생하면서 800만 건을 모두 탐색
**🔹 해결책**  
    - order_item 테이블에서 order_id로 필터링 후, product_id로 그룹화해야 함.
    - 하지만 order_id와 product_id를 단독 인덱스로 관리하면 조인 최적화가 어렵고, MySQL이 효율적인 실행 계획을 선택하지 않을 수도 있음.
    - 해결책: (order_id, product_id) 복합 인덱스를 추가하여 두 필드를 한 번에 필터링할 수 있도록 최적화.

## 위와 같은 문제점에 대한 해결책으로 인덱스를 적용 후 성능 테스트 
1. CREATE INDEX idx_order_item_order_product ON order_item(order_id, product_id);
```sql
-> Limit: 5 row(s)  (actual time=638..638 rows=0 loops=1)
    -> Sort: total_sold DESC, limit input to 5 row(s) per chunk  (actual time=638..638 rows=0 loops=1)
        -> Table scan on <temporary>  (actual time=638..638 rows=0 loops=1)
            -> Aggregate using temporary table  (actual time=638..638 rows=0 loops=1)
                -> Nested loop inner join  (cost=726367 rows=296808) (actual time=638..638 rows=0 loops=1)
                    -> Nested loop inner join  (cost=399895 rows=296808) (actual time=638..638 rows=0 loops=1)
                        -> Filter: ((o.`status` = 'COMPLETED') and (o.created_at between <cache>((now() - interval 3 day)) and <cache>(now())))  (cost=73407 rows=36928) (actual time=0.883..287 rows=300595 loops=1)
                            -> Table scan on o  (cost=73407 rows=997152) (actual time=0.826..196 rows=1e+6 loops=1)
                        -> Index lookup on oi using idx_order_item_order_product (order_id=o.id)  (cost=8.04 rows=8.04) (actual time=0.0011..0.0011 rows=0 loops=300595)
                    -> Single-row index lookup on p using PRIMARY (id=oi.product_id)  (cost=1 rows=1) (never executed)

```
## 그러면 뭐가 달라진걸까?
## ✅ 결론 및 최적화 효과
## ✅ **실행 계획 분석 (복합 인덱스 적용 후)**
### **기존 실행 계획 (`order_item` 인덱스 없을 때)**
| 실행 단계 | 기존 실행 시간 |
|-----------|--------------|
| **총 실행 시간** | **10,988ms** |
| `order` 테이블 필터링 | **3,496ms** |
| `order_item` 조인 | **8M rows 전체 스캔 (2391ms)** |

### **복합 인덱스(`order_id, product_id`) 적용 후**
| 실행 단계 | 변경된 실행 시간 | 성능 향상 |
|-----------|--------------|------------|
| **총 실행 시간** | **638ms** | ⏬ **94% 속도 향상** |
| `order` 테이블 필터링 | **287ms** | ⏬ 91% 감소 |
| `order_item` 테이블 조인 | **0.0011ms per loop (300K rows 조회)** | ⏬ 99% 감소 |

### 🔹 **개선된 이유**
✔ **가장 큰 차이점:** `order_item` 테이블의 **복합 인덱스(`order_id, product_id`)** 적용  
✔ **기존 문제:** `order_item`이 800만 건을 전체 스캔 → 복합 인덱스 적용 후, `order_id`를 기반으로 빠르게 필터링  
✔ **결과:** `order` 테이블에서 `status = 'COMPLETED'`를 필터링할 때, `order_item`에서 **불필요한 데이터 조회를 제거**하여 조인 성능이 대폭 향상

---
✔ **복합 인덱스(`order_id, product_id`)를 추가한 후:**
- `order_item`에서 **필요한 `order_id`만 조회**
- **조인 전에 불필요한 데이터를 제거하여 `order` 테이블에서 처리할 데이터가 크게 줄어듦**

💡 **즉, `created_at, status` 인덱스를 사용하지 않았어도 `order_item`의 조회 범위가 줄어들면서 성능이 향상됨!**

---

### **2️⃣ `order_id`가 이미 `PRIMARY KEY`이므로 인덱스 최적화가 자동으로 발생**
- `order_id`는 **`PRIMARY KEY`이므로 MySQL이 자동으로 인덱스를 활용**
- `order_item.order_id = order.id` 조인 시, **`order_id`를 기반으로 먼저 필터링**
- **기존 문제:**
    - `order_item`에서 모든 데이터를 가져와 `order_id`를 비교
    - `order` 테이블에서 다시 `status = 'COMPLETED'` 조건을 확인
- **해결 후:**
    - `order_item`에서 `order_id`를 먼저 필터링
    - `order` 테이블에서 **불필요한 row 조회를 제거하여 성능 향상**
## ✅ **결론**
| 항목 | 기존 문제 | 복합 인덱스(`order_id, product_id`) 추가 후 개선 효과 |
|------|---------|------------------|
| **`order` 테이블 필터링 속도** | `status` 및 `created_at` 조건이 적용된 후 필터링 | ✅ `order_id`를 먼저 필터링하면서 불필요한 조회 감소 |
| **`order_item` 조인 속도** | `order_id` 단일 인덱스로는 전체 조회 | ✅ `order_id` 복합 인덱스를 활용하여 조회 범위 최소화 |
| **총 실행 시간** | **10,988ms** | ✅ **638ms (94% 성능 향상)** |
### 성능을 더 올릴 순 없을 까?
✔ **추가 인덱스 생성 (선택적 최적화)**
```sql
-> Limit: 5 row(s)  (actual time=10074..10074 rows=0 loops=1)
    -> Sort: total_sold DESC, limit input to 5 row(s) per chunk  (actual time=10074..10074 rows=0 loops=1)
        -> Table scan on <temporary>  (actual time=10074..10074 rows=0 loops=1)
            -> Aggregate using temporary table  (actual time=10074..10074 rows=0 loops=1)
                -> Nested loop inner join  (cost=7.8e+6 rows=3.89e+6) (actual time=10074..10074 rows=0 loops=1)
                    -> Nested loop inner join  (cost=3.53e+6 rows=3.89e+6) (actual time=10074..10074 rows=0 loops=1)
                        -> Table scan on oi  (cost=805054 rows=7.77e+6) (actual time=1.71..2154 rows=8e+6 loops=1)
                        -> Filter: ((o.`status` = 'COMPLETED') and (o.created_at between <cache>((now() - interval 3 day)) and <cache>(now())))  (cost=0.25 rows=0.5) (actual time=919e-6..919e-6 rows=0 loops=8e+6)
                            -> Single-row index lookup on o using PRIMARY (id=oi.order_id)  (cost=0.25 rows=1) (actual time=833e-6..833e-6 rows=0 loops=8e+6)
                    -> Single-row index lookup on p using PRIMARY (id=oi.product_id)  (cost=1 rows=1) (never executed)
```
1. CREATE INDEX idx_order_status_created_at ON orders(status, created_at);

# 🔍 **`order` 테이블에 인덱스를 적용했는데 더 오래 걸리는 이유 분석**

## ✅ **비교 전후 실행 시간**
| 실행 단계 | 인덱스 적용 전 | 인덱스 적용 후 | 변화 |
|-----------|--------------|--------------|--------|
| **총 실행 시간** | **6,38ms** | **10,074ms** | ❌ 58% 증가 |
| `order_item` 테이블 스캔 | **300K rows 조회** | **8M rows 전체 조회** | ❌ 비효율적 조회 발생 |
| `order` 테이블 필터링 | **287ms** | **919µs per row (8M loops)** | ❌ `Nested Loop Join` 증가 |
| `product` 테이블 조회 | **실행됨** | **(never executed)** | 🚨 실행되지 않음 |

---

## ✅ **왜 인덱스를 추가했는데 성능이 저하되었을까?**
### **1️⃣ `order` 테이블의 인덱스가 비효율적으로 사용됨**
- 실행 계획을 보면, `order` 테이블에서 **`status = 'COMPLETED'` 및 `created_at` 필터링**을 수행할 때,  
  **인덱스가 아닌 `Nested Loop Join`을 사용하여 8M번 루프가 발생**
```plaintext
-> Filter: ((o.status = 'COMPLETED') and (o.created_at between ...))  
-> Single-row index lookup on o using PRIMARY (id=oi.order_id) (cost=0.25 rows=1) (actual time=833µs..833µs rows=0 loops=8M)
```

# 🔍 **결론: 인덱스를 무조건 다 건다고 효율이 좋아지는 것은 아니다.**

## ✅ **분석 결과**
1️⃣ **`order` 테이블에 `created_at, status` 인덱스를 추가했을 때, 성능이 오히려 저하됨.**
- `order` 테이블에서 필터링이 빠르게 수행되지 않고, **불필요한 `Nested Loop Join`이 증가**
- `order_item` 테이블과 조인할 때, **조인 순서가 비효율적으로 적용되면서 전체 스캔 증가**
- `created_at`의 시간까지 비교하는 것이 인덱스 최적화에 오히려 방해가 될 가능성 있음

2️⃣ **`order_item` 테이블에서 `order_id, product_id` 복합 인덱스가 가장 효과적**
- **`order_id`를 기준으로 먼저 필터링한 후, `product_id`를 활용할 수 있도록 설계**
- **불필요한 `order_item` 전체 스캔을 방지하여, 쿼리 실행 속도가 가장 빠름**
- `GROUP BY product_id`도 인덱스에서 직접 처리되어 성능이 향상됨

3️⃣ **`product` 테이블 조인은 성능에 큰 영향을 미치지 않음**
- `product` 테이블을 조인할 필요가 없는 경우, MySQL이 조인을 생략
- **즉, `product_id`를 기준으로 정렬하는 과정이 최적화되면 실행 시간이 크게 단축됨**

---

## ✅ **최적의 인덱스 설정**
✔ **효율적인 인덱스:**
```sql
1. CREATE INDEX idx_order_item_order_product ON order_item(order_id, product_id);

```
##
✅ 최종 결론
모든 필드에 인덱스를 다 거는 것이 항상 성능을 향상시키지는 않는다.
오히려 조인 및 실행 순서에 따라 비효율적인 쿼리가 발생할 수도 있다.
가장 효과적인 인덱스는 order_item (order_id, product_id) 복합 인덱스만 적용하는 것.
created_at의 시간 비교까지 포함되면, 인덱스 활용도가 떨어질 가능성이 있음.
