package com.example.fitness_application.master.user_preference;

import com.example.fitness_application.master.user_preference.dto.UserPreferenceDTO;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/user-preferences")
public class UserPreferenceController {

  private final UserPreferenceService service;

  public UserPreferenceController(UserPreferenceService service) {
    this.service = service;
  }

  @PostMapping
  public ResponseEntity<UserPreferenceDTO> saveOrUpdate(@RequestBody @Valid UserPreferenceDTO payloadDTO) {
    return ResponseEntity.ok(service.saveOrUpdate(payloadDTO));
  }

  @GetMapping("/by-user/{userId}")
  public ResponseEntity<UserPreferenceDTO> findByUserId(@PathVariable Long userId) {
    UserPreferenceDTO dto = service.findByUserId(userId);
    if (dto == null) {
      return ResponseEntity.notFound().build();
    }
    return ResponseEntity.ok(dto);
  }
}
