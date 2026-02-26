package com.example.fitness_application.common.exception.advice;

import com.example.fitness_application.common.exception.custom.ConflictException;
import com.example.fitness_application.common.exception.custom.NotFoundException;
import com.example.fitness_application.common.exception.custom.UnprocessableException;
import com.example.fitness_application.common.exception.response.ErrorResponse;
import com.example.fitness_application.common.util.RequestUtils;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@RestControllerAdvice
public class CommonErrorHandler {

  @ExceptionHandler({AuthenticationException.class, BadCredentialsException.class})
  public ResponseEntity<ErrorResponse> handleAuthenticationException(AuthenticationException ex) {
    return errorResponse(HttpStatus.UNAUTHORIZED, ex, null);
  }

  @ExceptionHandler(AccessDeniedException.class)
  public ResponseEntity<ErrorResponse> handleAccessDenied(AccessDeniedException ex) {
    return errorResponse(HttpStatus.FORBIDDEN, ex, null);
  }

  @ExceptionHandler(MethodArgumentNotValidException.class)
  public ResponseEntity<ErrorResponse> handleValidationExceptions(MethodArgumentNotValidException ex) {

    Map<String, String> errors = new HashMap<>();
    ex.getBindingResult().getAllErrors().forEach(error -> {
      String fieldName = ((FieldError) error).getField();
      String errorMessage = error.getDefaultMessage();
      errors.put(fieldName, errorMessage);
    });

    return errorResponse(HttpStatus.BAD_REQUEST, ex, "Validation failed for the request.", errors);
  }

  @ExceptionHandler(HttpMessageNotReadableException.class)
  public ResponseEntity<ErrorResponse> handleHttpMessageNotReadableException(HttpMessageNotReadableException ex) {
    log.error("Malformed JSON request | uri={} | message={}", RequestUtils.getRequestUri(), ex.getMessage());
    return errorResponse(HttpStatus.BAD_REQUEST, ex,
            "Malformed JSON request. Please check your request body format.");
  }

  @ExceptionHandler(MethodArgumentTypeMismatchException.class)
  public ResponseEntity<ErrorResponse> handleTypeMismatch(
          MethodArgumentTypeMismatchException ex, HttpServletRequest request) {
    return errorResponse(HttpStatus.BAD_REQUEST, ex, "Invalid value for parameter: " + ex.getName());
  }

  @ExceptionHandler(MissingServletRequestParameterException.class)
  public ResponseEntity<ErrorResponse> handleMissingParam(MissingServletRequestParameterException ex,
                                                          HttpServletRequest request) {
    return errorResponse(HttpStatus.BAD_REQUEST, ex, ex.getParameterName() + " parameter is required");
  }

  @ExceptionHandler(NotFoundException.class)
  public ResponseEntity<ErrorResponse> handleNotFoundException(NotFoundException ex) {
    return errorResponse(HttpStatus.NOT_FOUND, ex);
  }

  @ExceptionHandler(ConflictException.class)
  public ResponseEntity<ErrorResponse> handleConflictException(ConflictException ex) {
    return errorResponse(HttpStatus.CONFLICT, ex);
  }

  @ExceptionHandler(UnprocessableException.class)
  public ResponseEntity<ErrorResponse> handleUnprocessableException(UnprocessableException ex) {
    return errorResponse(HttpStatus.UNPROCESSABLE_ENTITY, ex);
  }

  @ExceptionHandler(Exception.class)
  public ResponseEntity<ErrorResponse> handleAllExceptions(Exception ex) {
    return errorResponse(HttpStatus.INTERNAL_SERVER_ERROR, ex,
            "An unexpected internal server error occurred.");
  }

  private ResponseEntity<ErrorResponse> errorResponse(HttpStatus httpStatus, Throwable ex) {
    return errorResponse(httpStatus, ex, null);
  }

  private ResponseEntity<ErrorResponse> errorResponse(HttpStatus httpStatus, Throwable ex, String message) {
    ErrorResponse errorResponse = new ErrorResponse(httpStatus, message == null ? ex.getMessage() : message);
    return new ResponseEntity<>(errorResponse, httpStatus);
  }

  private ResponseEntity<ErrorResponse> errorResponse(HttpStatus httpStatus, Throwable ex, String message,
                                                      Map<String, String> errors) {
    ErrorResponse errorResponse = new ErrorResponse(httpStatus, message == null ? ex.getMessage() : message, errors);
    return new ResponseEntity<>(errorResponse, httpStatus);
  }

  private String extractConstraintName(String dbMessage) {
    if (dbMessage == null) return null;

    int start = dbMessage.indexOf('"');
    int end = dbMessage.lastIndexOf('"');

    if (start != -1 && end != -1 && start < end)
      return dbMessage.substring(start + 1, end);

    return null;
  }

}
