package com.pragma.powerup.infrastructure.feignconfiguration;

import feign.RequestInterceptor;
import feign.RequestTemplate;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

@Component
@Slf4j
public class SecurityFeignRequestInterceptor implements RequestInterceptor {

    private static final String AUTHORIZATION_HEADER = "Authorization";

    @Override
    public void apply(RequestTemplate template) {
        template.header(AUTHORIZATION_HEADER, getBearerTokenHeader());
    }

    public static String getBearerTokenHeader() {
        log.info(((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest().getHeader(AUTHORIZATION_HEADER));
        return ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest().getHeader(AUTHORIZATION_HEADER);
    }
}
