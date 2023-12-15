package com.example.demo.config;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.logout.LogoutHandler;
import org.springframework.stereotype.Service;

import com.example.demo.token.TokenRepository;
import com.example.demo.user.UserRepository;

@Service
@RequiredArgsConstructor
public class LogoutService implements LogoutHandler {

  private final TokenRepository tokenRepository;

  private final UserRepository userRepository;

  private final JwtService jwtService;

  @Override
  public void logout(
      HttpServletRequest request,
      HttpServletResponse response,
      Authentication authentication
  ) 
  {
    final String authHeader = request.getHeader("Authorization");
    final String jwt;

    if (authHeader == null ||!authHeader.startsWith("Bearer ")) {
      return;
    }

    jwt = authHeader.substring(7);
    var storedToken = tokenRepository.findByToken(jwt)
        .orElse(null);
    if (storedToken != null) {
        var refreshTokenStore = tokenRepository.findAllTokenByUser(storedToken.getUserId());
        tokenRepository.deleteAll(refreshTokenStore);
        SecurityContextHolder.clearContext();
    }

    //userEmail = jwtService.extractUsername(refreshToken);

    //tokenRepository.deleteTokenByEmail(email);
  }
}
