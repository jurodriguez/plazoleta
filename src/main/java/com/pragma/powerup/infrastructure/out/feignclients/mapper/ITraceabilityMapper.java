package com.pragma.powerup.infrastructure.out.feignclients.mapper;

import com.pragma.powerup.application.dto.request.TraceabilityRequestDto;
import com.pragma.powerup.domain.model.Traceability;
import com.pragma.powerup.infrastructure.out.feignclients.dto.TraceabilityDto;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

import java.util.List;

@Mapper(componentModel = "spring",
        unmappedTargetPolicy = ReportingPolicy.IGNORE,
        unmappedSourcePolicy = ReportingPolicy.IGNORE
)
public interface ITraceabilityMapper {

    TraceabilityRequestDto toTraceabilityDto(Traceability traceability);

    List<Traceability> toTraceabilityResponseDtoList(List<TraceabilityDto> traceabilityList);
}
