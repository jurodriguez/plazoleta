package com.pragma.powerup.infrastructure.out.feignclients;

import com.pragma.powerup.application.dto.request.TraceabilityRequestDto;
import com.pragma.powerup.infrastructure.out.feignclients.dto.TraceabilityDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import javax.validation.Valid;
import java.util.List;

@FeignClient(name = "traceability-service", url = "localhost:8083/api/v1/traceability")
public interface TraceabilityFeignClients {

    @PostMapping("/")
    ResponseEntity<Void> saveTraceability(@Valid @RequestBody TraceabilityRequestDto traceabilityRequestDto);

    @GetMapping("/getAllTraceability")
    List<TraceabilityDto> getAllTraceability(@RequestParam Long orderId);

    @GetMapping("/timeDifferenceForOrders")
    String timeDifferenceForOrders(@RequestParam Long orderId);
}
