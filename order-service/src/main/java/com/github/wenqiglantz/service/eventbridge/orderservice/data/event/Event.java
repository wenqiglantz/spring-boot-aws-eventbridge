package com.github.wenqiglantz.service.eventbridge.orderservice.data.event;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.util.Collection;
import java.util.Date;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class Event<T> {

    @NotNull
    private String version;

    @NotNull
    private String region;

    @NotNull
    private String account;

    @NotNull
    private String id;

    @NotNull
    private String source;

    @NotNull
    @JsonProperty("detail-type")
    private String detailType;

    @NotNull
    private Collection<String> resources;

    @NotNull
    private Date time;

    @NotNull
    private T detail;
}