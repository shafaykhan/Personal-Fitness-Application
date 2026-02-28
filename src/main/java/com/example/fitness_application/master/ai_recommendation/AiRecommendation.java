package com.example.fitness_application.master.ai_recommendation;

import com.example.fitness_application.common.auditable.Auditable;
import com.example.fitness_application.common.enums.RecommendationEnum;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;

import java.time.LocalDateTime;

@EqualsAndHashCode(callSuper = true)
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AiRecommendation extends Auditable {

  @Id
  private Long id;

  private Long userId;

  private Long dataId;
  private RecommendationEnum recommendation;

  private String content;

  private LocalDateTime generatedAt;

  private Boolean recordStatus;
}
