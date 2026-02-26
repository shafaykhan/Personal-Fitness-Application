package com.example.fitness_application.identity.user.dto;

import com.example.fitness_application.common.enums.Gender;
import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class CreateUserDTO {

  @NotBlank
  private String name;
  @NotBlank
  private String username;
  @NotBlank
  private String password;
  @NotBlank
  private String email;
  @NotBlank
  private String contactNo;

  @NotNull
  private LocalDate dateOfBirth;

  @NotNull
  private Gender gender;
}
