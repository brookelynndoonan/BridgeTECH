package com.kenzie.appserver.config;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import com.kenzie.appserver.service.model.Career;

import java.util.concurrent.TimeUnit;

public class CacheStore {
    private final Cache<String, Career> cache;

    public CacheStore(int expiry, TimeUnit timeUnit) {
        // initalize the cache
        this.cache = CacheBuilder.newBuilder()
                .expireAfterWrite(expiry, timeUnit)
                .concurrencyLevel(Runtime.getRuntime().availableProcessors())
                .build();    
    }

    public Career get(String key) {
        // Write your code here
        // Retrieve and return the concert
        return cache.getIfPresent(key);
    }

    public void evict(String key) {
        // Write your code here
        // Invalidate/evict the concert from cache
        cache.invalidate(key);

    }

    public void add(String key, Career value) {
        // Write your code here
        // Add concert to cache
        cache.put(key, value);
    }
}
