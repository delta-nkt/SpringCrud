//package com.example.DEMO_INTEGRATION.Utils.CacheUtils;
//
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.stereotype.Component;
//
//import java.lang.reflect.Field;
//import java.time.Instant;
//import java.util.Map;
//import java.util.concurrent.ConcurrentHashMap;
//
//@Slf4j
//@Component
//public class GenericCacheUtil {
//
//    private final Map<String, CacheEntry> cache = new ConcurrentHashMap<>();
//
//    private static class CacheEntry {
//        Object value;
//        long expiryMillis;
//
//        CacheEntry(Object value, long expiryMillis) {
//            this.value = value;
//            this.expiryMillis = expiryMillis;
//        }
//
//        boolean isExpired() {
//            return Instant.now().toEpochMilli() > expiryMillis;
//        }
//    }
//
//    /**
//     * Generic cache setter
//     * @param key - unique cache key
//     * @param value - object to cache
//     * @param ttlSeconds - time to live in seconds
//     */
//    public <T> void setCache(String key, T value, long ttlSeconds) {
//        long expiry = Instant.now().toEpochMilli() + ttlSeconds * 1000;
//        cache.put(key, new CacheEntry(value, expiry));
//        log.info("Cache set for key={} with TTL={}s", key, ttlSeconds);
//    }
//
//    /**
//     * Get the full cached object
//     * @param key - cache key
//     * @param clazz - expected class
//     * @return object or null if expired/missing
//     */
//    public <T> T getCache(String key, Class<T> clazz) {
//        CacheEntry entry = cache.get(key);
//        if (entry == null || entry.isExpired()) {
//            cache.remove(key);
//            log.warn("Cache expired/missing for key={}", key);
//            return null;
//        }
//        return clazz.cast(entry.value);
//    }
//
//    /**
//     * Extract a field value from cached object
//     * @param key - cache key
//     * @param fieldName - field name to extract
//     * @return value or null
//     */
//    public Object getFieldFromCache(String key, String fieldName) {
//        CacheEntry entry = cache.get(key);
//        if (entry == null || entry.isExpired()) {
//            cache.remove(key);
//            log.warn("Cache expired/missing for key={}", key);
//            return null;
//        }
//        try {
//            Field field = entry.value.getClass().getDeclaredField(fieldName);
//            field.setAccessible(true);
//            return field.get(entry.value);
//        } catch (Exception e) {
//            log.error("Failed to get field={} from cache key={}: {}", fieldName, key, e.getMessage());
//            return null;
//        }
//    }
//}
