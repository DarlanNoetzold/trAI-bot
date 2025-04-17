package tech.noetzold.crypto_bot_backend.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import tech.noetzold.crypto_bot_backend.dto.AuthResponseDTO;
import tech.noetzold.crypto_bot_backend.dto.UserLoginDTO;
import tech.noetzold.crypto_bot_backend.dto.UserRegisterDTO;
import tech.noetzold.crypto_bot_backend.service.AuthService;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
@Slf4j
public class AuthController {

    private final AuthService authService;

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody UserRegisterDTO dto) {
        log.info("POST /api/auth/register called with email={}, username={}", dto.getEmail(), dto.getUsername());
        authService.register(dto);
        return ResponseEntity.ok("Usuário registrado com sucesso");
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponseDTO> login(@RequestBody UserLoginDTO dto) {
        log.info("POST /api/auth/login called with email={}", dto.getEmail());
        return ResponseEntity.ok(authService.login(dto));
    }
}
