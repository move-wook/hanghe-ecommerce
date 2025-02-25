import http from "k6/http";
import { sleep } from "k6";

export let options = {
  vus: 10,  // 동시에 실행할 가상 유저 수
  duration: "30s",  // 테스트 실행 시간
};

export default function () {
  let res = http.get("https://test.k6.io");

  // 응답 시간이 200ms 이하인지 확인
  if (res.timings.duration > 200) {
    console.error(`⛔ 응답 시간이 너무 느림! ${res.timings.duration}ms`);
  }

  sleep(1); // 1초 대기 후 다시 요청
}
