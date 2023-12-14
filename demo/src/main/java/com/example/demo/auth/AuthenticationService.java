package com.example.demo.auth;

import java.sql.Timestamp;
import java.util.Date;

import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.example.demo.config.JwtService;
import com.example.demo.token.Token;
import com.example.demo.token.TokenRepository;
import com.example.demo.user.User;
import com.example.demo.user.UserRepository;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AuthenticationService {

    private final UserRepository userRepository;

    private final PasswordEncoder passwordEncoder;

    private final JwtService jwtService;

    private final AuthenticationManager authenticationManager;

    private final TokenRepository tokenRepository;
    
    public AuthenticationResponse register(RegisterRequest request) {
        var user = User.builder()
            .firstname(request.getFirstname())
            .lastname(request.getLastname())
            .email(request.getEmail())
            .password(passwordEncoder.encode(request.getPassword()))
            .hash(passwordEncoder.encode(request.getPassword()))
            .updatedAt(new Date())
            .build();

        userRepository.save(user);

        var jwtToken = jwtService.generateToken((UserDetails) user);

        return AuthenticationResponse.builder()
            .token(jwtToken)
            .build();
    }

    public AuthenticationResponse authentication(AuthenticationRequest request) {
        authenticationManager.authenticate(
            new UsernamePasswordAuthenticationToken(
                request.getEmail(),
                request.getPassword())
        );

        var user = userRepository.findByEmail(request.getEmail())
                .orElseThrow();
        
        var jwtToken = jwtService.generateToken( user);

        return AuthenticationResponse.builder()
            .token(jwtToken)
            .build();
    }

    public SignInResponse signIn(SignInRequest request) {
        Date today = new Date();

        var user = userRepository.findByEmail(request.getEmail())
                .orElseThrow();
        var jwtToken = jwtService.generateToken(user);
        var jwtRefreshToken = jwtService.generateRefreshToken(user);
        var token = Token.builder()
                    .refreshToken(jwtRefreshToken)
                    .expiresIn(new Date(today.getTime() + (1000 * 60 * 60 * 24 * 30)))
                    .createdAt(today)
                    .updatedAt(today)
                    .build();

        tokenRepository.save(token);

        return SignInResponse.builder()
            .user((User)user)
            .refreshToken(jwtToken)
            .token(jwtRefreshToken)
            .build();
    }

    public RefreshTokenResponse refreshToken(RefreshTokenRequest request) {
       var user = userRepository.findUserByRefreshToken(request.getTokenRefersh()).get(0);
        if(user == null){
            throw new ResponseStatusException(
                HttpStatus.NOT_FOUND, "entity not found"
            );
        }

        var jwtToken = jwtService.generateToken(user);
        var jwtRefreshToken = jwtService.generateRefreshToken(user);
        
        return RefreshTokenResponse.builder()
                .token(jwtToken)
                .refreshToken(jwtRefreshToken)
                .build();
    }
    
}
