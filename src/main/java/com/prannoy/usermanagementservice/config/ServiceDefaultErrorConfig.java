package com.prannoy.usermanagementservice.config;

import com.prannoy.usermanagementservice.rest.BasicExceptionHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ServiceDefaultErrorConfig {

    @Bean
    public BasicExceptionHandler createResponseEntityExceptionHandler() {
        return new BasicExceptionHandler();
    }
}
