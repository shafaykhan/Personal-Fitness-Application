package com.example.fitness_application.master.ai_recommendation;

import org.springframework.data.repository.ListCrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface AiRecommendationRepository extends ListCrudRepository<AiRecommendation, Long> {

  List<AiRecommendation> findAllByUserIdAndRecordStatusTrueOrderByIdDesc(Long userId);

  Optional<AiRecommendation> findByIdAndRecordStatusTrue(Long id);
}
