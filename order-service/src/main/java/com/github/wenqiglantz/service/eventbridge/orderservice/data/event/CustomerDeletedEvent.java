package com.github.wenqiglantz.service.eventbridge.orderservice.data.event;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.github.wenqiglantz.service.eventbridge.orderservice.data.CustomerStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Size;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonPropertyOrder({"customerId", "status"})
public class CustomerDeletedEvent {

    @Size(max = 36)
    private String customerId;

    private CustomerStatus status = CustomerStatus.DELETED;

}
