package com.kenzie.appserver.config.cachestore;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import com.kenzie.appserver.service.model.Companies;

import java.util.concurrent.TimeUnit;

public class CacheStoreCompanies {
    private final Cache<String, Companies> cache;

    public CacheStoreCompanies(int expiry, TimeUnit timeUnit) {

        this.cache = CacheBuilder.newBuilder()
                .expireAfterWrite(expiry, timeUnit)
                .concurrencyLevel(Runtime.getRuntime().availableProcessors())
                .build();
    }

    public Companies get(String key) {

        return cache.getIfPresent(key);
    }

    public void evict(String key) {

        cache.invalidate(key);

    }

    public void add(String key, Companies value) {

        cache.put(key, value);
    }
}
