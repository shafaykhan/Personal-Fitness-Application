package com.example.fitness_application.identity.token;

import com.example.fitness_application.common.exception.custom.UnprocessableException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.UUID;

@Service
public class TokenService {

  private final TokenRepository repository;
  @Value("${app.token.expiry.minutes}")
  private int tokenExpiryMinutes;

  public TokenService(TokenRepository repository) {
    this.repository = repository;
  }

  public Token generateAndSaveToken(long userId) {
    Instant now = Instant.now();
    Token prepareToken = Token.builder()
            .userId(userId)
            .token(UUID.randomUUID().toString())
            .revoked(false)
            .lastUpdatedOn(now)
            .createdAt(now)
            .build();
    return repository.save(prepareToken);
  }

  public Token validateTokenAndUpdateLastUpdatedOn(Token token) {
    Instant now = Instant.now();
    if (token.getLastUpdatedOn() != null &&
            token.getLastUpdatedOn().isBefore(now.minus(tokenExpiryMinutes, ChronoUnit.MINUTES))) {
      repository.delete(token);
      throw new UnprocessableException("Token expired!");
    }

    token.setLastUpdatedOn(now);
    return repository.save(token);
  }

  @Transactional
  public void revokeAllUserTokens(long userId) {
    List<Token> tokenList = repository.findAllByUserIdAndRevokedIsFalse(userId);
    tokenList.forEach(token -> token.setRevoked(true));
    repository.saveAll(tokenList);
  }

  public void revokeToken(Token token) {
    token.setRevoked(true);
    repository.save(token);
  }

  public Token findByToken(String token) {
    return repository.findByToken(token)
            .orElseThrow(() -> new BadCredentialsException("Invalid token!"));
  }
}
