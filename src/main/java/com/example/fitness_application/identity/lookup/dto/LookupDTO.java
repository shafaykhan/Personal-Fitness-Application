package com.example.fitness_application.identity.lookup.dto;

import com.example.fitness_application.common.enums.Status;
import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class LookupDTO {

  private Long id;

  @NotBlank
  private String groupKey;
  @NotBlank
  private String value;
  @NotNull
  private Status status;
}
