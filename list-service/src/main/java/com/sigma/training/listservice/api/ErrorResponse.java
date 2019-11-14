package com.sigma.training.listservice.api;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class ErrorResponse {

  private int errorCode;
  private String message;

}
