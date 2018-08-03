package com.anarres.toolskit.cache;


import java.util.Collection;
import java.util.Set;

public interface Cache<K, V> {
    V get(K key);

    V put(K key, V value);
    
    V remove(K key);

    void clear();

    int size();

    Set<K> keys();

    Collection<V> values();
}
