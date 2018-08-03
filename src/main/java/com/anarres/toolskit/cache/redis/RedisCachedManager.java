package com.anarres.toolskit.cache.redis;

import com.anarres.toolskit.cache.Cache;
import com.anarres.toolskit.cache.HCache;

import java.util.*;

public class RedisCachedManager {
    private final HCache cached;

    private final Map<String, RedisCache> caches = new HashMap<>();

    public RedisCachedManager(HCache cached) {
        this.cached = cached;
    }

    protected Cache createCache(String cacheName) {
        if(caches.containsKey(cacheName)) {
            return caches.get(cacheName);
        }

        RedisCache c = new RedisCache(cacheName, cached);
        caches.put(cacheName, c);

        return c;
    }

    public Set<String> caches() {
        return Collections.unmodifiableSet(caches.keySet());
    }
}
