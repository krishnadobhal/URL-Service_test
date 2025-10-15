package com.url_service.url_service.configuration;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

@Configuration
public class RedisConfig {

    // DB for storing shortened urls (Persisted Redis)
    @Bean
    @Primary
    public JedisPool jedisPool(
            @Value("${spring.data.redis.host}") String host,
            @Value("${spring.data.redis.port}") int port) {

        JedisPoolConfig poolConfig = new JedisPoolConfig();
        poolConfig.setJmxEnabled(false);  // disable JMX registration

        return new JedisPool(poolConfig, host, port, 2000, null, 0);
    }

    // DB for caching
    @Bean
    @Qualifier("cacheJedisPool")
    public JedisPool cacheJedisPool(
            @Value("${spring.data.redis.host}") String host,
            @Value("${spring.data.redis.port}") int port) {

        JedisPoolConfig poolConfig = new JedisPoolConfig();
        poolConfig.setJmxEnabled(false);
        return new JedisPool(poolConfig, host, port, 2000, null, 1);
    }
}