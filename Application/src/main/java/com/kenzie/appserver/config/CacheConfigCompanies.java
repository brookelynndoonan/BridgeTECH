package com.kenzie.appserver.config;

import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.TimeUnit;

@Configuration
@EnableCaching
public class CacheConfigCompanies {

    // Create a Cache here if needed
    @Bean
    public CacheStoreCompanies myCache() {
        return new CacheStoreCompanies(120, TimeUnit.SECONDS);
    }
}