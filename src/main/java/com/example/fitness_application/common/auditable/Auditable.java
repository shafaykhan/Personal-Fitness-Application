package com.example.fitness_application.common.auditable;

import lombok.Data;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;

import java.time.LocalDateTime;

@Data
public abstract class Auditable {

  @CreatedBy
  private Long createdBy;

  @CreatedDate
  private LocalDateTime createdAt;

  @LastModifiedBy
  private Long lastModifiedBy;

  @LastModifiedDate
  private LocalDateTime lastModifiedAt;
}
