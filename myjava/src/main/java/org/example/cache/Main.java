package org.example.cache;

import org.example.cache.local.LRUCache;
import org.example.cache.local.LocalCache;


public class Main {
    public static void main(String[] args) {
        LocalCache<String, String> cache = new LRUCache<>(60000, 100);

        // 设置缓存
        cache.set("key1", "value1");
        System.out.println(cache.get("key1").orElse("Not Found")); // 输出: value1

        // 使用 getWithFn
        String value = cache.getWithFn("key2", () -> "computedValue");
        System.out.println(value); // 输出: computedValue

        // 再次获取 key2，应从缓存返回
        System.out.println(cache.get("key2").orElse("Not Found")); // 输出: computedValue
    }
}
