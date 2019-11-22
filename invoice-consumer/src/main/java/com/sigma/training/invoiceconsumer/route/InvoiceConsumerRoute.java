package com.sigma.training.invoiceconsumer.route;

import org.apache.camel.CamelContext;
import org.apache.camel.spring.SpringCamelContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

@Component
public class InvoiceConsumerRoute {

  private static final Logger LOGGER = LoggerFactory.getLogger(InvoiceConsumerRoute.class);

  private final CamelContext camelContext;
  private final InvoiceConsumerRouteDefinition routeDefinition;

  @Autowired
  public InvoiceConsumerRoute(ApplicationContext applicationContext, InvoiceConsumerRouteDefinition routeDefinition) {
    this.camelContext = new SpringCamelContext(applicationContext);
    this.routeDefinition = routeDefinition;

    try {
      camelContext.setAutoStartup(false);
      camelContext.start();
      camelContext.addRouteDefinition(routeDefinition.getRouteDefinition());
      camelContext.startRoute(routeDefinition.getRouteId());

    } catch (Exception ex) {
      LOGGER.error("Error creating the Camel Context. Exception: "+ex.getLocalizedMessage(), ex);
    }

  }
}
