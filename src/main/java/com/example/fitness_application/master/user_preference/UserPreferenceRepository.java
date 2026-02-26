package com.example.fitness_application.master.user_preference;

import org.springframework.data.repository.ListCrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserPreferenceRepository extends ListCrudRepository<UserPreference, Long> {

  Optional<UserPreference> findByUserId(Long userId);
}
