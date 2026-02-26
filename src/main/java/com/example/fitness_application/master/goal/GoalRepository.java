package com.example.fitness_application.master.goal;

import org.springframework.data.repository.ListCrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface GoalRepository extends ListCrudRepository<Goal, Long> {

  List<Goal> findAllByUserIdAndRecordStatusTrueOrderByIdDesc(Long userId);

  Optional<Goal> findByIdAndRecordStatusTrue(Long id);
}
