package com.example.fitness_application.master.workout.dto;

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
public class WorkoutDTO {

  private Long id;

  @NotNull
  private Long userId;
  @NotNull
  private Long typeId;
  @NotNull
  private Long durationUomId;
  @NotNull
  private Long intensityId;

  @NotBlank
  private String exerciseName;

  @NotNull
  private Integer duration;
  private Integer caloriesBurned;

  private String notes;

  @NotNull
  private LocalDateTime recordDateTime;
  @NotNull
  private Status status;

  private String typeValue;
  private String durationUomValue;
  private String intensityValue;
}
