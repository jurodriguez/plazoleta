package com.pragma.powerup.application.mapper;

import com.pragma.powerup.application.dto.response.OrderResponseDto;
import com.pragma.powerup.domain.model.Order;
import com.pragma.powerup.domain.model.orders.OrderResponseModel;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

import java.util.List;

@Mapper(componentModel = "spring",
        unmappedTargetPolicy = ReportingPolicy.IGNORE,
        unmappedSourcePolicy = ReportingPolicy.IGNORE)
public interface IOrderResponseMapper {

    OrderResponseDto toResponse(Order order);

    List<OrderResponseDto> toResponseList(List<Order> orderList);

    List<OrderResponseDto> toOrderResponseList(List<OrderResponseModel> orderResponseModels);
}
