package com.socialmedia.utils.cache;

import com.socialmedia.config.PropertiesManager;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;


public class RedisUtils {
    private static final JedisPool jedisPool = new JedisPool(
            new JedisPoolConfig(),
            PropertiesManager.getProperty("redis.host"),
            Integer.parseInt(PropertiesManager.getProperty("redis.port")
            )
    );
    private static final Jedis jedis = jedisPool.getResource();

    public static String getFromRedis(String key) {
        return jedis.get(key);
    }

    public static void setKeyValueWithTTL(String key, String value, int ttlInSeconds) {
        jedis.set(key, value);
        jedis.expire(key, ttlInSeconds);
    }

}
