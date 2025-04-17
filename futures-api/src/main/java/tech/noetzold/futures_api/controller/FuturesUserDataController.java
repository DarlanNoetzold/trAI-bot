// FuturesUserDataController.java
package tech.noetzold.futures_api.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tech.noetzold.futures_api.service.FuturesUserDataService;

@RestController
@RequestMapping("/api/futures/userdata")
@RequiredArgsConstructor
@Slf4j
public class FuturesUserDataController {

    private final FuturesUserDataService futuresUserDataService;

    @PostMapping("/listenKey")
    public ResponseEntity<?> getListenKey() {
        log.info("POST /api/futures/userdata/listenKey called");
        return ResponseEntity.ok(futuresUserDataService.createListenKey());
    }

    @PutMapping("/listenKey")
    public ResponseEntity<?> keepAliveListenKey(@RequestParam String listenKey) {
        log.info("PUT /api/futures/userdata/listenKey called with listenKey={}", listenKey);
        return ResponseEntity.ok(futuresUserDataService.keepAliveListenKey(listenKey));
    }

    @DeleteMapping("/listenKey")
    public ResponseEntity<?> deleteListenKey(@RequestParam String listenKey) {
        log.info("DELETE /api/futures/userdata/listenKey called with listenKey={}", listenKey);
        return ResponseEntity.ok(futuresUserDataService.deleteListenKey(listenKey));
    }
}
