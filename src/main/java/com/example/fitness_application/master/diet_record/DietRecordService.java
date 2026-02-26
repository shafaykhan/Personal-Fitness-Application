package com.example.fitness_application.master.diet_record;

import com.example.fitness_application.common.enums.Status;
import com.example.fitness_application.common.exception.custom.NotFoundException;
import com.example.fitness_application.identity.lookup.LookupService;
import com.example.fitness_application.master.diet_record.dto.DietRecordDTO;
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
public class DietRecordService {

  private final DietRecordRepository repository;
  private final LookupService lookupService;
  private final ModelMapper modelMapper;

  public DietRecordService(DietRecordRepository repository, LookupService lookupService, ModelMapper modelMapper) {
    this.repository = repository;
    this.lookupService = lookupService;
    this.modelMapper = modelMapper;
  }

  public DietRecordDTO saveOrUpdate(DietRecordDTO payloadDTO) {
    DietRecord dietRecord;
    if (payloadDTO.getId() != null) {
      dietRecord = findEntityById(payloadDTO.getId());
      dietRecord.setId(dietRecord.getId());
      dietRecord.setUserId(dietRecord.getUserId());
    } else {
      dietRecord = new DietRecord();
      dietRecord.setStatus(Status.ACTIVE);
      dietRecord.setRecordStatus(true);
    }

    modelMapper.map(payloadDTO, dietRecord);
    return modelMapper.map(repository.save(dietRecord), DietRecordDTO.class);
  }

  public List<DietRecordDTO> findAllByUserId(Long userId) {
    List<DietRecordDTO> dtoList = repository.findAllByUserIdAndRecordStatusTrueOrderByIdDesc(userId).stream()
            .map(entity -> modelMapper.map(entity, DietRecordDTO.class))
            .toList();
    addValues(dtoList);
    return dtoList;
  }

  public DietRecordDTO findById(Long id) {
    DietRecord entity = findEntityById(id);
    DietRecordDTO dto = modelMapper.map(entity, DietRecordDTO.class);
    addValues(List.of(dto));
    return dto;
  }

  public void deleteById(Long id) {
    DietRecord entity = findEntityById(id);
    entity.setRecordStatus(false);
    repository.save(entity);
  }

  public void addValues(List<DietRecordDTO> dtoList) {
    if (dtoList.isEmpty()) return;

    Set<Long> lookupIds = dtoList.stream()
            .map(DietRecordDTO::getMealTypeId)
            .filter(Objects::nonNull)
            .collect(Collectors.toSet());

    Map<Long, String> lookupIdAndValueMap = lookupService.idValueMap(lookupIds);

    dtoList.forEach(dto -> {
      dto.setMealTypeValue(lookupIdAndValueMap.get(dto.getMealTypeId()));
    });
  }

  private DietRecord findEntityById(Long id) {
    return repository.findByIdAndRecordStatusTrue(id)
            .orElseThrow(() -> new NotFoundException("DietRecord not found!"));
  }
}
