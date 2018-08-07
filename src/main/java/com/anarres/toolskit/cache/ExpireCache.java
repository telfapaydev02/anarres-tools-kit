package com.anarres.toolskit.cache;

public interface ExpireCache<K, V> extends Cache<K, V> {

    V put(K key, V value, long expireMS) throws Exception ;
}
