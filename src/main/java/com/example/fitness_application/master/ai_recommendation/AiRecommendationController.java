package com.example.fitness_application.master.ai_recommendation;

import com.example.fitness_application.master.ai_recommendation.dto.AiRecommendationDTO;
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
@RequestMapping("/api/ai-recommendations")
public class AiRecommendationController {

  private final AiRecommendationService service;

  public AiRecommendationController(AiRecommendationService service) {
    this.service = service;
  }

  @PostMapping
  public ResponseEntity<AiRecommendationDTO> generateAndSave(@RequestBody @Valid AiRecommendationDTO payloadDTO) {
    return ResponseEntity.ok(service.generateAndSave(payloadDTO));
  }

  @GetMapping("/by-user/{userId}")
  public ResponseEntity<List<AiRecommendationDTO>> findAllByUserId(@PathVariable Long userId) {
    return ResponseEntity.ok(service.findAllByUserId(userId));
  }

  @GetMapping("/{id}")
  public ResponseEntity<AiRecommendationDTO> findById(@PathVariable Long id) {
    return ResponseEntity.ok(service.findById(id));
  }

  @DeleteMapping("/{id}")
  public ResponseEntity<Void> deleteById(@PathVariable Long id) {
    service.deleteById(id);
    return new ResponseEntity<>(HttpStatus.OK);
  }
}
