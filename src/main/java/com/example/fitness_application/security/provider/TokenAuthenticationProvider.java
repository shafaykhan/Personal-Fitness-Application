package com.example.fitness_application.security.provider;

import com.example.fitness_application.common.enums.Status;
import com.example.fitness_application.identity.token.Token;
import com.example.fitness_application.identity.token.TokenService;
import com.example.fitness_application.identity.user.User;
import com.example.fitness_application.identity.user.UserService;
import com.example.fitness_application.identity.user.dto.UserDTO;
import com.example.fitness_application.security.authentication.AuthenticationWithToken;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.web.authentication.preauth.PreAuthenticatedAuthenticationToken;
import org.springframework.stereotype.Component;

import java.util.List;

@Slf4j
@Component
public class TokenAuthenticationProvider implements AuthenticationProvider {

  private final UserService userService;
  private final TokenService tokenService;
  private final ModelMapper modelMapper;

  public TokenAuthenticationProvider(UserService userService,
                                     TokenService tokenService,
                                     ModelMapper modelMapper) {
    this.userService = userService;
    this.tokenService = tokenService;
    this.modelMapper = modelMapper;
  }

  @Override
  public Authentication authenticate(Authentication authentication) throws AuthenticationException {
    String token = (String) authentication.getPrincipal();

    if (token == null || token.isBlank())
      throw new BadCredentialsException("Invalid token!");

    Token tokenEntity = tokenService.findByToken(token);
    tokenEntity = tokenService.validateTokenAndUpdateLastUpdatedOn(tokenEntity);

    if (Boolean.TRUE.equals(tokenEntity.getRevoked())) {
      throw new BadCredentialsException("Token revoked!");
    }

    User user = userService.findEntityById(tokenEntity.getUserId());

    if (!Status.ACTIVE.equals(user.getStatus()))
      throw new BadCredentialsException("User is not active!");

    UserDTO userDTO = modelMapper.map(user, UserDTO.class);
    userService.addValues(List.of(userDTO));
    return new AuthenticationWithToken(userDTO, tokenEntity, List.of(new SimpleGrantedAuthority(userDTO.getRole().name())));
  }

  @Override
  public boolean supports(Class<?> authentication) {
    return PreAuthenticatedAuthenticationToken.class.isAssignableFrom(authentication);
  }
}
