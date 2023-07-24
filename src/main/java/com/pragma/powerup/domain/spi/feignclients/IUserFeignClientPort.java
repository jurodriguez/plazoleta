package com.pragma.powerup.domain.spi.feignclients;

import com.pragma.powerup.domain.model.User;

public interface IUserFeignClientPort {
    User getUserById(Long userId);
}
