package com.pragma.powerup.infrastructure.out.token;

import com.pragma.powerup.domain.spi.bearertoken.IToken;
import com.pragma.powerup.infrastructure.security.TokenUtils;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

public class TokenAdapter implements IToken {
    @Override
    public String getBearerToken() {
        return ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest().getHeader("Authorization");
    }

    @Override
    public String getEmail(String token) {
        return TokenUtils.getEmail(token.replace("Bearer ", ""));
    }

    @Override
    public Long getUserAuthenticatedId(String token) {
        return TokenUtils.getUserAuthenticatedId(token.replace("Bearer ", ""));
    }
}
