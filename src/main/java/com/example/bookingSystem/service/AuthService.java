package com.example.bookingSystem.service;

import com.example.bookingSystem.config.JwtUtils;
import com.example.bookingSystem.dto.request.LoginRequest;
import com.example.bookingSystem.dto.request.RegisterRequest;
import com.example.bookingSystem.dto.response.JwtResponse;
import com.example.bookingSystem.model.entity.User;
import com.example.bookingSystem.model.enums.Role;
import com.example.bookingSystem.repository.UserRepository;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtils jwtUtils;
    private final AuthenticationManager authenticationManager;

    public AuthService(
            UserRepository userRepository,
            PasswordEncoder passwordEncoder,
            JwtUtils jwtUtils,
            AuthenticationManager authenticationManager
    ) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtUtils = jwtUtils;
        this.authenticationManager = authenticationManager;
    }

    public JwtResponse register(RegisterRequest request) {
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new RuntimeException("Email already exists");
        }

        User user = User.builder()
                .fullName(request.getFullName())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .role(Role.USER)
                .build();

        User saved = userRepository.save(user);
        // Add extra claims for frontend compatibility
        java.util.Map<String, Object> extraClaims = new java.util.HashMap<>();
        extraClaims.put("userId", saved.getId());
        extraClaims.put("role", saved.getRole().name());
        String token = jwtUtils.generateToken(extraClaims, saved);

        return JwtResponse.builder()
                .token(token)
                .userId(saved.getId())
                .fullName(saved.getFullName())
                .email(saved.getEmail())
                .role(saved.getRole())
                .build();
    }

    public JwtResponse login(LoginRequest request) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword())
        );

        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));

        // Add extra claims for frontend compatibility
        java.util.Map<String, Object> extraClaims = new java.util.HashMap<>();
        extraClaims.put("userId", user.getId());
        extraClaims.put("role", user.getRole().name());
        String token = jwtUtils.generateToken(extraClaims, user);

        return JwtResponse.builder()
                .token(token)
                .userId(user.getId())
                .fullName(user.getFullName())
                .email(user.getEmail())
                .role(user.getRole())
                .build();
    }
}
