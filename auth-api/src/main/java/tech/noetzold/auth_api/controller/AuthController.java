package tech.noetzold.auth_api.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import tech.noetzold.auth_api.dto.*;
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

        if (dto.getRole() == null || dto.getRole().isBlank()) {
            return ResponseEntity.badRequest().body("Role is required (ADMIN, TRADER or VIEWER)");
        }

        authService.register(dto);

        auditService.log(
                null,
                dto.getEmail(),
                "REGISTER",
                "AUTH",
                "User registered with role: " + dto.getRole().toUpperCase()
        );

        return ResponseEntity.ok("User successfully registered");
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponseDTO> login(@RequestBody UserLoginDTO dto) {
        log.info("POST /api/auth/login called with email={}", dto.getEmail());

        AuthResponseDTO response = authService.login(dto);
        auditService.log(0L, dto.getEmail(), "LOGIN", "AUTH", "User logged in with email: " + dto.getEmail());

        return ResponseEntity.ok(response);
    }

    @PutMapping("/update")
    public ResponseEntity<?> updateUser(@RequestBody UserUpdateDTO dto, Authentication authentication) {
        log.info("PUT /api/auth/update called for user={}", authentication.getName());

        try {
            authService.updateUser(dto, authentication.getName());

            auditService.log(
                    null,
                    authentication.getName(),
                    "UPDATE",
                    "AUTH",
                    "User data updated"
            );

            return ResponseEntity.ok("User successfully updated");
        } catch (Exception e) {
            log.error("Error updating user: {}", e.getMessage());
            return ResponseEntity.badRequest().body("Error updating user: " + e.getMessage());
        }
    }

    @GetMapping("/me")
    public ResponseEntity<UserResponseDTO> getCurrentUser(Authentication authentication) {
        log.info("GET /api/auth/me called for user={}", authentication.getName());

        UserResponseDTO user = authService.getUserByEmail(authentication.getName());
        return ResponseEntity.ok(user);
    }
}
