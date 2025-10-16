//package com.url_service.url_service.service;
//
//import lombok.RequiredArgsConstructor;
//import org.springframework.stereotype.Service;
//import redis.clients.jedis.Jedis;
//import redis.clients.jedis.JedisPool;
//
//@RequiredArgsConstructor
//@Service
//public class RedisService {
//
//    private final JedisPool jedisPool;
//
//    public void setValue(String key, String value) {
//        try (Jedis jedis = jedisPool.getResource()) {
//            jedis.set(key, value);
//        }
//    }
//
//    public void setValueWithExpiry(String key, String value, long seconds) {
//        try (Jedis jedis = jedisPool.getResource()) {
//            jedis.set(key, value);
//            jedis.expire(key, seconds);
//        }
//    }
//
//    public String getValue(String key) {
//        try (Jedis jedis = jedisPool.getResource()) {
//            return jedis.get(key);
//        }
//    }
//
//    public boolean exists(String key) {
//        try (Jedis jedis = jedisPool.getResource()) {
//            return jedis.exists(key);
//        }
//    }
//}