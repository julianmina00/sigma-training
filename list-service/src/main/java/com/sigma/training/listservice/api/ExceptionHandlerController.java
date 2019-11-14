package com.sigma.training.listservice.api;

import com.sigma.training.listservice.exception.NotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class ExceptionHandlerController {

  @ExceptionHandler(NotFoundException.class)
  public ResponseEntity<ErrorResponse> notFoundExceptionHandler(NotFoundException ex){
    ErrorResponse response = new ErrorResponse();
    response.setErrorCode(HttpStatus.BAD_REQUEST.value());
    response.setMessage(ex.getLocalizedMessage());
    return ResponseEntity.ok(response);
  }

  @ExceptionHandler(Exception.class)
  public ResponseEntity<ErrorResponse> exceptionHandler(Exception ex){
    ErrorResponse response = new ErrorResponse();
    response.setErrorCode(HttpStatus.INTERNAL_SERVER_ERROR.value());
    response.setMessage("upss... there was an error in the server!!!");
    return ResponseEntity.ok(response);
  }

}
