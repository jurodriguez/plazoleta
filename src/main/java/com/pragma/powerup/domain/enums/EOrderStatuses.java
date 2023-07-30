package com.pragma.powerup.domain.enums;

public enum EOrderStatuses {
    PENDING(1L, "PENDING"),
    IN_PREPARATION(2L, "IN_PREPARATION"),
    READY(3L, "READY"),
    CUSTOMER(4L, "Customer");

    private final Long id;

    private final String name;

    EOrderStatuses(Long id, String name) {
        this.id = id;
        this.name = name;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }
}
