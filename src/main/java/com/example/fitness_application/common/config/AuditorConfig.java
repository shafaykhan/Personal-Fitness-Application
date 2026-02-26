package com.example.fitness_application.common.config;

import com.example.fitness_application.identity.user.User;
import com.example.fitness_application.identity.user.dto.UserDTO;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.AuditorAware;
import org.springframework.data.jdbc.repository.config.EnableJdbcAuditing;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.Optional;

@Configuration
@EnableJdbcAuditing
public class AuditorConfig implements AuditorAware<Long> {

  @Override
  public Optional<Long> getCurrentAuditor() {
    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    if (authentication != null && authentication.isAuthenticated() && authentication.getPrincipal() instanceof UserDTO) {
      return Optional.of(((UserDTO) authentication.getPrincipal()).getId());
    } else {
      return Optional.empty();
    }
  }
}
