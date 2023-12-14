package com.example.demo.auth;

import com.example.demo.user.User;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class SignInResponse {
    private User user;
    
    private String token;

    private String refreshToken;
}
