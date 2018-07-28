package com.anarres.toolskit.cache;

import org.apache.shiro.cache.AbstractCacheManager;
import org.apache.shiro.cache.Cache;
import org.apache.shiro.cache.CacheException;

/**
 * redis的缓存管理器
 * @author
 *
 */
public class ShiroRedisCacheManager extends AbstractCacheManager {
	private ICached cached;
	@Override
	protected Cache createCache(String cacheName) throws CacheException {
		return new ShiroRedisCache(cacheName,cached);
	}
	public ICached getCached() {
		return cached;
	}
	public void setCached(ICached cached) {
		this.cached = cached;
	}

}
