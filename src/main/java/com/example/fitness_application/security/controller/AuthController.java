package com.example.fitness_application.security.controller;

import com.example.fitness_application.security.dto.AuthResponseDTO;
import com.example.fitness_application.security.dto.LoginRequestDTO;
import com.example.fitness_application.security.service.AuthService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.PathVariable;

@RestController
@RequestMapping("/auth")
public class AuthController {

  private final AuthService service;

  public AuthController(AuthService service) {
    this.service = service;
  }

  @PostMapping("/login")
  public ResponseEntity<AuthResponseDTO> login(@Valid @RequestBody LoginRequestDTO request) {
    return ResponseEntity.ok(service.login(request));
  }

  @PostMapping("/logout/{userId}")
  public ResponseEntity<Void> logout(@PathVariable Long userId) {
    service.logout(userId);
    return new ResponseEntity<>(HttpStatus.OK);
  }
}
