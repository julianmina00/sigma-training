package com.sigma.training.invoiceconsumer.route;

import com.sigma.training.invoiceconsumer.route.processor.InvoiceToListProcessor;
import org.apache.camel.Exchange;
import org.apache.camel.model.RouteDefinition;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class InvoiceConsumerRouteDefinition {

  private static final Logger LOGGER = LoggerFactory.getLogger(InvoiceConsumerRouteDefinition.class);

  private static final String ROUTE_ID = "InvoiceConsumerRouteDefinition";
  private final String listServiceEndpoint;
  private final String kafkaInvoiceTopic;
  private final String kafkaBootstrapServers;
  private final InvoiceToListProcessor invoiceToListProcessor;

  @Autowired
  public InvoiceConsumerRouteDefinition(
      @Value("${list.service.add.list.endpoint}") String listServiceEndpoint,
      @Value("${kafka.invoice.topic}") String kafkaInvoiceTopic,
      @Value("${kafka.bootstrap.servers}") String kafkaBootstrapServers,
      InvoiceToListProcessor invoiceToListProcessor) {
    this.listServiceEndpoint = listServiceEndpoint;
    this.kafkaInvoiceTopic = kafkaInvoiceTopic;
    this.kafkaBootstrapServers = kafkaBootstrapServers;
    this.invoiceToListProcessor = invoiceToListProcessor;
  }

  public String getRouteId(){
    return ROUTE_ID;
  }

  public RouteDefinition getRouteDefinition(){
    RouteDefinition definition = new RouteDefinition();
    definition
        .from(getKafkaConfiguration())
        .routeId(getRouteId())
        .log("Start consuming messages from Kafka")
        .process(invoiceToListProcessor)
        .toD("http4:"+listServiceEndpoint)
        .log("Messages was successfully sent to the List Service.  ${body}");
    addExceptionHandle(definition);
    return definition;
  }

  private String getKafkaConfiguration() {
    return
        "kafka:".concat(kafkaInvoiceTopic)
        .concat("?brokers=").concat(kafkaBootstrapServers)
        .concat("&groupId=").concat(getRouteId())
        .concat("&maxPollIntervalMs=").concat(Integer.toString(Integer.MAX_VALUE))
        .concat("&autoOffsetReset=earliest");
  }

  private void addExceptionHandle(RouteDefinition definition) {
    definition.onException(Exception.class)
        .handled(true)
        .process(exchange -> {
          Exception ex = (Exception)exchange.getProperty(Exchange.EXCEPTION_CAUGHT);
          LOGGER.error("Upss... there was an error: "+ ex.getLocalizedMessage(), ex);
        });
  }


}
