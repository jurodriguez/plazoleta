package com.pragma.powerup.infrastructure.out.feignclients.mapper;

import com.pragma.powerup.domain.model.User;
import com.pragma.powerup.infrastructure.out.feignclients.dto.UserDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring",
        unmappedTargetPolicy = ReportingPolicy.IGNORE,
        unmappedSourcePolicy = ReportingPolicy.IGNORE
)
public interface IUserDtoMapper {

    @Mapping(target = "roleId", source = "role.id")
    User toUserModel(UserDto userDto);
}
