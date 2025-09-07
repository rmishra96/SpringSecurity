package com.springsecurity.UtilityPackage;

import java.util.LinkedHashMap;
import java.util.Map;

public class LRUCacheWithTTL<K,V> extends LinkedHashMap<K,LRUCacheWithTTL.CacheEntry<V>> {
    static class CacheEntry<V> {
        V value;
        long timestamp;
        public CacheEntry(V value, long timestamp) {
            this.value = value;
            this.timestamp = timestamp;
        }
        boolean isExpired(long ttlMillis) {
            return System.currentTimeMillis() - timestamp > ttlMillis;
        }
    }

    private final int capacity;
    private final long ttlMillis;
    public LRUCacheWithTTL(int capacity, long ttlMillis) {
        super(capacity, 0.75f, true);
        this.capacity = capacity;
        this.ttlMillis = ttlMillis;
    }

    protected boolean removeEldestEntry(Map.Entry<K,CacheEntry<V>> eldest) {
        return size() > capacity;
    }

    public V getValue(K key) {
        CacheEntry<V> entry = super.get(key);
        if (entry == null) {
            super.remove(key);
            return null;
        }
        return entry.value;
    }

    public void putValue(K key, V value) {
        super.put(key,new CacheEntry<>(value,System.currentTimeMillis()));
    }

    public static void main(String[] args) throws InterruptedException {
        LRUCacheWithTTL<Integer,String> cache = new LRUCacheWithTTL<>(5, 1000);
        cache.putValue(1,"A");
        cache.putValue(2,"B");
        Thread.sleep(1000);
        cache.putValue(3,"C");
        System.out.println(cache.getValue(1));
        Thread.sleep(1500);
        System.out.println(cache.getValue(2));
        cache.putValue(4,"D");
        System.out.println(cache.getValue(1)); // null
        System.out.println(cache.getValue(3)); // C
        System.out.println(cache.getValue(4)); // D

    }
}
