package com.example.fitness_application.security.dto;

import com.example.fitness_application.identity.user.dto.UserDTO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AuthResponseDTO {

  private UserDTO userDTO;
  private String token;
}
