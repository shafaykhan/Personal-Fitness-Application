package com.example.fitness_application.master.ai_recommendation.strategy;

import com.example.fitness_application.common.enums.RecommendationEnum;
import com.example.fitness_application.master.user_preference.UserPreferenceService;
import com.example.fitness_application.master.user_preference.dto.UserPreferenceDTO;
import com.example.fitness_application.master.workout.WorkoutService;
import com.example.fitness_application.master.workout.dto.WorkoutDTO;
import org.springframework.stereotype.Component;

@Component
public class WorkoutPromptBuilder implements RecommendationPromptBuilder {

  private final WorkoutService service;
  private final UserPreferenceService userPreferenceService;

  public WorkoutPromptBuilder(WorkoutService service,
                              UserPreferenceService userPreferenceService) {
    this.service = service;
    this.userPreferenceService = userPreferenceService;
  }

  @Override
  public RecommendationEnum getType() {
    return RecommendationEnum.WORKOUT;
  }

  @Override
  public String build(Long dataId) {
    WorkoutDTO dto = service.findById(dataId);
    UserPreferenceDTO pref = userPreferenceService.findByUserId(dto.getUserId());
    if (pref == null)
      pref = new UserPreferenceDTO();

    return """
            You are a certified professional fitness trainer.
            
            STRICT RULES:
            - Output ONLY valid JSON.
            - Do NOT include thinking text.
            - Do NOT include analysis explanation.
            - Do NOT include <think> tags.
            - Do NOT include any text before or after JSON.
            - Your entire response must start with '{' and end with '}'.
            
            Analyze the workout record and provide
            personalized improvement suggestions.
            
            Consider user's preferences and goal.
            
            Respond ONLY in valid JSON.
            No explanation outside JSON.
            
            User Goal: %s
            Preferred Intensity: %s
            Diet Type: %s
            Allergies: %s
            
            Workout Details:
            Exercise: %s
            Type: %s
            Duration: %d %s
            Intensity Performed: %s
            Calories Burned: %s
            Notes: %s
            Date: %s
            
            Important:
             - Respect allergies
             - Align suggestions with goal
             - Do not give medical diagnosis
             - Keep response concise
             - Title must be short (max 20 words)
            
            Return in this format:
            {
              "title": "",
              "analysis": "",
              "caloriesEvaluation": "",
              "improvements": [],
              "nextWorkoutSuggestion": "",
              "recoveryAdvice": "",
              "safetyTip": ""
            }
            """
            .formatted(
                    pref.getGoalTypeValue(),
                    pref.getIntensityValue(),
                    pref.getDietTypeValue(),
                    pref.getAllergies(),
                    dto.getExerciseName(),
                    dto.getTypeValue(),
                    dto.getDuration(),
                    dto.getDurationUomValue(),
                    dto.getIntensityValue(),
                    dto.getCaloriesBurned(),
                    dto.getNotes(),
                    dto.getRecordDateTime()
            );
  }
}
