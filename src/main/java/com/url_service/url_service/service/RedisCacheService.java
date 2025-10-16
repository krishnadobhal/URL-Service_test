//package com.url_service.url_service.service;
//
//import lombok.RequiredArgsConstructor;
//import org.springframework.beans.factory.annotation.Qualifier;
//import org.springframework.stereotype.Service;
//import redis.clients.jedis.Jedis;
//import redis.clients.jedis.JedisPool;
//
//import java.util.concurrent.TimeUnit;
//
//@Service
//public class RedisCacheService {
//    private final JedisPool jedisPool;
//
//    // The @Qualifier annotation on the constructor parameter tells Spring
//    // to inject the bean named "cacheJedisPool"
//    public RedisCacheService(@Qualifier("cacheJedisPool") JedisPool jedisPool) {
//        this.jedisPool = jedisPool;
//    }
//
//    public void setCache(String key, String value, long ttl) {
//        try (Jedis jedis = jedisPool.getResource()) {
//            jedis.setex(key, ttl, value); // setex is used to set the key with a TTL(Time-To-Live)
//        }
//    }
//
//    public void setCache(String key, String value) {
//        try (Jedis jedis = jedisPool.getResource()) {
//            long defaultTtl = TimeUnit.DAYS.toSeconds(1);
//            jedis.setex(key, defaultTtl, value);
//        }
//    }
//
//    public String getCache(String key) {
//        try (Jedis jedis = jedisPool.getResource()) {
//            return jedis.get(key);
//        }
//    }
//}
