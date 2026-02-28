package com.example.fitness_application.master.ai_recommendation.dto;

import com.example.fitness_application.common.enums.RecommendationEnum;
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
public class AiRecommendationDTO {

  private Long id;

  @NotNull
  private Long userId;
  @NotNull
  private Long dataId;
  @NotNull
  private RecommendationEnum recommendation;

  private String content;

  private LocalDateTime generatedAt;

  private String typeValue;
}
