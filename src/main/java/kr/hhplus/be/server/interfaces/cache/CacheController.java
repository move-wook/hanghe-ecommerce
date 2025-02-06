package kr.hhplus.be.server.interfaces.cache;

import kr.hhplus.be.server.application.cache.CacheFacade;
import kr.hhplus.be.server.interfaces.cache.request.CacheRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1")
public class CacheController {

    private final CacheFacade cacheFacade;
    // 캐시 워밍을 위한 수동 API
    @PostMapping("/warm-up-cache")
    public ResponseEntity<String> warmUpCache(@RequestBody CacheRequest.CacheRegisterV1 request) {
        cacheFacade.warmUpCache(CacheRequest.CacheRegisterV1.from(request));
        return ResponseEntity.ok("Cache warming completed.");
    }
}
