package com.pragma.powerup.domain.api;

import com.pragma.powerup.domain.model.Traceability;

import java.util.List;

public interface ITraceabilityServicePort {
    List<Traceability> getAllTraceability(Long orderId);
}
