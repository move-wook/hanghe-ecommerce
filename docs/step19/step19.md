# STEP 19

## 부하테스트

구현한 기능은 포인트 충전/조회, 상품조회, 상위상품조회, 주문 요청/조회, 주문 결제요청, 쿠폰조회/발급이 있었다.

주문요청을 부하테스트로 선정한 이유

주문요청은 결제전에 가장 많이 발생 할 수 있는 트랜잭션 중 하나이며, 트래픽이 가장 몰릴 수 있는 기능이다. 결제로 이어지는 핵심 프로세스이며 주문 요청이 발생하면 결제율이 높아질 수 있기 때문에 그만큼 핵심 비즈니스라고 생각된다.

순간적으로 트래픽이 올 수 있는 상황

- 프로모션 및 할인 이벤트 (블랙프라이데이, 파격세일, 연말세일)
- 인기상품 출시 ( 한정판 상품, 사전예약)

부하테스트의 목적은 실제 운영환경에서 발생 할 수 있는 트래픽을 견딜 수 있는지 평가하는 테스트이고 검증하고 성능 병목을 찾아 개선해야하기 때문이다.

# 부하 테스트 시나리오

운영 환경에서 발생할 수 있는 다양한 트래픽 패턴을 반영하여 정상 트래픽, 피크 트래픽, 부하 및 스트레스 테스트, 내구성 테스트의 4가지 유형을 수행하려고 한다.

## **Normal Traffic (정상 트래픽 시나리오)**

**목적:**

- 하루 중 일반적인 시간대에 발생하는 트래픽 패턴을 시뮬레이션
- 시스템이 기본적인 트래픽을 정상적으로 처리할 수 있는지 검증

**예시:**

- 1초에 1건의 주문 요청 발생
- 총 10초 동안 15명의 가상 사용자가 동시 접속

**설정:**

- **고정된 사용자 수 (`constant-vus`)**
- **15명의 가상 사용자 (`vus: 15`)**
- **10초 동안 지속 (`duration: "10s"`)**

```jsx
normal_traffic: {
  executor: 'constant-vus',
  vus: 15,  // 기존 10명 → 15명 (50% 증가)
  duration: "10s",  // 10초 동안 유지
},
```

---

## **Peak Traffic (피크 트래픽 시나리오)**

**목적:**

- 특정 시간대(예: 할인 행사, 이벤트 등)에서 발생하는 높은 트래픽을 시뮬레이션
- 서버가 급격히 증가하는 트래픽을 견딜 수 있는지 확인

**예시:**

- 특정 시간대에 1초당 100건 이상의 주문 요청 발생
- 10초 동안 750 TPS, 20초 후 1500 TPS, 이후 최대 3000 TPS까지 증가

**설정:**

- **도달 속도 증가 (`ramping-arrival-rate`)**
- **초기 요청 속도: `75 TPS` 시작**
- **최대 `3000 TPS`까지 증가**

```jsx
peak_traffic: {
  executor: 'ramping-arrival-rate',  // 요청 도달 속도 증가
  startRate: 75,  // 기존 50 TPS → **75 TPS** (50% 증가)
  timeUnit: '1s',
  preAllocatedVUs: 75,  // 기존 50 → 75으로 증가
  stages: [
    { duration: '10s', target: 750 },  // 기존 500 → **750 TPS**
    { duration: '20s', target: 1500 },  // 기존 1000 → **1500 TPS**
    { duration: '20s', target: 3000 },  // 기존 2000 → **3000 TPS**
    { duration: '10s', target: 0 },  // 점진적 감소
  ],
},
```

---

## **Stress Test (부하 및 스트레스 테스트)**

**목적:**

- 시스템의 한계를 확인하기 위해 점진적으로 부하를 증가
- 특정 임계값에서 성능 저하 또는 장애가 발생하는지 확인

**예시:**

- 동시 사용자 수를 점진적으로 증가
- 10초 동안 1500명, 20초 후 3000명, 최대 6000명까지 증가

**설정:**

