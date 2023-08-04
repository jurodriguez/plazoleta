package com.pragma.powerup.application.handler.impl;

import com.pragma.powerup.application.dto.response.TraceabilityResponseDto;
import com.pragma.powerup.application.handler.ITraceabilityHandler;
import com.pragma.powerup.application.mapper.ITraceabilityResponseMapper;
import com.pragma.powerup.domain.api.ITraceabilityServicePort;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
@Validated
public class TraceabilityHandler implements ITraceabilityHandler {

    private final ITraceabilityServicePort traceabilityServicePort;

    private final ITraceabilityResponseMapper traceabilityResponseMapper;

    @Override
    public List<TraceabilityResponseDto> getAllTraceability(Long orderId) {
        return traceabilityResponseMapper.toTraceabilityResponseList(traceabilityServicePort.getAllTraceability(orderId));
    }
}
