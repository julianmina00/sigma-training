package com.sigma.training.invoicepublisher.route;

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
public class PushishingRoute extends RouteBuilder {

  private final int port;
  private final String bootstrapServers;
  private final String invoiceTopicName;
  private final PublisherRouteProcessor publisherRouteProcessor;

  @Autowired
  public PushishingRoute(@Value("${server.port}") int port,
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
  }

  private void addInvoiceRoute(){
    RestDefinition restDefinition = rest("/api")
        .post("/invoice")
        .consumes("text/plain")
        .produces("application/json")
        .bindingMode(RestBindingMode.off)
        .description("Post a single invoice")
        .outType(String.class);

    restDefinition.param()
        .name("body").type(RestParamType.body).description("Body of the endpoint").example("5741018630493083527:11.64:Shampoo,Orange juice,Shaving cream")
        .dataType("string").required(true).endParam();

    RouteDefinition routeDefinition = restDefinition.route();
    routeDefinition
        .routeId("invoicePublisherRoute")
        .log("Prepering invoice to be posted to Kafka")
        .process(publisherRouteProcessor)
        .to(getKakfaConfiguration())
        .log("The invoice was successfully sent to Kafka");
    addExceptionHandler(routeDefinition);
  }

  private String getKakfaConfiguration() {
    return "kafka:"+invoiceTopicName+"?brokers="+bootstrapServers+"&serializerClass="+ StringSerializer.class.getName();
  }

  private void addExceptionHandler(RouteDefinition routeDefinition) {
    log.error("Exception processing the message");
  }

}
