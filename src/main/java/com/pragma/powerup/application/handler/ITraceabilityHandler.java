package com.pragma.powerup.application.handler;

import com.pragma.powerup.application.dto.response.TraceabilityResponseDto;

import java.util.List;

public interface ITraceabilityHandler {
    List<TraceabilityResponseDto> getAllTraceability(Long orderId);
}
