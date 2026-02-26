package com.example.fitness_application.identity.token;

import org.springframework.data.repository.ListCrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TokenRepository extends ListCrudRepository<Token, Long> {

  Optional<Token> findByToken(String token);

  List<Token> findAllByUserIdAndRevokedIsFalse(Long userId);
}
