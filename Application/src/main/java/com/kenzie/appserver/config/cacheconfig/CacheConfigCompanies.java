package com.kenzie.appserver.config.cacheconfig;

import com.kenzie.appserver.config.cachestore.CacheStoreCompanies;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.TimeUnit;

@Configuration
@EnableCaching
public class CacheConfigCompanies {

    // Create a Cache here if needed
    @Bean
    public CacheStoreCompanies myCacheCompanies() {
        return new CacheStoreCompanies(120, TimeUnit.SECONDS);
    }
}