package com.example.demo.auth;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.example.demo.config.JwtService;
import com.example.demo.token.Token;
import com.example.demo.token.TokenRepository;
import com.example.demo.user.User;
import com.example.demo.user.UserRepository;

import io.jsonwebtoken.io.IOException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;


@Service
@RequiredArgsConstructor
public class AuthenticationService {

    private final UserRepository userRepository;

    private final PasswordEncoder passwordEncoder;

    private final JwtService jwtService;

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

    public SignInResponse signIn(SignInRequest request) {
        Date today = new Date();

        var user = userRepository.findByEmail(request.getEmail())
                .orElseThrow();

        var jwtToken = jwtService.generateToken(user);
        var jwtRefreshToken = jwtService.generateRefreshToken(user);
        Token token = Token.builder()
                    .refreshToken(jwtRefreshToken)
                    .expiresIn(new Date(today.getTime() + (1000 * 60 * 60 * 24 * 30)))
                    .createdAt(today)
                    .updatedAt(today)
                    .user(user)
                    .build();
        tokenRepository.save(token);

        return SignInResponse.builder()
            .user((User)user)
            .refreshToken(jwtToken)
            .token(jwtRefreshToken)
            .build();
    }

    public RefreshTokenResponse refreshToken(
            HttpServletRequest request,
            HttpServletResponse response
    ) throws IOException {
        final String authHeader = request.getHeader(HttpHeaders.AUTHORIZATION);
        final String refreshToken;
        final String userEmail;

        refreshToken = authHeader.substring(7);
        userEmail = jwtService.extractUsername(refreshToken);
        if (userEmail != null) {
            var user = this.userRepository.findByEmail(userEmail)
                    .orElseThrow();
            if (jwtService.isTokenValid(refreshToken, user)) {
                revokeAllUserTokens(user);
                var accessToken = jwtService.generateToken(user);
                var newRefreshToken = jwtService.generateRefreshToken(user);

                return RefreshTokenResponse.builder()
                    .token(accessToken)
                    .refreshToken(newRefreshToken)
                    .build();
            }
        }

        throw new ResponseStatusException(
            HttpStatus.NOT_FOUND, "Not found"
        );
    }

    private void revokeAllUserTokens(User user) {
        var validUserTokens = tokenRepository.findAllValidTokenByUser(user.getId());

        if (validUserTokens.isEmpty()) {
            return;
        }
          
        validUserTokens.forEach(token -> {
            Date today = new Date();
            Calendar cal = new GregorianCalendar();
            cal.setTime(today);
            cal.add(Calendar.DAY_OF_MONTH, -30);
            Date today30 = cal.getTime();
            token.setExpiresIn(today30);
        });

        tokenRepository.saveAll(validUserTokens);
      }

    
}
