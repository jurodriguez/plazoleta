package com.pragma.powerup.infrastructure.security;

import com.pragma.powerup.infrastructure.out.feignclients.UserFeignClients;
import com.pragma.powerup.infrastructure.out.feignclients.dto.UserDto;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    private final UserFeignClients userFeignClient;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        UserDto userDto = userFeignClient.getUserByEmail(email);
        return new UserDetailsImpl(userDto);
    }
}
