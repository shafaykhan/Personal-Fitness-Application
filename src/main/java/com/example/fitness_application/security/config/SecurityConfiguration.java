package com.example.fitness_application.security.config;

import com.example.fitness_application.common.exception.response.ErrorResponse;
import com.example.fitness_application.security.filter.AuthenticationFilter;
import com.example.fitness_application.security.provider.LoginAuthenticationProvider;
import com.example.fitness_application.security.provider.TokenAuthenticationProvider;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.List;

@Slf4j
@Configuration
@EnableWebSecurity
public class SecurityConfiguration {

  private final AuthenticationFilter authenticationFilter;
  private final LoginAuthenticationProvider loginAuthenticationProvider;
  private final TokenAuthenticationProvider tokenAuthenticationProvider;

  public SecurityConfiguration(LoginAuthenticationProvider loginAuthenticationProvider,
                               TokenAuthenticationProvider tokenAuthenticationProvider,
                               AuthenticationFilter authenticationFilter) {
    this.loginAuthenticationProvider = loginAuthenticationProvider;
    this.tokenAuthenticationProvider = tokenAuthenticationProvider;
    this.authenticationFilter = authenticationFilter;
  }

  @Bean
  public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
    http.csrf(AbstractHttpConfigurer::disable)
            .cors(cors -> cors.configurationSource(corsConfigurationSource()))
            .authorizeHttpRequests(auth -> auth
                    .requestMatchers("/page/**", "/css/**", "/js/**", "/img/**", "/component/**",
                            "/favicon.ico").permitAll()
                    .requestMatchers("/auth/login").permitAll()
                    .requestMatchers(HttpMethod.POST, "/api/users/register").permitAll()
                    // All other API endpoints require authentication
                    .anyRequest().permitAll()
            )
            .sessionManagement(session ->
                    session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
            .exceptionHandling(exceptions ->
                    exceptions.authenticationEntryPoint(authenticationEntryPoint())
                            .accessDeniedHandler(accessDeniedHandler()))
            .addFilterBefore(authenticationFilter, UsernamePasswordAuthenticationFilter.class);

    return http.build();
  }

  @Bean
  public AuthenticationEntryPoint authenticationEntryPoint() {
    return (request, response, authException) -> {
      log.warn("Authentication failed: {}", authException.getMessage(), authException);

      ErrorResponse errorResponse = new ErrorResponse(HttpStatus.UNAUTHORIZED, "Invalid or expired token!");
      response.setContentType("application/json");
      response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
      response.getWriter().write(new ObjectMapper().writeValueAsString(errorResponse));
    };
  }

  @Bean
  public AccessDeniedHandler accessDeniedHandler() {
    return (request, response, accessDeniedException) -> {
      log.warn("Authorization failed: {}", accessDeniedException.getMessage(), accessDeniedException);

      ErrorResponse errorResponse = new ErrorResponse(HttpStatus.FORBIDDEN, "Access denied!");
      response.setStatus(HttpServletResponse.SC_FORBIDDEN);
      response.setContentType("application/json");
      response.setCharacterEncoding("UTF-8");
      response.getWriter().write(new ObjectMapper().writeValueAsString(errorResponse));
    };
  }

  @Bean
  public CorsConfigurationSource corsConfigurationSource() {
    CorsConfiguration configuration = new CorsConfiguration();
    configuration.setAllowedOrigins(List.of("*"));
    configuration.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "OPTIONS"));
    configuration.setAllowedHeaders(List.of("*"));
    configuration.setAllowCredentials(true);

    UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
    source.registerCorsConfiguration("/**", configuration);
    return source;
  }

  @Bean
  public PasswordEncoder passwordEncoder() {
    return new BCryptPasswordEncoder();
  }

  @Bean
  public AuthenticationManager authenticationManager() {
    return new ProviderManager(List.of(loginAuthenticationProvider, tokenAuthenticationProvider));
  }
}
