package com.springsecurity.UtilityPackage;

import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class LRUCacheTTLPrd<K,V> extends LinkedHashMap<K,LRUCacheTTLPrd.CacheEntry<V>> {
    static class CacheEntry<V> {
        V value;
        long ttl;
        public CacheEntry(V value, long ttl) {
            this.value = value;
            this.ttl = ttl;
        }

        public boolean isexpired(long ttl) {
            return System.currentTimeMillis() - this.ttl > ttl;
        }
    }

    private final long ttl;
    private final int capacity;
    private final ScheduledExecutorService cleaner;

    public LRUCacheTTLPrd(int capacity, long ttl) {
        super(capacity, 0.75f, true);
        this.capacity = capacity;
        this.ttl = ttl;
        cleaner = Executors.newSingleThreadScheduledExecutor();
        cleaner.scheduleAtFixedRate(this::cleanupExpiredEntries, ttl, ttl, TimeUnit.MILLISECONDS);

    }

    private synchronized void cleanupExpiredEntries() {
        Iterator<Map.Entry<K,CacheEntry<V>>> iterator = super.entrySet().iterator();
        while (iterator.hasNext()) {
            Map.Entry<K,CacheEntry<V>> entry = iterator.next();
            if (entry.getValue().isexpired(ttl)) {
                iterator.remove();
                System.out.println("Expired Entry: " + entry.getKey());
            }
        }
    }

    public synchronized V getValue(K key) {
        CacheEntry<V> entry = super.get(key);
        if (entry == null || entry.isexpired(ttl)) {
            super.remove(key);
            return null;
        }
        return entry.value;
    }
    public synchronized  void putValue(K key, V value) {
        super.put(key,new CacheEntry<>(value,System.currentTimeMillis()));
    }

    public void shutdown() {
        cleaner.shutdown();
    }
    public static void main(String[] args) throws InterruptedException {
        LRUCacheTTLPrd<Integer,String> prd = new LRUCacheTTLPrd<>(3,3000);
        prd.putValue(1,"A");
        prd.putValue(2,"B");
        Thread.sleep(4000);
        System.out.println(prd.getValue(1));
        System.out.println(prd.getValue(2));
        prd.shutdown();
    }
}
