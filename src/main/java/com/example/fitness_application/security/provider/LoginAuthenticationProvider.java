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
import org.springframework.context.annotation.Lazy;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.List;

@Slf4j
@Component
public class LoginAuthenticationProvider implements AuthenticationProvider {

  private final PasswordEncoder passwordEncoder;
  private final UserService userService;
  private final TokenService tokenService;
  private final ModelMapper modelMapper;

  public LoginAuthenticationProvider(@Lazy PasswordEncoder passwordEncoder,
                                     UserService userService,
                                     TokenService tokenService,
                                     ModelMapper modelMapper) {
    this.passwordEncoder = passwordEncoder;
    this.userService = userService;
    this.tokenService = tokenService;
    this.modelMapper = modelMapper;
  }

  @Override
  public Authentication authenticate(Authentication authentication) throws AuthenticationException {
    String username = authentication.getName();
    String password = (String) authentication.getCredentials();

    User user = userService.findByUsername(username);

    if (Status.INACTIVE.equals(user.getStatus()))
      throw new BadCredentialsException("User is not active!");

    if (!passwordEncoder.matches(password, user.getPassword()))
      throw new BadCredentialsException("Invalid password!");

    Token token = tokenService.generateAndSaveToken(user.getId());

    UserDTO userDTO = modelMapper.map(user, UserDTO.class);
    userService.addValues(Collections.singletonList(userDTO));

    return new AuthenticationWithToken(userDTO, token, List.of(new SimpleGrantedAuthority(user.getRole().name())));
  }

  @Override
  public boolean supports(Class<?> authentication) {
    return UsernamePasswordAuthenticationToken.class.isAssignableFrom(authentication);
  }
}
