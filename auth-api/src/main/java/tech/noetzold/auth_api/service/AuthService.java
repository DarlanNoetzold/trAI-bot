package tech.noetzold.auth_api.service;

import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import tech.noetzold.auth_api.dto.AuthResponseDTO;
import tech.noetzold.auth_api.dto.UserLoginDTO;
import tech.noetzold.auth_api.dto.UserRegisterDTO;
import tech.noetzold.auth_api.dto.UserRegistrationDTO;
import tech.noetzold.auth_api.model.User;
import tech.noetzold.auth_api.repository.UserRepository;
import tech.noetzold.auth_api.util.JwtUtil;

import java.util.Set;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;

    public void register(UserRegisterDTO dto) {
        if (userRepository.findByEmail(dto.getEmail()).isPresent()) {
            throw new RuntimeException("Email já cadastrado");
        }

        if (dto.getRole() == null || dto.getRole().isBlank()) {
            throw new IllegalArgumentException("Role is required (ADMIN, TRADER or VIEWER)");
        }

        User user = new User();
        user.setUsername(dto.getUsername());
        user.setEmail(dto.getEmail());
        user.setPassword(passwordEncoder.encode(dto.getPassword()));
        user.setBirthDate(String.valueOf(dto.getBirthDate()));
        user.setAddress(dto.getAddress());
        user.setTestnetApiKey(dto.getTestnetApiKey());
        user.setTestnetSecretKey(dto.getTestnetSecretKey());
        user.setProductionApiKey(dto.getProductionApiKey());
        user.setProductionSecretKey(dto.getProductionSecretKey());

        user.setRoles(Set.of(dto.getRole().toUpperCase()));

        userRepository.save(user);
    }


    public AuthResponseDTO login(UserLoginDTO dto) {
        User user = userRepository.findByEmail(dto.getEmail())
                .orElseThrow(() -> new RuntimeException("Email não encontrado"));

        if (!passwordEncoder.matches(dto.getPassword(), user.getPassword())) {
            throw new RuntimeException("Senha incorreta");
        }

        return new AuthResponseDTO(jwtUtil.generateToken(user.getEmail(), user.getId(), "user"), user.getUsername());
    }
}
