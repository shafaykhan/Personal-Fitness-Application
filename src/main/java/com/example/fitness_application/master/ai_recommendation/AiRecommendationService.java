package com.example.fitness_application.master.ai_recommendation;

import com.example.fitness_application.common.exception.custom.NotFoundException;
import com.example.fitness_application.identity.lookup.LookupService;
import com.example.fitness_application.master.ai_recommendation.dto.AiRecommendationDTO;
import com.example.fitness_application.master.workout.WorkoutService;
import org.modelmapper.ModelMapper;
import org.springframework.ai.ollama.OllamaChatModel;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@Transactional
public class AiRecommendationService {

  private final AiRecommendationRepository repository;
  private final LookupService lookupService;
  private final ModelMapper modelMapper;
  private final WorkoutService workoutService;
  private final OllamaChatModel ollamaChatModel;

  public AiRecommendationService(AiRecommendationRepository repository,
                                 LookupService lookupService,
                                 ModelMapper modelMapper,
                                 WorkoutService workoutService,
                                 OllamaChatModel ollamaChatModel) {
    this.repository = repository;
    this.lookupService = lookupService;
    this.modelMapper = modelMapper;
    this.workoutService = workoutService;
    this.ollamaChatModel = ollamaChatModel;
  }

  public AiRecommendationDTO saveOrUpdate(AiRecommendationDTO payloadDTO) {
    AiRecommendation aiRecommendation;
    if (payloadDTO.getId() != null) {
      aiRecommendation = findEntityById(payloadDTO.getId());
      aiRecommendation.setId(aiRecommendation.getId());
      aiRecommendation.setUserId(aiRecommendation.getUserId());
    } else {
      aiRecommendation = new AiRecommendation();
      aiRecommendation.setRecordStatus(true);
    }

    modelMapper.map(payloadDTO, aiRecommendation);
    return modelMapper.map(repository.save(aiRecommendation), AiRecommendationDTO.class);
  }

  public List<AiRecommendationDTO> findAllByUserId(Long userId) {
    List<AiRecommendationDTO> dtoList = repository.findAllByUserIdAndRecordStatusTrueOrderByIdDesc(userId).stream()
            .map(entity -> modelMapper.map(entity, AiRecommendationDTO.class))
            .toList();
    addValues(dtoList);
    return dtoList;
  }

  public AiRecommendationDTO findById(Long id) {
    AiRecommendation entity = findEntityById(id);
    AiRecommendationDTO dto = modelMapper.map(entity, AiRecommendationDTO.class);
    addValues(List.of(dto));
    return dto;
  }

  public void deleteById(Long id) {
    AiRecommendation entity = findEntityById(id);
    entity.setRecordStatus(false);
    repository.save(entity);
  }

  public void generateRecommendationAndSave(String type, String promote) {

  }

  public void addValues(List<AiRecommendationDTO> dtoList) {
    if (dtoList.isEmpty()) return;

    Set<Long> lookupIds = dtoList.stream()
            .map(AiRecommendationDTO::getTypeId)
            .filter(Objects::nonNull)
            .collect(Collectors.toSet());

    Map<Long, String> lookupIdAndValueMap = lookupService.idValueMap(lookupIds);

    dtoList.forEach(dto -> {
      dto.setTypeValue(lookupIdAndValueMap.get(dto.getTypeId()));
    });
  }

  private AiRecommendation findEntityById(Long id) {
    return repository.findByIdAndRecordStatusTrue(id)
            .orElseThrow(() -> new NotFoundException("AI Recommendation not found!"));
  }
}
