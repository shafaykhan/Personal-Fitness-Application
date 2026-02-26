package com.example.fitness_application.master.diet_record;

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
public class DietRecord extends Auditable {

  @Id
  private Long id;

  private Long userId;
  private Long mealTypeId;

  private String foodName;

  private Integer calories;
  private Double protein;
  private Double carbs;
  private Double fat;

  private LocalDateTime recordDateTime;

  private Status status;

  private Boolean recordStatus;
}
