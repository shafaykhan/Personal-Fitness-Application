package com.example.fitness_application.identity.user;

import com.example.fitness_application.identity.user.dto.CreateUserDTO;
import com.example.fitness_application.identity.user.dto.UserDTO;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/users")
public class UserController {

  private final UserService service;

  public UserController(UserService service) {
    this.service = service;
  }

  @PostMapping("/register")
  public ResponseEntity<UserDTO> save(@Valid @RequestBody CreateUserDTO payloadDTO) {
    return ResponseEntity.ok(service.save(payloadDTO));
  }

  @PutMapping
  public ResponseEntity<UserDTO> update(@Valid @RequestBody UserDTO payloadDTO) {
    return ResponseEntity.ok(service.update(payloadDTO));
  }

  @GetMapping
  public ResponseEntity<List<UserDTO>> findAll() {
    return ResponseEntity.ok(service.findAll());
  }

  @GetMapping("/{id}")
  public ResponseEntity<UserDTO> findById(@PathVariable Long id) {
    return ResponseEntity.ok(service.findById(id));
  }
}
