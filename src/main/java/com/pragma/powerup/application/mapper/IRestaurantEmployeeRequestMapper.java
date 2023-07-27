package com.pragma.powerup.application.mapper;

import com.pragma.powerup.application.dto.request.RestaurantEmployeeRequestDto;
import com.pragma.powerup.domain.model.RestaurantEmployee;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring",
        unmappedTargetPolicy = ReportingPolicy.IGNORE,
        unmappedSourcePolicy = ReportingPolicy.IGNORE)
public interface IRestaurantEmployeeRequestMapper {

    RestaurantEmployee toRestaurantEmployeeModel(RestaurantEmployeeRequestDto restaurantEmployeeRequestDto);
}
