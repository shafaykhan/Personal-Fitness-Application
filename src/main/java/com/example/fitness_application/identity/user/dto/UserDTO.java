package com.example.fitness_application.identity.user.dto;

import com.example.fitness_application.common.enums.Gender;
import com.example.fitness_application.common.enums.Role;
import com.example.fitness_application.common.enums.Status;
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
public class UserDTO {

  @NotNull
  private Long id;

  private Long heightUomId;
  private Long weightUomId;

  @NotBlank
  private String name;
  @NotBlank
  private String username;
  @NotBlank
  private String password;
  @NotBlank
  private String contactNo;
  @NotBlank
  private String email;

  @NotNull
  private LocalDate dateOfBirth;

  private Double height;
  private Double weight;

  @NotNull
  private Gender gender;
  private Role role;
  private Status status;

  private String heightUomValue;
  private String weightUomValue;
}
