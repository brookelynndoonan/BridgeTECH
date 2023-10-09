package com.kenzie.appserver.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.cache.annotation.EnableCaching;

import java.util.concurrent.TimeUnit;

@Configuration
@EnableCaching
public class CacheConfigCareer {

    // Create a Cache here if needed
        @Bean
    public CacheStoreCareer myCache() {
        return new CacheStoreCareer(120, TimeUnit.SECONDS);
    }

}
