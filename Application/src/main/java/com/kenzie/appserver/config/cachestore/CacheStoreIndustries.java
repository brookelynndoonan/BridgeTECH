package com.kenzie.appserver.config.cachestore;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;

import com.kenzie.appserver.service.model.Industries;

import java.util.concurrent.TimeUnit;

public class CacheStoreIndustries {
    private final Cache<String, Industries> cache;

    public CacheStoreIndustries(int expiry, TimeUnit timeUnit) {

        this.cache = CacheBuilder.newBuilder()
                .expireAfterWrite(expiry, timeUnit)
                .concurrencyLevel(Runtime.getRuntime().availableProcessors())
                .build();
    }

    public Industries get(String key) {

        return cache.getIfPresent(key);
    }

    public void evict(String key) {

        cache.invalidate(key);

    }

    public void add(String key, Industries value) {

        cache.put(key, value);
    }

}
