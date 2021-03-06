package com.github.wenqiglantz.service.eventbridge.customerservice.data;

public enum CustomerStatus {
    CREATED,
    UPDATED,
    DELETED;

    public String value() {
        return name();
    }

    public static CustomerStatus fromValue(String v) {
        return valueOf(v);
    }
}
