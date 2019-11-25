package com.sigma.training.invoicepublisher.route;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sigma.training.invoicepublisher.model.ErrorMessage;
import java.io.IOException;
import java.util.Map;
import org.apache.camel.Exchange;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.model.RouteDefinition;
import org.apache.camel.model.rest.RestBindingMode;
import org.apache.camel.model.rest.RestDefinition;
import org.apache.camel.model.rest.RestParamType;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class PublishingRoute extends RouteBuilder {

  private final int port;
  private final String bootstrapServers;
  private final String invoiceTopicName;
  private final PublisherRouteProcessor publisherRouteProcessor;

  @Autowired
  public PublishingRoute(@Value("${server.port}") int port,
      @Value("${kafka.bootstrap.servers}") String bootstrapServers,
      @Value("${kafka.invoice.topic}") String invoiceTopicName,
      PublisherRouteProcessor publisherRouteProcessor) {
    this.port = port;
    this.bootstrapServers = bootstrapServers;
    this.invoiceTopicName = invoiceTopicName;
    this.publisherRouteProcessor = publisherRouteProcessor;
  }

  @Override
  public void configure() throws Exception {

    restConfiguration()
        .component("netty4-http")
        .port(port)
        .bindingMode(RestBindingMode.json)
        .dataFormatProperty("prettyPrint","true")
        .contextPath("/")
        .apiContextPath("/api-doc")
        .apiProperty("api.title","Invoice Publisher API")
        .apiProperty("api.version","1.0.0")
        .apiProperty("cors","true");
    addInvoiceRoute();
    addBulkInvoiceRoute();
  }

  private void addBulkInvoiceRoute() {
    RestDefinition restDefinition = getRestDefinition("/invoice/bulk","Post a bunch of invoices");
    RouteDefinition routeDefinition = restDefinition.route();
    routeDefinition
        .routeId("invoiceBulkPublisherRoute")
        .split(body().tokenize("\n")).streaming()
        .log("Prepering invoice to be posted to Kafka")
        .process(publisherRouteProcessor)
        .to(getKakfaConfiguration())
        .log("The invoice was successfully sent to Kafka");
    addExceptionHandler(routeDefinition);

  }

  private void addInvoiceRoute(){
    RestDefinition restDefinition = getRestDefinition("/invoice","Post a single invoice");
    RouteDefinition routeDefinition = restDefinition.route();
    routeDefinition
        .routeId("invoicePublisherRoute")
        .log("Prepering invoice to be posted to Kafka")
        .process(publisherRouteProcessor)
        .to(getKakfaConfiguration())
        .log("The invoice was successfully sent to Kafka");
    addExceptionHandler(routeDefinition);
  }

  private RestDefinition getRestDefinition(String endpoint, String description) {
    RestDefinition restDefinition = rest("/api")
        .post(endpoint)
        .consumes("text/plain")
        .produces("application/json")
        .bindingMode(RestBindingMode.off)
        .description(description)
        .outType(String.class);

    restDefinition.param()
        .name("body").type(RestParamType.body).description("Body of the endpoint")
        .dataType("string").required(true).endParam();
    return restDefinition;
  }

  private String getKakfaConfiguration() {
    return "kafka:"+invoiceTopicName+"?brokers="+bootstrapServers+"&serializerClass="+ StringSerializer.class.getName();
  }

  private void addExceptionHandler(RouteDefinition routeDefinition) {
    routeDefinition.onException(Exception.class)
        .handled(true)
        .process(exchange -> {
          Exception exception = (Exception)exchange.getProperty(Exchange.EXCEPTION_CAUGHT);
          String message = "Uppssss... there was an exception: " + exception.getLocalizedMessage();
          ErrorMessage errorMessage = new ErrorMessage(message, exception.getClass().getName());
          ObjectMapper mapper = new ObjectMapper();
          exchange.getIn().setBody(mapper.writeValueAsString(errorMessage), String.class);
          log.error(message, exception);
        });
    log.error("Exception processing the message");
  }

}
