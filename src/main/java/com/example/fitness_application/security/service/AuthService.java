package com.example.fitness_application.security.service;

import com.example.fitness_application.identity.token.Token;
import com.example.fitness_application.identity.token.TokenService;
import com.example.fitness_application.identity.user.dto.UserDTO;
import com.example.fitness_application.security.dto.AuthResponseDTO;
import com.example.fitness_application.security.dto.LoginRequestDTO;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

  private final AuthenticationManager authenticationManager;
  private final TokenService tokenService;

  public AuthService(AuthenticationManager authenticationManager, TokenService tokenService) {
    this.authenticationManager = authenticationManager;
    this.tokenService = tokenService;
  }

  public AuthResponseDTO login(LoginRequestDTO request) {
    Authentication authentication = tryToAuthenticate(new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword()));
    SecurityContextHolder.getContext().setAuthentication(authentication);

    UserDTO userDTO = (UserDTO) authentication.getPrincipal();

    Token token = (Token) authentication.getDetails();
    if (token == null)
      throw new InternalAuthenticationServiceException("Token are missing after authentication!");

    return new AuthResponseDTO(userDTO, token.getToken());
  }

  public void logout(Long userId) {
    tokenService.revokeAllUserTokens(userId);
  }

  private Authentication tryToAuthenticate(Authentication authenticationRequest) {
    Authentication responseAuthentication = authenticationManager.authenticate(authenticationRequest);

    if (!responseAuthentication.isAuthenticated())
      throw new InternalAuthenticationServiceException("Unable to authenticate User with provided credentials");

    return responseAuthentication;
  }
}
