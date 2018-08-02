package com.anarres.toolskit.cache.shiro;

import com.anarres.toolskit.cache.redis.RedisCache;
import com.anarres.toolskit.support.FuncKit;
import org.apache.shiro.cache.Cache;
import org.apache.shiro.cache.CacheException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collection;
import java.util.Set;

/**
 * Shiro 实现的缓存
 *
 * @param <K>
 * @param <V>
 */
public class ShiroRedisCache<K,V> implements Cache<K,V> {
	private com.anarres.toolskit.cache.Cache<K, V> impl;

	public ShiroRedisCache(RedisCache<K, V> redisImpl){
		this.impl = redisImpl;
	}

	@Override
	public V get(K key) throws CacheException {
		return impl.get(key);
	}

	@Override
	public V put(K key, V value) throws CacheException {
		return impl.put(key, value);
	}

	@Override
	public V remove(K key) throws CacheException {
		return impl.remove(key);
	}

	@Override
	public void clear() throws CacheException {
		impl.clear();
	}

	@Override
	public int size() {
		return impl.size();
	}

	@Override
	public Set<K> keys() {
		return impl.keys();
	}

	@Override
	public Collection<V> values() {
		return impl.values();
	}

}
