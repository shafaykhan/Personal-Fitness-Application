package com.example.fitness_application.master.workout;

import com.example.fitness_application.common.enums.Status;
import com.example.fitness_application.common.exception.custom.NotFoundException;
import com.example.fitness_application.identity.lookup.LookupService;
import com.example.fitness_application.master.workout.dto.WorkoutDTO;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@Transactional
public class WorkoutService {

  private final com.example.fitness_application.master.workout.WorkoutRepository repository;
  private final LookupService lookupService;
  private final ModelMapper modelMapper;

  public WorkoutService(WorkoutRepository repository, LookupService lookupService, ModelMapper modelMapper) {
    this.repository = repository;
    this.lookupService = lookupService;
    this.modelMapper = modelMapper;
  }

  public WorkoutDTO saveOrUpdate(WorkoutDTO payloadDTO) {
    Workout workout;
    if (payloadDTO.getId() != null) {
      workout = findEntityById(payloadDTO.getId());

      workout.setId(workout.getId());
      workout.setUserId(workout.getUserId());
    } else {
      workout = new Workout();
      workout.setStatus(Status.ACTIVE);
      workout.setRecordStatus(true);
    }

    modelMapper.map(payloadDTO, workout);
    return modelMapper.map(repository.save(workout), WorkoutDTO.class);
  }

  public List<WorkoutDTO> findAllByUserId(Long userId) {
    List<WorkoutDTO> dtoList = repository.findAllByUserIdAndRecordStatusTrueOrderByIdDesc(userId).stream().map(entity -> modelMapper.map(entity, WorkoutDTO.class)).toList();
    addValues(dtoList);
    return dtoList;
  }

  public WorkoutDTO findById(Long id) {
    Workout entity = findEntityById(id);
    WorkoutDTO dto = modelMapper.map(entity, WorkoutDTO.class);
    addValues(List.of(dto));
    return dto;
  }

  public void deleteById(Long id) {
    Workout entity = findEntityById(id);
    entity.setRecordStatus(false);
    repository.save(entity);
  }

  public void addValues(List<WorkoutDTO> dtoList) {
    if (dtoList.isEmpty()) return;

    Set<Long> lookupIds = dtoList.stream().map(dto -> new Long[]{dto.getTypeId(), dto.getDurationUomId(), dto.getIntensityId()}).flatMap(Arrays::stream).filter(Objects::nonNull).collect(Collectors.toSet());

    Map<Long, String> lookupIdAndValueMap = lookupService.idValueMap(lookupIds);

    dtoList.forEach(dto -> {
      dto.setTypeValue(lookupIdAndValueMap.get(dto.getTypeId()));
      dto.setDurationUomValue(lookupIdAndValueMap.get(dto.getDurationUomId()));
      dto.setIntensityValue(lookupIdAndValueMap.get(dto.getIntensityId()));
    });
  }

  private String buildWorkout(Long id) {
    WorkoutDTO dto = findById(id);
    return """
            You are a certified fitness trainer.
            
            Analyze the following workout record
            and give improvement suggestions.
            
            Respond only in valid JSON.
            No extra explanation.
            
            Workout Details:
            Exercise: %s
            Type: %s
            Duration: %d %s
            Intensity: %s
            Calories Burned: %s
            Notes: %s
            Date: %s
            
            Return in this format:
            {
              "analysis": "",
              "caloriesEvaluation": "",
              "improvements": [],
              "nextWorkoutSuggestion": "",
              "safetyTip": ""
            }
            """.formatted(dto.getExerciseName(),
            dto.getTypeValue(),
            dto.getDuration(),
            dto.getDurationUomValue(),
            dto.getIntensityValue(),
            dto.getCaloriesBurned(),
            dto.getNotes(),
            dto.getRecordDateTime());
  }

  private Workout findEntityById(Long id) {
    return repository.findByIdAndRecordStatusTrue(id).orElseThrow(() -> new NotFoundException("Workout not found!"));
  }
}
