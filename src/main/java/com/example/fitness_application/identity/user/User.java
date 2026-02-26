package com.example.fitness_application.identity.user;

import com.example.fitness_application.common.auditable.Auditable;
import com.example.fitness_application.common.enums.Gender;
import com.example.fitness_application.common.enums.Role;
import com.example.fitness_application.common.enums.Status;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;

import java.time.LocalDate;

@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
@AllArgsConstructor
public class User extends Auditable {

  @Id
  private Long id;

  private Long heightUomId;
  private Long weightUomId;

  private String name;
  private String username;
  private String password;
  private String email;
  private String contactNo;

  private LocalDate dateOfBirth;

  private Double height;
  private Double weight;

  private Gender gender;
  private Role role;
  private Status status;
}
