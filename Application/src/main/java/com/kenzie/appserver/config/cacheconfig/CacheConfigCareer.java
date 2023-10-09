package com.kenzie.appserver.config.cacheconfig;

import com.kenzie.appserver.config.cachestore.CacheStoreCareer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.cache.annotation.EnableCaching;

import java.util.concurrent.TimeUnit;

@Configuration
@EnableCaching
public class CacheConfigCareer {

    // Create a Cache here if needed
        @Bean
    public CacheStoreCareer myCacheCareer() {
        return new CacheStoreCareer(120, TimeUnit.SECONDS);
    }

}
