package com.example.fitness_application.master.health_record;

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
public class HealthRecord extends Auditable {

  @Id
  private Long id;

  private Long userId;

  private Double weight;
  private Double sleepHours;
  private Double waterIntake;
  private Double bmi;
  private Integer heartRate;
  private Integer steps;

  private LocalDateTime recordDateTime;

  private Status status;

  private Boolean recordStatus;
}
