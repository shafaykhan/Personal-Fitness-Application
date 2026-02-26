package com.example.fitness_application.master.diet_record;

import org.springframework.data.repository.ListCrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface DietRecordRepository extends ListCrudRepository<DietRecord, Long> {

  List<DietRecord> findAllByUserIdAndRecordStatusTrueOrderByIdDesc(Long userId);

  Optional<DietRecord> findByIdAndRecordStatusTrue(Long id);
}
