package com.pragma.powerup.infrastructure.out.feignclients.adapter;

import com.pragma.powerup.domain.model.Traceability;
import com.pragma.powerup.domain.spi.feignclients.ITraceabilityFeignClientPort;
import com.pragma.powerup.infrastructure.out.feignclients.TraceabilityFeignClients;
import com.pragma.powerup.infrastructure.out.feignclients.mapper.ITraceabilityMapper;
import lombok.RequiredArgsConstructor;

import java.util.List;

@RequiredArgsConstructor
public class TraceabilityFeignAdapter implements ITraceabilityFeignClientPort {

    private final TraceabilityFeignClients traceabilityFeignClients;

    private final ITraceabilityMapper traceabilityDtoMapper;

    @Override
    public void saveTraceability(Traceability traceability) {
        traceabilityFeignClients.saveTraceability(traceabilityDtoMapper.toTraceabilityDto(traceability));
    }

    @Override
    public List<Traceability> getAllTraceability(Long orderId) {
        return traceabilityDtoMapper.toTraceabilityResponseDtoList(traceabilityFeignClients.getAllTraceability(orderId));
    }

    @Override
    public String timeDifferenceForOrders(Long orderId) {
        return traceabilityFeignClients.timeDifferenceForOrders(orderId);
    }
}
