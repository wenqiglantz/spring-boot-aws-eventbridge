package com.github.wenqiglantz.service.eventbridge.orderservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class OrderServiceApplication {

  public static void main(String[] args) throws Exception {
    SpringApplication app = new SpringApplication(OrderServiceApplication.class);
    app.run();
  }
}
