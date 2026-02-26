package com.example.fitness_application.master.ai_recommendation;

import com.example.fitness_application.common.auditable.Auditable;
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
public class AiRecommendation extends Auditable {

  @Id
  private Long id;

  private Long userId;
  private Long typeId;

  private String content;

  private LocalDateTime recordDateTime;

  private Boolean recordStatus;
}
