package com.example.fitness_application.master.health_record;

import com.example.fitness_application.common.enums.Status;
import com.example.fitness_application.common.exception.custom.NotFoundException;
import com.example.fitness_application.master.health_record.dto.HealthRecordDTO;
import com.example.fitness_application.master.user_preference.UserPreferenceService;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class HealthRecordService {

  private final HealthRecordRepository repository;
  private final UserPreferenceService userPreferenceService;
  private final ModelMapper modelMapper;

  public HealthRecordService(HealthRecordRepository repository,
                             UserPreferenceService userPreferenceService,
                             ModelMapper modelMapper) {
    this.repository = repository;
    this.userPreferenceService = userPreferenceService;
    this.modelMapper = modelMapper;
  }

  public HealthRecordDTO saveOrUpdate(HealthRecordDTO payloadDTO) {
    HealthRecord healthRecord;
    if (payloadDTO.getId() != null) {
      healthRecord = findEntityById(payloadDTO.getId());
    } else {
      healthRecord = new HealthRecord();
      healthRecord.setStatus(Status.ACTIVE);
      healthRecord.setRecordStatus(true);
    }

    modelMapper.map(payloadDTO, healthRecord);
    return modelMapper.map(repository.save(healthRecord), HealthRecordDTO.class);
  }

  public List<HealthRecordDTO> findAllByUserId(Long userId) {
    return repository.findAllByUserIdAndRecordStatusTrueOrderByIdDesc(userId).stream()
            .map(entity -> modelMapper.map(entity, HealthRecordDTO.class))
            .toList();
  }

  public HealthRecordDTO findById(Long id) {
    HealthRecord entity = findEntityById(id);
    return modelMapper.map(entity, HealthRecordDTO.class);
  }

  public void deleteById(Long id) {
    HealthRecord entity = findEntityById(id);
    entity.setRecordStatus(false);
    repository.save(entity);
  }

  private HealthRecord findEntityById(Long id) {
    return repository.findByIdAndRecordStatusTrue(id)
            .orElseThrow(() -> new NotFoundException("Health Record not found!"));
  }
}
