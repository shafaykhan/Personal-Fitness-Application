package com.example.fitness_application.common.exception.response;

import com.example.fitness_application.common.util.RequestUtils;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;
import java.util.Map;

@Data
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class ErrorResponse {

  private final String timestamp;
  private final String statusCode;
  private final int status;
  private final String message;
  private String path;
  private Map<String, String> validationErrors;

  public ErrorResponse(HttpStatus status, String message) {
    this(status, message, null);
  }

  public ErrorResponse(HttpStatus status, String message, Map<String, String> validationErrors) {
    this.timestamp = String.valueOf(LocalDateTime.now());
    this.statusCode = status.name();
    this.status = status.value();
    this.message = message;
    this.path = RequestUtils.getRequestUri();
    this.validationErrors = validationErrors;
  }
}
