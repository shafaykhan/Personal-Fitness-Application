package com.example.fitness_application.security.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class RefreshTokenRequestDTO {

  @NotBlank
  private String refreshToken;
}
