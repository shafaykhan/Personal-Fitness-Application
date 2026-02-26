package com.example.fitness_application.security.authentication;

import com.example.fitness_application.identity.token.Token;
import com.example.fitness_application.identity.user.dto.UserDTO;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.web.authentication.preauth.PreAuthenticatedAuthenticationToken;

import java.util.Collection;

public class AuthenticationWithToken extends PreAuthenticatedAuthenticationToken {

  public AuthenticationWithToken(UserDTO aPrincipal, Token token) {
    super(aPrincipal, null);
    setDetails(token);
    setAuthenticated(true);
  }

  public AuthenticationWithToken(UserDTO aPrincipal, Token token, Collection<? extends GrantedAuthority> anAuthorities) {
    super(aPrincipal, null, anAuthorities);
    setDetails(token);
  }
}
