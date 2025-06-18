package tech.noetzold.auth_api.service;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import tech.noetzold.auth_api.dto.*;
import tech.noetzold.auth_api.model.User;
import tech.noetzold.auth_api.repository.UserRepository;
import tech.noetzold.auth_api.util.JwtUtil;

import java.time.LocalDate;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;


    public UserResponseDTO getUserByEmail(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        UserResponseDTO dto = new UserResponseDTO();
        dto.setUsername(user.getUsername());
        dto.setEmail(user.getEmail());
        if (user.getBirthDate() != null && !user.getBirthDate().equalsIgnoreCase("") && !user.getBirthDate().equalsIgnoreCase("null")) {
            dto.setBirthDate(LocalDate.parse(user.getBirthDate()));
        }
        dto.setAddress(user.getAddress());
        dto.setTestnetApiKey(user.getTestnetApiKey());
        dto.setTestnetSecretKey(user.getTestnetSecretKey());
        dto.setProductionApiKey(user.getProductionApiKey());
        dto.setProductionSecretKey(user.getProductionSecretKey());
        dto.setWhatsappNumber(user.getWhatsappNumber());
        dto.setTelegramChatId(user.getTelegramChatId());
        dto.setWhatsappApiKey(user.getWhatsappApiKey());
        dto.setRole(user.getRoles().stream().findFirst().orElse("VIEWER"));

        return dto;
    }

    public void register(UserRegisterDTO dto) {
        if (userRepository.findByEmail(dto.getEmail()).isPresent()) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Email already registered");
        }

        if (dto.getRole() == null || dto.getRole().isBlank()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Role is required (ADMIN, TRADER or VIEWER)");
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

    public void updateUser(UserUpdateDTO dto, String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        if (dto.getUsername() != null) user.setUsername(dto.getUsername());
        if (dto.getEmail() != null && !dto.getEmail().equals(user.getEmail())) {
            if (userRepository.existsByEmail(dto.getEmail())) {
                throw new RuntimeException("Email already exists");
            }
            user.setEmail(dto.getEmail());
        }
        if (dto.getBirthDate() != null) user.setBirthDate(dto.getBirthDate().toString());
        if (dto.getAddress() != null) user.setAddress(dto.getAddress());
        if (dto.getTestnetApiKey() != null) user.setTestnetApiKey(dto.getTestnetApiKey());
        if (dto.getTestnetSecretKey() != null) user.setTestnetSecretKey(dto.getTestnetSecretKey());
        if (dto.getProductionApiKey() != null) user.setProductionApiKey(dto.getProductionApiKey());
        if (dto.getProductionSecretKey() != null) user.setProductionSecretKey(dto.getProductionSecretKey());
        if (dto.getWhatsappNumber() != null) user.setWhatsappNumber(dto.getWhatsappNumber());
        if (dto.getTelegramChatId() != null) user.setTelegramChatId(dto.getTelegramChatId());
        if (dto.getWhatsappApiKey() != null) user.setWhatsappApiKey(dto.getWhatsappApiKey());

        userRepository.save(user);
    }



    public AuthResponseDTO login(UserLoginDTO dto) {
        User user = userRepository.findByEmail(dto.getEmail())
                .orElseThrow(() -> new RuntimeException("Email não encontrado"));

        if (!passwordEncoder.matches(dto.getPassword(), user.getPassword())) {
            throw new RuntimeException("Senha incorreta");
        }

        String role = user.getRoles().stream().findFirst().orElse("VIEWER");
        return new AuthResponseDTO(jwtUtil.generateToken(user.getEmail(), user.getId(), role), user.getUsername(), role);
    }
}
