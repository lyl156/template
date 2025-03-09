package org.example.cache.local;

import java.util.Optional;
import java.util.concurrent.Callable;

public interface LocalCache<K, V> {
    Optional<V> get(K key);

    V getWithFn(K key, Callable<V> fn);

    void set(K key, V value);
}
