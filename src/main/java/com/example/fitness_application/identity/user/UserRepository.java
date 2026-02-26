package com.example.fitness_application.identity.user;

import org.springframework.data.repository.ListCrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends ListCrudRepository<User, Long> {

  List<User> findAllByOrderByIdDesc();

  Optional<User> findByUsername(String username);

  int countByIdNotAndUsername(Long id, String username);

  int countByIdNotAndEmail(Long id, String email);

  int countByIdNotAndContactNo(Long id, String contactNo);
}
