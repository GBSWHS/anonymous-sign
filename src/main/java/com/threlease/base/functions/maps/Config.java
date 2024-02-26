package com.threlease.base.functions.maps;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class Config {
    @Bean
    public com.threlease.base.functions.maps.Data dataBean() {
        return new com.threlease.base.functions.maps.Data();
    }
}
