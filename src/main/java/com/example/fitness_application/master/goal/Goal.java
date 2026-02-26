package com.example.fitness_application.master.goal;

import com.example.fitness_application.common.auditable.Auditable;
import com.example.fitness_application.common.enums.Status;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;

import java.time.LocalDate;
import java.time.LocalDateTime;

@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Goal extends Auditable {

  @Id
  private Long id;

  private Long userId;
  private Long typeId;

  private Double targetValue;
  private Double currentValue;

  private String description;

  private LocalDate startDate;
  private LocalDate endDate;

  private LocalDateTime recordDateTime;

  private Status status;

  private Boolean recordStatus;
}
