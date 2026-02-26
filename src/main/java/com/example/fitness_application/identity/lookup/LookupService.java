package com.example.fitness_application.identity.lookup;

import com.example.fitness_application.common.enums.Status;
import com.example.fitness_application.common.exception.custom.ConflictException;
import com.example.fitness_application.common.exception.custom.NotFoundException;
import com.example.fitness_application.identity.lookup.dto.LookupDTO;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class LookupService {

  private final LookupRepository repository;
  private final ModelMapper modelMapper;

  public LookupService(LookupRepository repository, ModelMapper modelMapper) {
    this.repository = repository;
    this.modelMapper = modelMapper;
  }

  @Transactional
  public LookupDTO saveOrUpdate(LookupDTO payloadDTO) {
    Lookup lookup;
    isExists(payloadDTO.getId() != null ? payloadDTO.getId() : 0, payloadDTO.getGroupKey(), payloadDTO.getValue());
    if (payloadDTO.getId() != null) {
      lookup = findEntityById(payloadDTO.getId());

    } else {
      lookup = new Lookup();
      lookup.setRecordStatus(true);
    }

    modelMapper.map(payloadDTO, lookup);
    return modelMapper.map(repository.save(lookup), LookupDTO.class);
  }

  public List<LookupDTO> findAll() {
    return repository.findAllByRecordStatusTrueOrderByGroupKeyAscIdDesc().stream()
            .map(lookup -> modelMapper.map(lookup, LookupDTO.class))
            .toList();
  }

  public LookupDTO findById(Long id) {
    Lookup entity = findEntityById(id);
    return modelMapper.map(entity, LookupDTO.class);
  }

  public Map<String, List<LookupDTO>> findAllByGroupKeyInAndStatusActive(String groupKeys) {
    String[] groupKeyArray = groupKeys.split("~");
    return repository.findAllByGroupKeyInAndStatusAndRecordStatusTrue(groupKeyArray, Status.ACTIVE).stream()
            .collect(Collectors.groupingBy(Lookup::getGroupKey))
            .entrySet().stream()
            .collect(Collectors.toMap(
                    Map.Entry::getKey,
                    entry -> entry.getValue().stream()
                            .map(dto -> modelMapper.map(dto, LookupDTO.class))
                            .collect(Collectors.toList())));
  }

  public Map<Long, String> idValueMap(Set<Long> ids) {
    if (ids.isEmpty()) return Collections.emptyMap();

    return repository.findAllByIdIn(ids).stream()
            .collect(Collectors.toMap(Lookup::getId, Lookup::getValue));
  }

  private void isExists(long id, String groupKey, String value) {
    if (repository.countByIdNotAndGroupKeyAndValue(id, groupKey, value) > 0)
      throw new ConflictException("Lookup already exists!");
  }

  private Lookup findEntityById(Long id) {
    return repository.findByIdAndRecordStatusTrue(id)
            .orElseThrow(() -> new NotFoundException("Lookup not found!"));
  }
}
