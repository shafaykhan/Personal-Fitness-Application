package com.example.fitness_application.common.util;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.util.Collections;
import java.util.Map;

public final class RequestUtils {

  private RequestUtils() {
  }

  public static HttpServletRequest getCurrentRequest() {
    ServletRequestAttributes attrs =
            (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();

    return attrs != null ? attrs.getRequest() : null;
  }

  public static String getRequestUri() {
    HttpServletRequest request = getCurrentRequest();
    return request != null ? request.getRequestURI() : null;
  }

  public static String getHttpMethod() {
    HttpServletRequest request = getCurrentRequest();
    return request != null ? request.getMethod() : null;
  }

  public static Map<String, String[]> getRequestParams() {
    HttpServletRequest request = getCurrentRequest();
    return request != null ? request.getParameterMap() : Collections.emptyMap();
  }
}
