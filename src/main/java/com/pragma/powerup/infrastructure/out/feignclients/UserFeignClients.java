package com.pragma.powerup.infrastructure.out.feignclients;

import com.pragma.powerup.infrastructure.out.feignclients.dto.UserDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "user-services", url = "localhost:8090/api/v1/user")
public interface UserFeignClients {

    @GetMapping("/{id}")
    UserDto getUserById(@PathVariable(value = "id") Long userId);

    @GetMapping("/email/{email}")
    UserDto getUserByEmail(@PathVariable(value = "email") String email);
}
