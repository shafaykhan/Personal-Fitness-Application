package com.example.fitness_application.master.health_record;

import org.springframework.data.repository.ListCrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface HealthRecordRepository extends ListCrudRepository<HealthRecord, Long> {

  List<HealthRecord> findAllByUserIdAndRecordStatusTrueOrderByIdDesc(Long userId);

  Optional<HealthRecord> findByIdAndRecordStatusTrue(Long id);
}
