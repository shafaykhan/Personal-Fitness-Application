package com.example.fitness_application.identity.lookup;

import com.example.fitness_application.identity.lookup.dto.LookupDTO;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/lookups")
public class LookupController {

  private final LookupService service;

  public LookupController(LookupService service) {
    this.service = service;
  }

  @PostMapping
  public ResponseEntity<LookupDTO> saveOrUpdate(@RequestBody @Valid LookupDTO payloadDTO) {
    return ResponseEntity.ok(service.saveOrUpdate(payloadDTO));
  }

  @GetMapping
  public ResponseEntity<List<LookupDTO>> findAll() {
    return ResponseEntity.ok(service.findAll());
  }

  @GetMapping("/{id}")
  public ResponseEntity<LookupDTO> findById(@PathVariable Long id) {
    return ResponseEntity.ok(service.findById(id));
  }

  @GetMapping("/by-status-active")
  public ResponseEntity<Map<String, List<LookupDTO>>> findAllByGroupKeyInAndStatusActive(@RequestParam String groupKeys) {
    return ResponseEntity.ok(service.findAllByGroupKeyInAndStatusActive(groupKeys));
  }
}
