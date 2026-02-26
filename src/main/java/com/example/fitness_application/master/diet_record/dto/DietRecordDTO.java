package com.example.fitness_application.master.diet_record.dto;

import com.example.fitness_application.common.enums.Status;
import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class DietRecordDTO {

  private Long id;

  @NotNull
  private Long userId;
  @NotNull
  private Long mealTypeId;

  @NotBlank
  private String foodName;

  private Integer calories;
  private Double protein;
  private Double carbs;
  private Double fat;

  @NotNull
  private LocalDateTime recordDateTime;

  private Status status;

  private String mealTypeValue;
}
