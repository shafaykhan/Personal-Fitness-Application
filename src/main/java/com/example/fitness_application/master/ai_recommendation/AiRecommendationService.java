package com.example.fitness_application.master.ai_recommendation;

import com.example.fitness_application.common.enums.RecommendationEnum;
import com.example.fitness_application.common.exception.custom.NotFoundException;
import com.example.fitness_application.common.exception.custom.UnprocessableException;
import com.example.fitness_application.master.ai_recommendation.dto.AiRecommendationDTO;
import com.example.fitness_application.master.ai_recommendation.strategy.RecommendationPromptBuilder;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.ai.ollama.OllamaChatModel;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@Transactional
public class AiRecommendationService {

  private final AiRecommendationRepository repository;
  private final ModelMapper modelMapper;
  private final List<RecommendationPromptBuilder> recommendationPromptBuilders;
  private final OllamaChatModel ollamaChatModel;

  public AiRecommendationService(AiRecommendationRepository repository,
                                 ModelMapper modelMapper,
                                 List<RecommendationPromptBuilder> recommendationPromptBuilders,
                                 OllamaChatModel ollamaChatModel) {
    this.repository = repository;
    this.modelMapper = modelMapper;
    this.recommendationPromptBuilders = recommendationPromptBuilders;
    this.ollamaChatModel = ollamaChatModel;
  }

  public AiRecommendationDTO generateAndSave(AiRecommendationDTO payloadDTO) {
    log.info("Attempting to generate and save AI recommendation for payload: {}", payloadDTO);
    String generatedContent = generateRecommendation(payloadDTO.getRecommendation(), payloadDTO.getDataId());

    AiRecommendation prepareRecommendation = AiRecommendation.builder()
            .userId(payloadDTO.getUserId())
            .dataId(payloadDTO.getDataId())
            .recommendation(payloadDTO.getRecommendation())
            .content(generatedContent)
            .generatedAt(LocalDateTime.now())
            .recordStatus(true)
            .build();

    AiRecommendation savedRecommendation = repository.save(prepareRecommendation);
    log.info("Successfully generated and saved AI recommendation with ID: {}", savedRecommendation.getId());
    return modelMapper.map(savedRecommendation, AiRecommendationDTO.class);
  }

  public List<AiRecommendationDTO> findAllByUserId(Long userId) {
    return repository.findAllByUserIdAndRecordStatusTrueOrderByIdDesc(userId).stream()
            .map(entity -> modelMapper.map(entity, AiRecommendationDTO.class))
            .toList();
  }

  public AiRecommendationDTO findById(Long id) {
    AiRecommendation entity = findEntityById(id);
    return modelMapper.map(entity, AiRecommendationDTO.class);
  }

  public void deleteById(Long id) {
    AiRecommendation entity = findEntityById(id);
    entity.setRecordStatus(false);
    repository.save(entity);
  }

  private AiRecommendation findEntityById(Long id) {
    return repository.findByIdAndRecordStatusTrue(id)
            .orElseThrow(() -> {
              log.warn("AI Recommendation with ID: {} not found.", id);
              return new NotFoundException("AI Recommendation not found!");
            });
  }

  private String generateRecommendation(RecommendationEnum recommendation, Long dataId) {
    log.info("Generating AI recommendation for type: {} and data ID: {}", recommendation, dataId);
    String promptText = recommendationPromptBuilders.stream()
            .filter(b -> b.getType() == recommendation)
            .map(builder -> builder.build(dataId))
            .findFirst()
            .orElseThrow(() -> new UnprocessableException("AI Recommendation not found for type: " + recommendation.toString()));

    log.debug("Generated prompt for AI model: {}", promptText);

    try {
      String response = ollamaChatModel.call(promptText);
      log.debug("Received raw AI response: {}", response);
      String cleanJson = extractJson(response);
      validateJson(cleanJson);
      log.info("Successfully generated and validated AI response for type: {}", recommendation);
      return cleanJson;
    } catch (Exception e) {
      log.error("AI generation failed for type: {} and data ID: {}. Error: {}", recommendation, dataId, e.getMessage(), e);
      throw new UnprocessableException("AI generation failed: " + e.getMessage());
    }
  }

  private String extractJson(String response) {
    int start = response.indexOf("{");
    int end = response.lastIndexOf("}");

    if (start >= 0 && end > start) {
      return response.substring(start, end + 1);
    }

    throw new RuntimeException("No valid JSON found in AI response");
  }

  private void validateJson(String response) {
    log.debug("Validating JSON response from AI model.");
    ObjectMapper mapper = new ObjectMapper();
    try {
      mapper.readTree(response);
      log.debug("AI response is valid JSON.");
    } catch (Exception e) {
      log.error("AI response is not valid JSON. Response: {}. Error: {}", response, e.getMessage(), e);
      throw new UnprocessableException("Invalid AI JSON!");
    }
  }
}
