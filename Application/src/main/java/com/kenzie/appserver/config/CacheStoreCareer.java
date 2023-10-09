package com.kenzie.appserver.config;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import com.kenzie.appserver.service.model.Career;

import java.util.concurrent.TimeUnit;

public class CacheStoreCareer {
    private final Cache<String, Career> cache;

    public CacheStoreCareer(int expiry, TimeUnit timeUnit) {

        this.cache = CacheBuilder.newBuilder()
                .expireAfterWrite(expiry, timeUnit)
                .concurrencyLevel(Runtime.getRuntime().availableProcessors())
                .build();
    }

    public Career get(String key) {

        return cache.getIfPresent(key);
    }

    public void evict(String key) {

        cache.invalidate(key);

    }

    public void add(String key, Career value) {

        cache.put(key, value);
    }

}
