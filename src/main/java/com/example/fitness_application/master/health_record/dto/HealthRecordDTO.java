package com.example.fitness_application.master.health_record.dto;

import com.example.fitness_application.common.enums.Status;
import com.fasterxml.jackson.annotation.JsonInclude;
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
public class HealthRecordDTO {

  private Long id;

  @NotNull
  private Long userId;

  private Double weight;
  private Double sleepHours;
  private Double waterIntake;
  private Double bmi;
  private Integer heartRate;
  private Integer steps;

  @NotNull
  private LocalDateTime recordDateTime;

  @NotNull
  private Status status;
}
