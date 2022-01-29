package com.github.wenqiglantz.service.eventbridge.orderservice.restcontroller;

import com.github.wenqiglantz.service.eventbridge.orderservice.service.OrderService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/orders",
        produces = MediaType.APPLICATION_JSON_VALUE)
@Tag(name = "customer", description = "Operations pertaining to customer service")
public class OrderController {

    private static final String JSON = MediaType.APPLICATION_JSON_VALUE;

    private final OrderService orderService;

    @Operation(summary = "Retrieve all customers")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved all customers"),
            @ApiResponse(responseCode = "401", description = "Authorization denied"),
            @ApiResponse(responseCode = "404", description = "Not Found"),
            @ApiResponse(responseCode = "500", description = "Unexpected system exception")
    })
    @GetMapping(produces = JSON)
    public ResponseEntity<Map> getCustomers() {
        Map data = new HashMap();
        data.put("data", orderService.getCustomers());
        return ResponseEntity.ok(data);
    }
}
