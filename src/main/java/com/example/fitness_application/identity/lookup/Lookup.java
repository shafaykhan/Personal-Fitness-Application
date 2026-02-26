package com.example.fitness_application.identity.lookup;

import com.example.fitness_application.common.auditable.Auditable;
import com.example.fitness_application.common.enums.Status;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;

@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Lookup extends Auditable {

  @Id
  private Long id;

  private String groupKey;
  private String value;

  private Status status;

  private Boolean recordStatus;
}
