package com.example.fitness_application.master.workout;

import com.example.fitness_application.common.auditable.Auditable;
import com.example.fitness_application.common.enums.Status;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;

import java.time.LocalDateTime;

@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Workout extends Auditable {

  @Id
  private Long id;

  private Long userId;
  private Long typeId;
  private Long durationUomId;
  private Long intensityId;

  private String exerciseName;

  private Integer duration;
  private Integer caloriesBurned;

  private String notes;

  private LocalDateTime recordDateTime;

  private Status status;

  private Boolean recordStatus;
}
