package com.github.wenqiglantz.service.eventbridge.orderservice.service.impl;

import com.github.wenqiglantz.service.eventbridge.orderservice.data.CustomerInfo;
import com.github.wenqiglantz.service.eventbridge.orderservice.data.event.CustomerCreatedEvent;
import com.github.wenqiglantz.service.eventbridge.orderservice.data.event.CustomerDeletedEvent;
import com.github.wenqiglantz.service.eventbridge.orderservice.data.event.CustomerUpdatedEvent;
import com.github.wenqiglantz.service.eventbridge.orderservice.persistence.entity.Customer;
import com.github.wenqiglantz.service.eventbridge.orderservice.persistence.repository.CustomerRepository;
import com.github.wenqiglantz.service.eventbridge.orderservice.service.OrderService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

import static java.util.stream.Collectors.toList;

@Slf4j
@RequiredArgsConstructor
@Component
public class OrderServiceImpl implements OrderService {

    private final CustomerRepository customerRepository;

    @Override
    public void consumeCustomerCreatedEvent(CustomerCreatedEvent customerCreatedEvent) {
        String customerId = customerCreatedEvent.getCustomerId();
        Optional<Customer> customerOptional = customerRepository.findByCustomerId(customerId);
        if (!(customerOptional).isPresent()) {
            log.debug("customer not existing: ", customerId);
            Customer customer = Customer.builder()
                    .customerId(customerCreatedEvent.getCustomerId())
                    .firstName(customerCreatedEvent.getFirstName())
                    .lastName(customerCreatedEvent.getLastName())
                    .build();
            customerRepository.save(customer);
        }
    }

    @Override
    public void consumeCustomerUpdatedEvent(CustomerUpdatedEvent customerUpdatedEvent) {
        String customerId = customerUpdatedEvent.getCustomerId();
        Optional<Customer> customerOptional = customerRepository.findByCustomerId(customerId);
        if (customerOptional.isPresent()) {
            log.info("Customer updated: ", customerId);
            customerOptional.get().setFirstName(customerUpdatedEvent.getFirstName());
            customerOptional.get().setLastName(customerUpdatedEvent.getLastName());
            customerRepository.save(customerOptional.get());
        }
    }

    @Override
    public void consumeCustomerDeletedEvent(CustomerDeletedEvent customerDeletedEvent) {
        String customerId = customerDeletedEvent.getCustomerId();
        Optional<Customer> customerOptional = customerRepository.findByCustomerId(customerId);
        if (customerOptional.isPresent()) {
            log.debug("customer deleted: ", customerId);
            customerRepository.delete(customerOptional.get());
        }
    }

    @Override
    public List<CustomerInfo> getCustomers() {
        List<Customer> customers = customerRepository.findAll();

        List<CustomerInfo> customerInfos = customers.stream()
                .map(customer -> CustomerInfo.builder()
                        .customerId(customer.getCustomerId())
                        .firstName(customer.getFirstName())
                        .lastName(customer.getLastName())
                        .build())
                .collect(toList());

        return customerInfos;
    }
}
