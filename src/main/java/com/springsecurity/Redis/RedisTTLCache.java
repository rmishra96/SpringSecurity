package com.springsecurity.Redis;

import redis.clients.jedis.Jedis;

public class RedisTTLCache {
    private final Jedis jedis;
    private final long ttl;
    private final String namespace;

    public RedisTTLCache(String redishost,int redisPort, long ttl, String namespace) {
        this.jedis = new Jedis(redishost, redisPort);
        this.ttl = ttl;
        this.namespace = namespace;
    }
}
