package com.example.fitness_application.master.ai_recommendation.strategy;

import com.example.fitness_application.common.enums.RecommendationEnum;

public interface RecommendationPromptBuilder {

  RecommendationEnum getType();

  String build(Long dataId);
}
