package com.example.fitness_application.master.workout;

import com.example.fitness_application.master.workout.dto.WorkoutDTO;
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
@RequestMapping("/api/workouts")
public class WorkoutController {

  private final WorkoutService service;

  public WorkoutController(WorkoutService service) {
    this.service = service;
  }

  @PostMapping
  public ResponseEntity<WorkoutDTO> saveOrUpdate(@Valid @RequestBody WorkoutDTO payloadDTO) {
    return ResponseEntity.ok(service.saveOrUpdate(payloadDTO));
  }

  @GetMapping("/by-user/{userId}")
  public ResponseEntity<List<WorkoutDTO>> findAllByUserId(@PathVariable Long userId) {
    return ResponseEntity.ok(service.findAllByUserId(userId));
  }

  @GetMapping("/{id}")
  public ResponseEntity<WorkoutDTO> findById(@PathVariable Long id) {
    return ResponseEntity.ok(service.findById(id));
  }

  @DeleteMapping("/{id}")
  public ResponseEntity<Void> deleteById(@PathVariable Long id) {
    service.deleteById(id);
    return new ResponseEntity<>(HttpStatus.OK);
  }
}
