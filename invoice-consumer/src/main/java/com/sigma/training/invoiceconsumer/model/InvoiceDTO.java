package com.sigma.training.invoiceconsumer.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.util.List;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class InvoiceDTO {

  private String number;
  private String amount;
  private List<String> items;

}
