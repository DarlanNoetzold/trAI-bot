package tech.noetzold.futures_api.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tech.noetzold.futures_api.service.FuturesUserDataService;

@RestController
@RequestMapping("/api/futures/userdata")
@RequiredArgsConstructor
public class FuturesUserDataController {

    private final FuturesUserDataService futuresUserDataService;

    @PostMapping("/listenKey")
    public ResponseEntity<?> getListenKey() {
        return ResponseEntity.ok(futuresUserDataService.createListenKey());
    }

    @PutMapping("/listenKey")
    public ResponseEntity<?> keepAliveListenKey(@RequestParam String listenKey) {
        return ResponseEntity.ok(futuresUserDataService.keepAliveListenKey(listenKey));
    }

    @DeleteMapping("/listenKey")
    public ResponseEntity<?> deleteListenKey(@RequestParam String listenKey) {
        return ResponseEntity.ok(futuresUserDataService.deleteListenKey(listenKey));
    }
}

