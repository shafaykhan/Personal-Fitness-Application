package com.example.fitness_application.master.diet_record;

import com.example.fitness_application.master.diet_record.dto.DietRecordDTO;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/diet-records")
public class DietRecordController {

  private final DietRecordService service;

  public DietRecordController(DietRecordService service) {
    this.service = service;
  }

  @PostMapping
  public ResponseEntity<DietRecordDTO> saveOrUpdate(@Valid @RequestBody DietRecordDTO payloadDTO) {
    return ResponseEntity.ok(service.saveOrUpdate(payloadDTO));
  }

  @GetMapping("/by-user/{userId}")
  public ResponseEntity<List<DietRecordDTO>> findAllByUserId(@PathVariable Long userId) {
    return ResponseEntity.ok(service.findAllByUserId(userId));
  }

  @GetMapping("/{id}")
  public ResponseEntity<DietRecordDTO> findById(@PathVariable Long id) {
    return ResponseEntity.ok(service.findById(id));
  }

  @DeleteMapping("/{id}")
  public ResponseEntity<Void> deleteById(@PathVariable Long id) {
    service.deleteById(id);
    return new ResponseEntity<>(HttpStatus.OK);
  }
}
