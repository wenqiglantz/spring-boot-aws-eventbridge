package com.github.wenqiglantz.service.eventbridge.orderservice.service;

import com.github.wenqiglantz.service.eventbridge.orderservice.data.CustomerInfo;
import com.github.wenqiglantz.service.eventbridge.orderservice.data.event.CustomerCreatedEvent;
import com.github.wenqiglantz.service.eventbridge.orderservice.data.event.CustomerDeletedEvent;
import com.github.wenqiglantz.service.eventbridge.orderservice.data.event.CustomerUpdatedEvent;

import java.util.List;

public interface OrderService {

    void consumeCustomerCreatedEvent(CustomerCreatedEvent customerCreatedEvent);

    void consumeCustomerUpdatedEvent(CustomerUpdatedEvent customerUpdatedEvent);

    void consumeCustomerDeletedEvent(CustomerDeletedEvent customerDeletedEvent);

    List<CustomerInfo> getCustomers();
}