package com.github.wenqiglantz.service.eventbridge.orderservice.restcontroller;

import com.github.wenqiglantz.service.eventbridge.orderservice.data.event.CustomerCreatedEvent;
import com.github.wenqiglantz.service.eventbridge.orderservice.data.event.CustomerDeletedEvent;
import com.github.wenqiglantz.service.eventbridge.orderservice.data.event.CustomerUpdatedEvent;
import com.github.wenqiglantz.service.eventbridge.orderservice.data.event.Event;
import com.github.wenqiglantz.service.eventbridge.orderservice.service.OrderService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;


@Slf4j
@RequiredArgsConstructor
@RestController
@Tag(name = "subscriber", description = "Operations pertaining to order service event consumption")
public class SubscriberController {

  private final OrderService orderService;

  @Operation(summary = "Consume customer created Event")
  @ApiResponses(value = {
          @ApiResponse(responseCode = "200", description = "Successfully consumed customer created event"),
          @ApiResponse(responseCode = "400", description = "Bad Request"),
          @ApiResponse(responseCode = "500", description = "Unexpected system exception"),
          @ApiResponse(responseCode = "502", description = "An error has occurred with an upstream service")
  })
  @PutMapping (path = "/customer-created")
  public void consumeCustomerCreatedEvent(@Valid @RequestBody Event<CustomerCreatedEvent> event) {
      try {
        log.info(">>> received event {}", event.getDetail());
        orderService.consumeCustomerCreatedEvent(event.getDetail());
      } catch (Exception e) {
        throw new RuntimeException(e);
      }
  }

  /*
  @RequestMapping(method = RequestMethod.PUT)
  public Mono<ResponseEntity<Object>> onEvent(@Valid @RequestBody Event<Order> event) {

    log.info("Processing order {}", event.getDetail().getOrderId());

    return orderRepository.save(event.getDetail())
            .map(order -> ResponseEntity.created(UriComponentsBuilder.fromPath("/events/{id}")
                    .build(order.getOrderId())).build())
            .onErrorResume(error -> {
              log.error("Unhandled error occurred", error);
              return Mono.just(ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build());
            });
  }
}

   */

  @Operation(summary = "Consume customer updated Event")
  @ApiResponses(value = {
          @ApiResponse(responseCode = "200", description = "Successfully consumed customer updated event"),
          @ApiResponse(responseCode = "400", description = "Bad Request"),
          @ApiResponse(responseCode = "500", description = "Unexpected system exception"),
          @ApiResponse(responseCode = "502", description = "An error has occurred with an upstream service")
  })
  @PutMapping(path = "/customer-updated")
  public void consumeCustomerUpdatedEvent(@Valid @RequestBody Event<CustomerUpdatedEvent> event) {
    try {
      log.info("received event {}", event.getDetail());
      orderService.consumeCustomerUpdatedEvent(event.getDetail());

    } catch (Exception e) {
      throw new RuntimeException(e);
    }
  }

  @Operation(summary = "Consume customer deleted Event")
  @ApiResponses(value = {
          @ApiResponse(responseCode = "200", description = "Successfully consumed customer deleted event"),
          @ApiResponse(responseCode = "400", description = "Bad Request"),
          @ApiResponse(responseCode = "500", description = "Unexpected system exception"),
          @ApiResponse(responseCode = "502", description = "An error has occurred with an upstream service")
  })
  @PutMapping(path = "/customer-deleted")
  public void consumeCustomerDeletedEvent(@Valid @RequestBody Event<CustomerDeletedEvent> event) {
    try {
      log.info("received event {}", event.getDetail());
      orderService.consumeCustomerDeletedEvent(event.getDetail());

    } catch (Exception e) {
      throw new RuntimeException(e);
    }
  }
}
