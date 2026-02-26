package com.example.fitness_application.master.workout;

import org.springframework.data.repository.ListCrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface WorkoutRepository extends ListCrudRepository<Workout, Long> {

  List<Workout> findAllByUserIdAndRecordStatusTrueOrderByIdDesc(Long userId);

  Optional<Workout> findByIdAndRecordStatusTrue(Long id);
}
