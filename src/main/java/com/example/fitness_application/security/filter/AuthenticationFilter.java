package com.example.fitness_application.security.filter;

import com.example.fitness_application.common.exception.response.ErrorResponse;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.FilterChain;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.context.annotation.Lazy;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.preauth.PreAuthenticatedAuthenticationToken;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class AuthenticationFilter extends OncePerRequestFilter {

  private final AuthenticationManager authenticationManager;

  public AuthenticationFilter(@Lazy AuthenticationManager authenticationManager) {
    this.authenticationManager = authenticationManager;
  }

  @Override
  protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws IOException {
    final String token = request.getHeader(HttpHeaders.AUTHORIZATION);

    try {
      if (token != null && token.startsWith("Bearer ")) {
        String tokenWithoutBearer = token.substring(7);
        processTokenAuthentication(tokenWithoutBearer);
      }

      filterChain.doFilter(request, response);

    } catch (AuthenticationException exception) {
      SecurityContextHolder.clearContext();
      sendErrorResponse(response, HttpStatus.UNAUTHORIZED, exception.getMessage());

    } catch (Exception exception) {
      SecurityContextHolder.clearContext();
      sendErrorResponse(response, HttpStatus.INTERNAL_SERVER_ERROR, exception.getMessage());

    }
  }

  private void processTokenAuthentication(String token) {
    Authentication existingAuth = SecurityContextHolder.getContext().getAuthentication();
    if (existingAuth != null && existingAuth.isAuthenticated())
      return;

    Authentication authentication = tryToAuthenticate(new PreAuthenticatedAuthenticationToken(token, null));
    SecurityContextHolder.getContext().setAuthentication(authentication);
  }

  private Authentication tryToAuthenticate(Authentication authenticationRequest) {
    Authentication responseAuthentication = authenticationManager.authenticate(authenticationRequest);

    if (!responseAuthentication.isAuthenticated())
      throw new InternalAuthenticationServiceException("Unable to authenticate User with provided credentials");

    return responseAuthentication;
  }

  private void sendErrorResponse(HttpServletResponse response, HttpStatus status,
                                 String message) throws IOException {
    response.setStatus(status.value());
    response.setContentType("application/json");
    response.setCharacterEncoding("UTF-8");

    ErrorResponse error = new ErrorResponse(status, message);
    response.getWriter().write(new ObjectMapper().writeValueAsString(error));
  }
}