- **점진적인 부하 증가 (`ramping-vus`)**
- **초기 사용자: `150 VUs` → 최대 `6000 VUs` 증가**

```jsx
stress_test: {
  executor: 'ramping-vus',  // 점진적 VU 증가
  startVUs: 150,  // 기존 100 VUs → **150 VUs** (50% 증가)
  stages: [
    { duration: '10s', target: 1500 },  // 기존 1000 VUs → **1500 VUs**
    { duration: '20s', target: 3000 },  // 기존 2000 VUs → **3000 VUs**
    { duration: '20s', target: 6000 },  // 기존 4000 VUs → **6000 VUs**
    { duration: '10s', target: 0 },  // 점진적 감소
  ],
},
```

---

## **Endurance Test (내구성 테스트)**

**목적:**

- 장시간 동안 지속되는 부하에서 시스템의 안정성을 평가
- **메모리 누수, CPU 과부하, DB 커넥션 부족 등 장기적인 문제를 탐색**

**예시:**

- **30분 동안 500~750 TPS 유지**
- 장기적인 부하에서도 서버가 안정적으로 운영되는지 확인

**설정:**

- **고정된 요청 속도 (`constant-arrival-rate`)**
- **30분 동안 `500 TPS` 유지**

```jsx
endurance_test: {
  executor: 'constant-arrival-rate',
  rate: 500, // 기존 500 → **750 TPS 유지**
  timeUnit: '1s',
  duration: '30m', // 30분 동안 지속
  preAllocatedVUs: 1500, // 기존 1000 → **1500**
  maxVUs: 3000, // 기존 2000 → **3000**
},

```

---

```jsx

export const options = {
  scenarios: {
    //Normal Traffic (일반 트래픽) - 기존 10 VUs → 15 VUs
    normal_traffic: {
      executor: 'constant-vus',
      vus: 15,  // 기존 10명 → 15명 (50% 증가)
      duration: "10s",  // 10초 동안 유지
    },

    //Peak Traffic (피크 트래픽) - 기존 500 → 2000 TPS → **750 → 3000 TPS로 증가**
    peak_traffic: {
      executor: 'ramping-arrival-rate',  // 요청 도달 속도 증가
      startRate: 75,  // 기존 50 TPS → **75 TPS** (50% 증가)
      timeUnit: '1s',
      preAllocatedVUs: 75,  // 기존 50 → 75으로 증가
      stages: [
        { duration: '10s', target: 750 },  // 기존 500 → **750 TPS**
        { duration: '20s', target: 1500 },  // 기존 1000 → **1500 TPS**
        { duration: '20s', target: 3000 },  // 기존 2000 → **3000 TPS**
        { duration: '10s', target: 0 },  // 점진적 감소
      ],
    },

    //Stress Test (스트레스 테스트) - 기존 1000 → 4000 VUs → **1500 → 6000 VUs로 증가**
    stress_test: {
      executor: 'ramping-vus',  // 점진적 VU 증가
      startVUs: 150,  // 기존 100 VUs → **150 VUs** (50% 증가)
      stages: [
        { duration: '10s', target: 1500 },  // 기존 1000 VUs → **1500 VUs**
        { duration: '20s', target: 3000 },  // 기존 2000 VUs → **3000 VUs**
        { duration: '20s', target: 6000 },  // 기존 4000 VUs → **6000 VUs**
        { duration: '10s', target: 0 },  // 점진적 감소
      ],
    },

    //Endurance Test (내구성 테스트) - 기존 없음 → **30분 동안 750 TPS 유지**
    endurance_test: {
      executor: 'constant-arrival-rate',
      rate: 750, // 기존 500 
      timeUnit: '1s',
      duration: '50m', // 30분 동안 지속
      preAllocatedVUs: 1000, // 기존 1000 \
      maxVUs: 2000, // 기존 2000 
    },
  },
};
```

