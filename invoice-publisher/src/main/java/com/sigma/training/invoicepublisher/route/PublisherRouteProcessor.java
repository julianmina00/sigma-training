package com.sigma.training.invoicepublisher.route;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sigma.training.invoicepublisher.model.Invoice;
import java.util.Arrays;
import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.springframework.stereotype.Component;

@Component
public class PublisherRouteProcessor implements Processor {

  private static final String FIELD_SEPARATOR = ":";
  private static final String ITEMS_SEPARATOR = ",";
  private final ObjectMapper mapper = new ObjectMapper();

  @Override
  public void process(Exchange exchange) throws Exception {
    String body = exchange.getIn().getBody(String.class);
    String[] field = body.split(FIELD_SEPARATOR);
    String[] items = field[2].split(ITEMS_SEPARATOR);

    Invoice invoice = new Invoice();
    invoice.setNumber(field[0]);
    invoice.setAmount(field[1]);
    invoice.setItems(Arrays.asList(items));

    String json = mapper.writeValueAsString(invoice);
    exchange.getIn().setBody(json, String.class);
  }
}
