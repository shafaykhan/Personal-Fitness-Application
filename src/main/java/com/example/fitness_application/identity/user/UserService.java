package com.example.fitness_application.identity.user;

import com.example.fitness_application.common.enums.Role;
import com.example.fitness_application.common.enums.Status;
import com.example.fitness_application.common.exception.custom.ConflictException;
import com.example.fitness_application.common.exception.custom.NotFoundException;
import com.example.fitness_application.identity.lookup.LookupService;
import com.example.fitness_application.identity.user.dto.CreateUserDTO;
import com.example.fitness_application.identity.user.dto.UserDTO;
import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class UserService {

  private final UserRepository repository;
  private final LookupService lookupService;
  private final ModelMapper modelMapper;
  private final PasswordEncoder passwordEncoder;

  public UserService(UserRepository repository,
                     LookupService lookupService,
                     ModelMapper modelMapper,
                     @Lazy PasswordEncoder passwordEncoder) {
    this.repository = repository;
    this.lookupService = lookupService;
    this.modelMapper = modelMapper;
    this.passwordEncoder = passwordEncoder;
  }

  public UserDTO save(CreateUserDTO payloadDTO) {
    isUsernameExists(0, payloadDTO.getUsername());
    isContactNoExists(0, payloadDTO.getContactNo());
    isEmailExists(0, payloadDTO.getEmail());

    User entity = modelMapper.map(payloadDTO, User.class);
    entity.setPassword(passwordEncoder.encode(payloadDTO.getPassword()));
    entity.setRole(Role.USER);
    entity.setStatus(Status.ACTIVE);

    return modelMapper.map(repository.save(entity), UserDTO.class);
  }

  public UserDTO update(UserDTO payloadDTO) {
    User user = findEntityById(payloadDTO.getId());
    if (!user.getUsername().equals(payloadDTO.getUsername()))
      isUsernameExists(payloadDTO.getId(), payloadDTO.getUsername());

    if (!user.getContactNo().equals(payloadDTO.getContactNo()))
      isContactNoExists(payloadDTO.getId(), payloadDTO.getContactNo());

    if (!user.getEmail().equals(payloadDTO.getEmail()))
      isEmailExists(payloadDTO.getId(), payloadDTO.getEmail());

    if (payloadDTO.getPassword() != null && payloadDTO.getPassword().length() < 60) {
      payloadDTO.setPassword(passwordEncoder.encode(payloadDTO.getPassword()));
    } else {
      payloadDTO.setPassword(user.getPassword());
    }

    modelMapper.map(payloadDTO, user);
    return modelMapper.map(repository.save(user), UserDTO.class);
  }

  public List<UserDTO> findAll() {
    List<UserDTO> dtoList = repository.findAllByOrderByIdDesc().stream()
            .map(entity -> modelMapper.map(entity, UserDTO.class))
            .toList();
    addValues(dtoList);
    return dtoList;
  }

  public UserDTO findById(Long id) {
    User entity = findEntityById(id);
    UserDTO dto = modelMapper.map(entity, UserDTO.class);
    addValues(Collections.singletonList(dto));
    return dto;
  }

  public User findByUsername(String username) {
    return repository.findByUsername(username)
            .orElseThrow(() -> new BadCredentialsException("Invalid username or password!"));
  }

  public void addValues(List<UserDTO> dtoList) {
    if (dtoList.isEmpty()) return;

    Set<Long> lookupIds = dtoList.stream()
            .map(dto -> new Long[]{dto.getHeightUomId(), dto.getWeightUomId()})
            .flatMap(Arrays::stream)
            .filter(Objects::nonNull)
            .collect(Collectors.toSet());

    Map<Long, String> lookupIdAndValueMap = lookupService.idValueMap(lookupIds);

    dtoList.forEach(dto -> {
      dto.setHeightUomValue(lookupIdAndValueMap.get(dto.getHeightUomId()));
      dto.setWeightUomValue(lookupIdAndValueMap.get(dto.getWeightUomId()));
    });
  }

  public User findEntityById(Long id) {
    return repository.findById(id)
            .orElseThrow(() -> new NotFoundException("User not found!"));
  }

  private void isUsernameExists(long id, String username) {
    if (repository.countByIdNotAndUsername(id, username) > 0)
      throw new ConflictException("Username already exists!");
  }

  private void isEmailExists(long id, String email) {
    if (repository.countByIdNotAndEmail(id, email) > 0)
      throw new ConflictException("Email already exists!");
  }

  private void isContactNoExists(long id, String contactNo) {
    if (repository.countByIdNotAndContactNo(id, contactNo) > 0)
      throw new ConflictException("Contact number already exists!");
  }
}