---
[](https://private-user-images.githubusercontent.com/52989474/417654479-d0978c96-c774-4acd-9c73-ba5b5e76a2bc.png?jwt=eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJpc3MiOiJnaXRodWIuY29tIiwiYXVkIjoicmF3LmdpdGh1YnVzZXJjb250ZW50LmNvbSIsImtleSI6ImtleTUiLCJleHAiOjE3NDA2NjkxMjMsIm5iZiI6MTc0MDY2ODgyMywicGF0aCI6Ii81Mjk4OTQ3NC80MTc2NTQ0NzktZDA5NzhjOTYtYzc3NC00YWNkLTljNzMtYmE1YjVlNzZhMmJjLnBuZz9YLUFtei1BbGdvcml0aG09QVdTNC1ITUFDLVNIQTI1NiZYLUFtei1DcmVkZW50aWFsPUFLSUFWQ09EWUxTQTUzUFFLNFpBJTJGMjAyNTAyMjclMkZ1cy1lYXN0LTElMkZzMyUyRmF3czRfcmVxdWVzdCZYLUFtei1EYXRlPTIwMjUwMjI3VDE1MDcwM1omWC1BbXotRXhwaXJlcz0zMDAmWC1BbXotU2lnbmF0dXJlPWI4MWUzNGEzYTcwZDEwNWJhZjE4YjAxYzI3YmUzMjE4MTJjMjlmODE0MDk0MzVkYjEzNzJkNmFhNzA0ZjNlYTcmWC1BbXotU2lnbmVkSGVhZGVycz1ob3N0In0.8WcCO7RkLYLOfCzAjd_V0p0GlXZFXwPq3p3Gm4BkDDg)
### **주요 성능 지표**

| **항목** | **설명** | **결과 값** |
| --- | --- | --- |
| **총 요청 수 (http_reqs)** | 서버가 받은 총 요청 개수 | **56,277건** |
| **성공한 요청 수 (checks)** | 정상적으로 처리된 요청 개수 | **56,277건 (100.00%)** |
| **실패한 요청 수 (http_req_failed)** | 처리 실패한 요청 개수 | **0건 (0.00%)** |
| **평균 응답 시간 (http_req_duration)** | 요청이 처리되는 데 걸린 평균 시간 | **13.47초** |
| **최대 응답 시간** | 가장 오래 걸린 요청 처리 시간 | **46.25초** |
| **최소 응답 시간** | 가장 빠르게 처리된 요청 시간 | **214.5ms** |
| **데이터 수신량 (data_received)** | 서버가 클라이언트로부터 받은 총 데이터 크기 | **16MB** (51kB/s) |
| **데이터 전송량 (data_sent)** | 서버가 클라이언트에게 보낸 총 데이터 크기 | **13MB** (41kB/s) |
| **최대 동시 사용자 수 (vus_max)** | 서버가 동시에 처리한 최대 사용자 수 | **8075명** |

## **문제점 및 개선 사항**

### **성능 이슈**

- 요청 성공률은 100%이지만, 평균 응답 시간이 너무 김 (13.47초)
- 일부 요청은 40초 이상 걸림 → 병목 현상 발생 가능성
- 동시 사용자 8000명 이상에서 성능 저하 발생 가능성

### **해결 방안**

**서버 응답 시간 개선**

- **비효율적인 데이터베이스 쿼리 개선 (인덱스 추가, 조인 최적화)**
- **Redis 캐싱 적용으로 자주 요청되는 데이터 빠르게 제공**
- **API 응답 구조 단순화하여 불필요한 데이터 조회 최소화**

**부하 분산 및 서버 확장**

- **로드 밸런서를 사용하여 트래픽을 여러 서버로 분산**
- **트래픽이 많은 시간대에는 자동으로 서버 확장 (Auto Scaling) 적용**

**데이터베이스 성능 최적화**

- **DB Connection Pool 크기 증가 (ex: HikariCP maxPoolSize 조정)**
- **Long-running 트랜잭션 분석 후 최적화 (commit/rollback 즉시 처리)**
- **Slow Query 로그 분석 후 쿼리 튜닝**