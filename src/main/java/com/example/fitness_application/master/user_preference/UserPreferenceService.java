package com.example.fitness_application.master.user_preference;

import com.example.fitness_application.common.exception.custom.NotFoundException;
import com.example.fitness_application.identity.lookup.LookupService;
import com.example.fitness_application.master.user_preference.dto.UserPreferenceDTO;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@Transactional
public class UserPreferenceService {

  private final UserPreferenceRepository repository;
  private final LookupService lookupService;
  private final ModelMapper modelMapper;

  public UserPreferenceService(UserPreferenceRepository repository, LookupService lookupService, ModelMapper modelMapper) {
    this.repository = repository;
    this.lookupService = lookupService;
    this.modelMapper = modelMapper;
  }

  public UserPreferenceDTO saveOrUpdate(UserPreferenceDTO payloadDTO) {
    UserPreference userPreference;
    if (payloadDTO.getId() != null) {
      userPreference = findEntityById(payloadDTO.getId());
      userPreference.setId(userPreference.getId());
      userPreference.setUserId(userPreference.getUserId());
    } else {
      userPreference = repository.findByUserId(payloadDTO.getUserId()).orElse(new UserPreference());
    }

    modelMapper.map(payloadDTO, userPreference);
    return modelMapper.map(repository.save(userPreference), UserPreferenceDTO.class);
  }

  public UserPreferenceDTO findByUserId(Long userId) {
    UserPreferenceDTO dto = repository.findByUserId(userId)
            .map(userPreference -> modelMapper.map(userPreference, UserPreferenceDTO.class))
            .orElse(null);

    if (dto == null) return null;

    addValues(Collections.singletonList(dto));
    return dto;
  }


  public void addValues(List<UserPreferenceDTO> dtoList) {
    if (dtoList.isEmpty()) return;

    Set<Long> lookupIds = dtoList.stream()
            .map(dto -> new Long[]{dto.getDietTypeId(), dto.getIntensityId(), dto.getGoalTypeId()})
            .flatMap(Arrays::stream)
            .filter(Objects::nonNull)
            .collect(Collectors.toSet());

    Map<Long, String> lookupIdAndValueMap = lookupService.idValueMap(lookupIds);

    dtoList.forEach(dto -> {
      dto.setDietTypeValue(lookupIdAndValueMap.get(dto.getDietTypeId()));
      dto.setIntensityValue(lookupIdAndValueMap.get(dto.getIntensityId()));
      dto.setGoalTypeValue(lookupIdAndValueMap.get(dto.getGoalTypeId()));
    });
  }

  private UserPreference findEntityById(Long id) {
    return repository.findById(id)
            .orElseThrow(() -> new NotFoundException("User Preference not found!"));
  }
}
