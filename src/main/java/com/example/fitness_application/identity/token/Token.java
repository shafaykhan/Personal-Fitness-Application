package com.example.fitness_application.identity.token;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;

import java.time.Instant;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Token {

  @Id
  private Long id;
  private Long userId;

  private String token;

  private Boolean revoked = false;

  private Instant lastUpdatedOn;
  private Instant createdAt = Instant.now();
}
