package com.example.fitness_application.master.ai_recommendation.strategy;

import com.example.fitness_application.common.enums.RecommendationEnum;
import com.example.fitness_application.master.health_record.HealthRecordService;
import com.example.fitness_application.master.health_record.dto.HealthRecordDTO;
import com.example.fitness_application.master.user_preference.UserPreferenceService;
import com.example.fitness_application.master.user_preference.dto.UserPreferenceDTO;
import org.springframework.stereotype.Component;

@Component
public class HealthPromptBuilder implements RecommendationPromptBuilder {

  private final HealthRecordService service;
  private final UserPreferenceService userPreferenceService;

  public HealthPromptBuilder(HealthRecordService service, UserPreferenceService userPreferenceService) {
    this.service = service;
    this.userPreferenceService = userPreferenceService;
  }

  @Override
  public RecommendationEnum getType() {
    return RecommendationEnum.HEALTH;
  }

  @Override
  public String build(Long dataId) {
    HealthRecordDTO dto = service.findById(dataId);
    UserPreferenceDTO pref = userPreferenceService.findByUserId(dto.getUserId());
    if (pref == null)
      pref = new UserPreferenceDTO();

    return """
            You are a certified health and fitness expert.
            
            STRICT RULES:
            - Output ONLY valid JSON.
            - Do NOT include thinking text.
            - Do NOT include analysis explanation.
            - Do NOT include <think> tags.
            - Do NOT include any text before or after JSON.
            - Your entire response must start with '{' and end with '}'.
            
            Analyze the health record and provide
            personalized health insights.
            
            Respond ONLY in valid JSON.
            No explanation outside JSON.
            
            User Goal: %s
            Preferred Intensity: %s
            Diet Type: %s
            Allergies: %s
            
            Health Record:
            Weight: %s kg
            Sleep Hours: %s
            Water Intake: %s liters
            BMI: %s
            Heart Rate: %s bpm
            Steps: %s
            Status: %s
            Date: %s
            
            Important:
            - Align suggestions with user's goal
            - Respect diet type
            - Avoid medical diagnosis
            - Give practical lifestyle advice
            - Keep response concise
            - Title must be short (max 20 words)
            
            Return in this format:
            {
              "title": "",
              "overallHealthSummary": "",
              "riskFlags": [],
              "improvements": [],
              "goalAlignmentAdvice": "",
              "hydrationAdvice": "",
              "sleepAdvice": "",
              "activityAdvice": "",
              "safetyNote": ""
            }
            """
            .formatted(
                    pref.getGoalTypeValue(),
                    pref.getIntensityValue(),
                    pref.getDietTypeValue(),
                    pref.getAllergies(),

                    dto.getWeight(),
                    dto.getSleepHours(),
                    dto.getWaterIntake(),
                    dto.getBmi(),
                    dto.getHeartRate(),
                    dto.getSteps(),
                    dto.getStatus(),
                    dto.getRecordDateTime()
            );
  }
}
