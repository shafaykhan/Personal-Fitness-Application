package com.example.fitness_application.master.user_preference;

import com.example.fitness_application.common.auditable.Auditable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;

@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserPreference extends Auditable {

  @Id
  private Long id;

  private Long userId;
  private Long dietTypeId;
  private Long intensityId;
  private Long goalTypeId;

  private String allergies;
}
