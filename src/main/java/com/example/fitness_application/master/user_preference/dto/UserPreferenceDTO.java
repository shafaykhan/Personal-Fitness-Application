package com.example.fitness_application.master.user_preference.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class UserPreferenceDTO {

  private Long id;

  @NotNull
  private Long userId;
  private Long dietTypeId;
  private Long intensityId;
  private Long goalTypeId;

  private String allergies;

  private String dietTypeValue;
  private String intensityValue;
  private String goalTypeValue;
}
