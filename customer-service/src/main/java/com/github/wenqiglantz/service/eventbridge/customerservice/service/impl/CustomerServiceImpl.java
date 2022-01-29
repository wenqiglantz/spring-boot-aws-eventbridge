package com.github.wenqiglantz.service.eventbridge.customerservice.service.impl;

import com.amazonaws.auth.DefaultAWSCredentialsProviderChain;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.eventbridge.AmazonEventBridge;
import com.amazonaws.services.eventbridge.AmazonEventBridgeClient;
import com.amazonaws.services.eventbridge.model.PutEventsRequest;
import com.amazonaws.services.eventbridge.model.PutEventsRequestEntry;
import com.amazonaws.services.eventbridge.model.PutEventsResult;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jdk8.Jdk8Module;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.github.wenqiglantz.service.eventbridge.customerservice.data.CustomerInfo;
import com.github.wenqiglantz.service.eventbridge.customerservice.data.CustomerStatus;
import com.github.wenqiglantz.service.eventbridge.customerservice.data.event.CustomerCreatedEvent;
import com.github.wenqiglantz.service.eventbridge.customerservice.data.event.CustomerDeletedEvent;
import com.github.wenqiglantz.service.eventbridge.customerservice.data.event.CustomerUpdatedEvent;
import com.github.wenqiglantz.service.eventbridge.customerservice.data.exception.NotFoundException;
import com.github.wenqiglantz.service.eventbridge.customerservice.persistence.entity.Customer;
import com.github.wenqiglantz.service.eventbridge.customerservice.persistence.repository.CustomerRepository;
import com.github.wenqiglantz.service.eventbridge.customerservice.service.CustomerService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.logging.log4j.util.Strings;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.UUID;

import static java.util.stream.Collectors.toList;

@Slf4j
@Component
@RequiredArgsConstructor
public class CustomerServiceImpl implements CustomerService {

    private final CustomerRepository customerRepository;

    @Override
    public CustomerInfo saveCustomer(CustomerInfo customerInfo) throws Exception {
        customerInfo.setCustomerId(Strings.isBlank(customerInfo.getCustomerId()) ? UUID.randomUUID().toString() : customerInfo.getCustomerId());
        Customer customer = Customer.builder()
                .customerId(customerInfo.getCustomerId())
                .firstName(customerInfo.getFirstName())
                .lastName(customerInfo.getLastName())
                .build();
        customerRepository.save(customer);

        CustomerCreatedEvent customerCreatedEvent = CustomerCreatedEvent.builder()
                .customerId(customerInfo.getCustomerId())
                .firstName(customerInfo.getFirstName())
                .lastName(customerInfo.getLastName())
                .status(CustomerStatus.CREATED)
                .build();
        log.info(">>> publish event to EventBridge {} ", customerCreatedEvent);

        AmazonEventBridge client = AmazonEventBridgeClient.builder()
                .withRegion(Regions.US_EAST_1)
                .withCredentials(new DefaultAWSCredentialsProviderChain())
                .build();

        PutEventsRequestEntry requestEntry = new PutEventsRequestEntry();
        requestEntry.withSource("customer-service")  //this needs to match the event pattern defined in step 4 below
                .withDetailType("customer-created-detail-type")  //this needs to match the event pattern defined in step 4 below
                .withDetail(toJson(customerCreatedEvent)) //this converts the event object into JSON string
                .withEventBusName("test-event-bus"); //this needs to match the custom event bus name created in step 4 below
        PutEventsRequest request = new PutEventsRequest();
        request.withEntries(requestEntry);

        PutEventsResult result = client.putEvents(request); //AmazonEventBridgeClient puts the event onto the event bus

        log.info(">>> PutEventsRequestEntry to EventBridge: " + requestEntry);
        log.info(">>> result from calling EventBridge: " + result.toString());

        return customerInfo;
    }

    @Override
    public CustomerInfo getCustomer(String customerId) {
        Customer customer =
                customerRepository.findByCustomerId(customerId).orElseThrow(() ->
                        new NotFoundException("Could not find customer with customerId: " + customerId));

        CustomerInfo customerInfo = CustomerInfo.builder()
                .customerId(customerId)
                .firstName(customer.getFirstName())
                .lastName(customer.getLastName())
                .build();
        return customerInfo;
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

    @Override
    public void updateCustomer(String customerId, CustomerInfo customerInfo) throws Exception {
        Customer customer =
                customerRepository.findByCustomerId(customerId).orElseThrow(() ->
                        new NotFoundException("Could not find customer with customerId: " + customerId));
        customer.setFirstName(customerInfo.getFirstName());
        customer.setLastName(customerInfo.getLastName());
        customerRepository.save(customer);

        CustomerUpdatedEvent customerUpdatedEvent = CustomerUpdatedEvent.builder()
                .customerId(customerInfo.getCustomerId())
                .firstName(customerInfo.getFirstName())
                .lastName(customerInfo.getLastName())
                .status(CustomerStatus.UPDATED)
                .build();

        log.info("publish event {} ", customerUpdatedEvent);
        //initialize dapr client in try-with-resource block to properly close the client at the end
    }

    @Override
    public void deleteCustomer(String customerId) throws Exception {
        Customer customer =
                customerRepository.findByCustomerId(customerId).orElseThrow(() ->
                        new NotFoundException("Could not find customer with customerId: " + customerId));
        customerRepository.delete(customer);

        CustomerDeletedEvent customerDeletedEvent = CustomerDeletedEvent.builder()
                .customerId(customerId)
                .status(CustomerStatus.DELETED)
                .build();

        log.info("publish event {} ", customerDeletedEvent);
        //initialize dapr client in try-with-resource block to properly close the client at the end

    }

    private static String toJson(Object obj) {
        try {
            return new ObjectMapper()
                    .registerModule(new Jdk8Module())
                    .registerModule(new JavaTimeModule())
                    .writerWithDefaultPrettyPrinter()
                    .writeValueAsString(obj);
        } catch (JsonProcessingException e) {
            log.error("Error converting toJson", e);
        }
        return "{}";
    }
}
