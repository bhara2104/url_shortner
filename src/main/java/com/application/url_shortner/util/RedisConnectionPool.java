package com.application.url_shortner.util;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

import java.time.Duration;

public class RedisConnectionPool {
    private JedisPool jedisPool = null;
    private static RedisConnectionPool redisConnectionPool = null;

    public RedisConnectionPool() {
        JedisPoolConfig poolConfig = new JedisPoolConfig();
        poolConfig.setMaxTotal(128);
        poolConfig.setMaxIdle(128);
        poolConfig.setMinIdle(16);
        poolConfig.setTestOnBorrow(true);
        poolConfig.setTestOnReturn(true);
        poolConfig.setTestWhileIdle(true);
        poolConfig.setMinEvictableIdleTimeMillis(Duration.ofSeconds(60).toMillis());
        poolConfig.setTimeBetweenEvictionRunsMillis(Duration.ofSeconds(30).toMillis());
        poolConfig.setNumTestsPerEvictionRun(3);
        poolConfig.setBlockWhenExhausted(true);

        jedisPool = new JedisPool(poolConfig, "localhost", 6379, 2000, null);
    }

    public static RedisConnectionPool getInstance() {
        if (redisConnectionPool == null) {
            redisConnectionPool = new RedisConnectionPool();
        }
        return redisConnectionPool;
    }

    public Jedis getConnection() {
        return jedisPool.getResource();
    }
}
