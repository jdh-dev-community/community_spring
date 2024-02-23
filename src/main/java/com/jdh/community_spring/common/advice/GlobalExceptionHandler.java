package com.jdh.community_spring.common.advice;

import com.jdh.community_spring.common.dto.HttpErrorInfo;
import com.jdh.community_spring.common.exception.NotFoundException;
import lombok.extern.slf4j.Slf4j;

import org.springframework.dao.InvalidDataAccessApiUsageException;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.WebRequest;


import javax.persistence.EntityNotFoundException;

import static org.springframework.http.HttpStatus.*;


@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

  @ResponseStatus(NOT_FOUND)
  @ExceptionHandler(EntityNotFoundException.class)
  public @ResponseBody HttpErrorInfo handleEntityNotFoundException(WebRequest req, NotFoundException ex) {
    return createHttpErrorInfo(NOT_FOUND, req, ex);
  }

  @ResponseStatus(BAD_REQUEST)
  @ExceptionHandler(BindException.class)
  public @ResponseBody HttpErrorInfo handleBindException(WebRequest req, BindException ex) {
    return createHttpErrorInfo(BAD_REQUEST, req, ex);
  }

  @ResponseStatus(NOT_FOUND)
  @ExceptionHandler(NotFoundException.class)
  public @ResponseBody HttpErrorInfo handleNotFoundException(WebRequest req, NotFoundException ex) {
    return createHttpErrorInfo(NOT_FOUND, req, ex);
  }

  @ResponseStatus(BAD_REQUEST)
  @ExceptionHandler(InvalidDataAccessApiUsageException.class)
  public @ResponseBody HttpErrorInfo handleInvalidDataAccessApiUsageException(WebRequest req, InvalidDataAccessApiUsageException ex) {
    return createHttpErrorInfo(BAD_REQUEST, req, ex);
  }

  @ResponseStatus(BAD_REQUEST)
  @ExceptionHandler(IllegalArgumentException.class)
  public @ResponseBody HttpErrorInfo handleIllegalArgumentException(WebRequest req, IllegalArgumentException ex) {
    return createHttpErrorInfo(BAD_REQUEST, req, ex);
  }

  @ResponseStatus(BAD_REQUEST)
  @ExceptionHandler(MethodArgumentNotValidException.class)
  public @ResponseBody HttpErrorInfo handleMethodArgumentNotValidException(WebRequest req, MethodArgumentNotValidException ex) {
    return createHttpErrorInfo(BAD_REQUEST, req, ex);
  }
  @ResponseStatus(INTERNAL_SERVER_ERROR)
  @ExceptionHandler(Exception.class)
  public HttpErrorInfo handleAllException(WebRequest req, Exception ex) {
    return createHttpErrorInfo(INTERNAL_SERVER_ERROR, req, ex);
  }

  private HttpErrorInfo createHttpErrorInfo(HttpStatus httpStatus, WebRequest req, Exception ex) {
    final String path = req.getDescription(false).replace("uri=", "");
    final String message = ex.getMessage();

    log.debug("Returning HTTP status: {} for path: {}, message: {}", httpStatus, path, message);
    return new HttpErrorInfo(httpStatus, path, message);
  }
}
