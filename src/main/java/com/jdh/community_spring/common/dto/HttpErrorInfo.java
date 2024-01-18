package com.jdh.community_spring.common.dto;

import lombok.Getter;
import org.springframework.http.HttpStatus;
import java.time.LocalDateTime;



@Getter
public class HttpErrorInfo {
  private final LocalDateTime timestamp;
  private final String path;
  private final HttpStatus httpStatus;
  private final String message;

  public HttpErrorInfo(HttpStatus httpStatus, String path, String message) {
    timestamp = LocalDateTime.now();
    this.httpStatus = httpStatus;
    this.path = path;
    this.message = message;
  }
}
