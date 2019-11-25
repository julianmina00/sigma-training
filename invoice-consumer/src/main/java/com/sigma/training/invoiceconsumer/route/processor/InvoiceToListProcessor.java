package com.sigma.training.invoiceconsumer.route.processor;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sigma.training.invoiceconsumer.model.InvoiceDTO;
import com.sigma.training.invoiceconsumer.model.ItemDTO;
import com.sigma.training.invoiceconsumer.model.ListDTO;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Component;

@Component
public class InvoiceToListProcessor implements Processor {

  private final ObjectMapper mapper = new ObjectMapper();

  @Override
  public void process(Exchange exchange) throws Exception {

    // Translation part
    String body = exchange.getIn().getBody(String.class);
    InvoiceDTO invoice = mapper.readValue(body, InvoiceDTO.class);
    ListDTO list = new ListDTO();
    list.setName("Invoice Number: "+invoice.getNumber());
    list.setDescription("Shopping List for invoice number: "+invoice.getNumber()+". Total amount: "+invoice.getAmount());
    List<ItemDTO> items =  new ArrayList<>();
    Map<String, Integer> itemCounter = new HashMap<>();
    invoice.getItems().forEach(item -> {
      itemCounter.putIfAbsent(item, 0);
      itemCounter.computeIfPresent(item, (listItem, count) -> count + 1);
    });
    itemCounter.forEach((item, count) -> items.add(new ItemDTO(item, " x "+count)));
    list.setItems(items);



    //Configuration part
    String json = mapper.writeValueAsString(list);
    exchange.getIn().setBody(json, String.class);
    exchange.getIn().removeHeaders("*");
    exchange.getIn().setHeader(Exchange.CONTENT_TYPE, "application/json");
    exchange.getIn().setHeader(Exchange.HTTP_METHOD, HttpMethod.POST);

  }
}
