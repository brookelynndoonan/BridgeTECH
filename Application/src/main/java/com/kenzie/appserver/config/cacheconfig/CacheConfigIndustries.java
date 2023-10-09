package com.kenzie.appserver.config.cacheconfig;

import com.kenzie.appserver.config.cachestore.CacheStoreIndustries;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.TimeUnit;

@Configuration
@EnableCaching
public class CacheConfigIndustries {

    @Bean
    public CacheStoreIndustries myCacheIndustries() {
        return new CacheStoreIndustries(120, TimeUnit.SECONDS);
    }
}