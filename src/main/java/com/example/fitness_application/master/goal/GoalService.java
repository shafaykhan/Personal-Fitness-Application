package com.example.fitness_application.master.goal;

import com.example.fitness_application.common.enums.Status;
import com.example.fitness_application.common.exception.custom.NotFoundException;
import com.example.fitness_application.identity.lookup.LookupService;
import com.example.fitness_application.master.goal.dto.GoalDTO;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@Transactional
public class GoalService {

  private final GoalRepository repository;
  private final LookupService lookupService;
  private final ModelMapper modelMapper;

  public GoalService(GoalRepository repository, LookupService lookupService, ModelMapper modelMapper) {
    this.repository = repository;
    this.lookupService = lookupService;
    this.modelMapper = modelMapper;
  }

  public GoalDTO saveOrUpdate(GoalDTO payloadDTO) {
    Goal goal;
    if (payloadDTO.getId() != null) {
      goal = findEntityById(payloadDTO.getId());
      goal.setId(goal.getId());
      goal.setUserId(goal.getUserId());
    } else {
      goal = new Goal();
      goal.setStatus(Status.ACTIVE);
      goal.setRecordStatus(true);
    }

    modelMapper.map(payloadDTO, goal);
    return modelMapper.map(repository.save(goal), GoalDTO.class);
  }

  public List<GoalDTO> findAllByUserId(Long userId) {
    List<GoalDTO> dtoList = repository.findAllByUserIdAndRecordStatusTrueOrderByIdDesc(userId).stream()
            .map(entity -> modelMapper.map(entity, GoalDTO.class))
            .toList();
    addValues(dtoList);
    return dtoList;
  }

  public GoalDTO findById(Long id) {
    Goal entity = findEntityById(id);
    GoalDTO dto = modelMapper.map(entity, GoalDTO.class);
    addValues(List.of(dto));
    return dto;
  }

  public void deleteById(Long id) {
    Goal entity = findEntityById(id);
    entity.setRecordStatus(false);
    repository.save(entity);
  }

  public void addValues(List<GoalDTO> dtoList) {
    if (dtoList.isEmpty()) return;

    Set<Long> lookupIds = dtoList.stream()
            .map(GoalDTO::getTypeId)
            .filter(Objects::nonNull)
            .collect(Collectors.toSet());

    Map<Long, String> lookupIdAndValueMap = lookupService.idValueMap(lookupIds);

    dtoList.forEach(dto -> {
      dto.setTypeValue(lookupIdAndValueMap.get(dto.getTypeId()));
    });
  }

  private Goal findEntityById(Long id) {
    return repository.findByIdAndRecordStatusTrue(id)
            .orElseThrow(() -> new NotFoundException("Goal not found!"));
  }
}
