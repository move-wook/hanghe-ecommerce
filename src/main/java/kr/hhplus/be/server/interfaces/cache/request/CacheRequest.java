package kr.hhplus.be.server.interfaces.cache.request;

import kr.hhplus.be.server.application.cache.request.CacheInfo;

public class CacheRequest {
    public record CacheRegisterV1(String key){
        public static CacheInfo.CacheRegisterV1 from(CacheRegisterV1 request) {
            return new CacheInfo.CacheRegisterV1(request.key());
        }
    }
}
