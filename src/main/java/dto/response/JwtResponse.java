package com.example.bookingSystem.dto.response;

import com.example.bookingSystem.model.enums.Role;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class JwtResponse {
    private String token;
    private Long userId;
    private String fullName;
    private String email;
    private Role role;
}
