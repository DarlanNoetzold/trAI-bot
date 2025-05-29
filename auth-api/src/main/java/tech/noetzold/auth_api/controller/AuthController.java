package tech.noetzold.auth_api.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tech.noetzold.auth_api.dto.AuthResponseDTO;
import tech.noetzold.auth_api.dto.UserLoginDTO;
import tech.noetzold.auth_api.dto.UserRegisterDTO;
import tech.noetzold.auth_api.service.AuthService;
import tech.noetzold.auth_api.service.AuditService;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
@Slf4j
public class AuthController {

    private final AuthService authService;
    private final AuditService auditService;

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody UserRegisterDTO dto) {
        log.info("POST /api/auth/register called with email={}, username={}", dto.getEmail(), dto.getUsername());

        authService.register(dto);
        auditService.log(null, dto.getEmail(), "REGISTER", "AUTH", "User registered with email: " + dto.getEmail());

        return ResponseEntity.ok("Usuário registrado com sucesso");
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponseDTO> login(@RequestBody UserLoginDTO dto) {
        log.info("POST /api/auth/login called with email={}", dto.getEmail());

        AuthResponseDTO response = authService.login(dto);
        auditService.log(0L, dto.getEmail(), "LOGIN", "AUTH", "User logged in with email: " + dto.getEmail());

        return ResponseEntity.ok(response);
    }
}
