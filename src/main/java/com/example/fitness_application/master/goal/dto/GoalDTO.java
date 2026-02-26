package com.example.fitness_application.master.goal.dto;

import com.example.fitness_application.common.enums.Status;
import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class GoalDTO {

  private Long id;

  @NotNull
  private Long userId;
  @NotNull
  private Long typeId;

  @NotNull
  private Double targetValue;
  @NotNull
  private Double currentValue;

  private String description;

  @NotNull
  private LocalDate startDate;
  private LocalDate endDate;

  @NotNull
  private LocalDateTime recordDateTime;

  private Status status;

  private String typeValue;
}
