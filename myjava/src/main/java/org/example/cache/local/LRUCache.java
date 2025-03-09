package org.example.cache.local;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.Cache;

import java.util.Optional;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

public class LRUCache<K, V> implements LocalCache<K, V> {
    private final Cache<K, V> cache;

    public LRUCache(long ttl, int size) {
        this.cache = CacheBuilder.newBuilder()
                .maximumSize(size)
                .expireAfterWrite(ttl, TimeUnit.MILLISECONDS)
                .build();
    }

    @Override
    public Optional<V> get(K key) {
        return Optional.ofNullable(cache.getIfPresent(key));
    }

    @Override
    public V getWithFn(K key, Callable<V> fn) {
        try {
            return cache.get(key, fn);
        } catch (ExecutionException e) {
//            ExecutionException is Checked Exception 例如 IOException、RuntimeException、SQLException 等
            Throwable cause = e.getCause();
            if (cause instanceof RuntimeException) {
                throw (RuntimeException) cause;  // 直接拋出內部的 RuntimeException
                // 不讓所有使用 getWithFn() 方法的調用者都必須顯式處理 ExecutionException，讓錯誤處理變得更簡潔。
            } else {
                throw new RuntimeException("Failed to load value", cause); // 轉換為 RuntimeException
            }
        }
    }

    @Override
    public void set(K key, V value) {
        cache.put(key, value);
    }
}
