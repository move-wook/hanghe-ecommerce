import http from 'k6/http';
import { check, sleep } from 'k6';

// 10,000명의 유저 ID 리스트 (id는 2부터 10001까지)
const users = Array.from({ length: 1000 }, (_, i) => i + 2);

export const options = {
  scenarios: {
    // ✅ 1️⃣ Normal Traffic (일반 트래픽) - 기존 10 VUs → 15 VUs
    normal_traffic: {
      executor: 'constant-vus',
      vus: 15,  // 기존 10명 → 15명 (50% 증가)
      duration: "10s",  // 10초 동안 유지
    },

    // 🔥 2️⃣ Peak Traffic (피크 트래픽) - 기존 500 → 2000 TPS → **750 → 3000 TPS로 증가**
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

    // 🚀 3️⃣ Stress Test (스트레스 테스트) - 기존 1000 → 4000 VUs → **1500 → 6000 VUs로 증가**
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

    // 💾 4️⃣ Endurance Test (내구성 테스트) - 기존 없음 → **30분 동안 750 TPS 유지**
    endurance_test: {
      executor: 'constant-arrival-rate',
      rate: 500, // 기존 500
      timeUnit: '1s',
      duration: '5m', // 30분 동안 지속
      preAllocatedVUs: 1000, // 기존 1000 → **1500**
      maxVUs: 2000, // 기존 2000 → **3000**
    },
  },
};
export default function () {
  let userId = users[Math.floor(Math.random() * users.length)]; // 랜덤 유저 선택
    const url = 'http://host.docker.internal:8080/api/v1/orders';
    const payload = JSON.stringify({
        userId: userId,
        orderItems: [
            {
                productId: 1,
                quantity: 1
            },
        ],
    });
    const params = {
        headers: {
            'Content-Type': 'application/json',
        },
    };
  const res = http.post(url, payload, params);
  check(res, {
  		'주문 요청': (r) => r.status === 200,
  	});

  sleep(Math.random() * 2);  // 0~2초 랜덤 대기 (부하 패턴 유사하게)
}
