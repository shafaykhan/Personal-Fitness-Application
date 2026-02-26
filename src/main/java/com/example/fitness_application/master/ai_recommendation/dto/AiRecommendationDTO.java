package com.example.fitness_application.master.ai_recommendation.dto;

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
public class AiRecommendationDTO {

  private Long id;

  @NotNull
  private Long userId;
  @NotNull
  private Long typeId;

  @NotBlank
  private String content;

  @NotNull
  private LocalDateTime recordDateTime;

  private String typeValue;
}
