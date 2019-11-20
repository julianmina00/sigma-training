package com.sigma.training.invoicepublisher.model;

import java.util.List;
import lombok.Data;

@Data
public class Invoice {

  private String number;
  private String amount;
  private List<String> items;

}
