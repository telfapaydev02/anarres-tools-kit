package com.anarres.toolskit.cache.redis;

import com.anarres.toolskit.cache.Cache;
import com.anarres.toolskit.cache.HCache;
import com.anarres.toolskit.support.FuncKit;
import org.apache.shiro.cache.CacheException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collection;
import java.util.Set;

public class RedisCache<K, V> implements Cache<K, V> {
    private static Logger logger = LoggerFactory.getLogger(RedisCache.class);
    private String name;
    private HCache cached;

    public RedisCache(String name, HCache cached){
        this.name=name;
        this.cached=cached;
    }

    /**
     * 获得byte[]型的key
     * @param key
     * @return
     */
    private byte[] getByteKey(K key){
        if(key instanceof String){
            String preKey = key.toString();
            return preKey.getBytes();
        }else{
            return FuncKit.serialize(key);
        }
    }


    private byte[] getByteName(){
        return name.getBytes();

    }

    @Override
    public V get(K key) throws CacheException {
        logger.debug("根据key从Redis中获取对象 key [" + key + "]");
        try {
            if (key == null) {
                return null;
            }else{
                V value= (V) cached.getHashCached(getByteName(),getByteKey(key));
                return value;
            }
        } catch (Throwable t) {
            throw new CacheException(t);
        }

    }

    @Override
    public V put(K key, V value) throws CacheException {
        logger.debug("根据key存储 key [" + key + "]");
        try {
            cached.updateHashCached(getByteName(),getByteKey(key), FuncKit.serialize(value),null);
            return value;
        } catch (Throwable t) {
            throw new CacheException(t);
        }
    }

    @Override
    public V remove(K key) throws CacheException {
        logger.debug("从redis中删除 key [" + key + "]");
        try {
            V previous = get(key);
            cached.deleteHashCached(getByteName(),getByteKey(key));
            return previous;
        } catch (Throwable t) {
            throw new CacheException(t);
        }
    }

    @Override
    public void clear() throws CacheException {
        logger.debug("从redis中删除所有元素");
        try {
            cached.deleteCached(getByteName());
        } catch (Throwable t) {
            throw new CacheException(t);
        }
    }

    @Override
    public int size() {
        try {
            Long longSize = new Long(cached.getHashSize(getByteName()));
            return longSize.intValue();
        } catch (Throwable t) {
            throw new CacheException(t);
        }
    }

    @SuppressWarnings("unchecked")
    @Override
    public Set<K> keys() {
        try {
            Set<K> keys = cached.getHashKeys(getByteName());
            return keys;
        } catch (Throwable t) {
            throw new CacheException(t);
        }
    }

    @Override
    public Collection<V> values() {
        try {
            Collection<V> values = cached.getHashValues(getByteName());
            return values;
        } catch (Throwable t) {
            throw new CacheException(t);
        }
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }


    public HCache getCached() {
        return cached;
    }


    public void setCached(HCache cached) {
        this.cached = cached;
    }
}
