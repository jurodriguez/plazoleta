package com.pragma.powerup.domain.spi.feignclients;

import com.pragma.powerup.domain.model.Traceability;

import java.util.List;

public interface ITraceabilityFeignClientPort {
    void saveTraceability(Traceability traceability);

    List<Traceability> getAllTraceability(Long orderId);
}
