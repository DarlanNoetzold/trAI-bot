package tech.noetzold.crypto_bot_backend.service;

import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import tech.noetzold.crypto_bot_backend.dto.AuthResponseDTO;
import tech.noetzold.crypto_bot_backend.dto.UserLoginDTO;
import tech.noetzold.crypto_bot_backend.dto.UserRegisterDTO;
import tech.noetzold.crypto_bot_backend.dto.UserRegistrationDTO;
import tech.noetzold.crypto_bot_backend.model.User;
import tech.noetzold.crypto_bot_backend.repository.UserRepository;
import tech.noetzold.crypto_bot_backend.util.JwtUtil;

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

        userRepository.save(user);
    }

    public AuthResponseDTO login(UserLoginDTO dto) {
        User user = userRepository.findByEmail(dto.getEmail())
                .orElseThrow(() -> new RuntimeException("Email não encontrado"));

        if (!passwordEncoder.matches(dto.getPassword(), user.getPassword())) {
            throw new RuntimeException("Senha incorreta");
        }

        String token = jwtUtil.generateToken(user.getEmail());
        return new AuthResponseDTO(token, user.getUsername());
    }
}
