package com.pragma.powerup.infrastructure.out.feignclients.adapter;

import com.pragma.powerup.domain.model.User;
import com.pragma.powerup.domain.spi.feignclients.IUserFeignClientPort;
import com.pragma.powerup.infrastructure.out.feignclients.UserFeignClients;
import com.pragma.powerup.infrastructure.out.feignclients.dto.UserDto;
import com.pragma.powerup.infrastructure.out.feignclients.mapper.IUserDtoMapper;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class UserFeignAdapter implements IUserFeignClientPort {

    private final UserFeignClients userFeignClients;

    private final IUserDtoMapper userDtoMapper;

    @Override
    public User getUserById(Long userId) {
        UserDto userDto = userFeignClients.getUserById(userId);
        return userDtoMapper.toUserModel(userDto);
    }

    @Override
    public User getUserByEmail(String email) {
        UserDto userDto = userFeignClients.getUserByEmail(email);
        return userDtoMapper.toUserModel(userDto);
    }
}
