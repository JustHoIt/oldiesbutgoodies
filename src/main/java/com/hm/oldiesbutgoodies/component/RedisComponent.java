package com.hm.oldiesbutgoodies.component;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.stereotype.Component;

import java.time.Duration;

@Component
public class RedisComponent {

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    public void setExpiration(String key, Object obj, Duration expiration) {
        redisTemplate.setValueSerializer(new Jackson2JsonRedisSerializer<>(obj.getClass()));
        redisTemplate.opsForValue().set(key, obj, expiration);
    }

    public void set(String key, Object obj) {
        redisTemplate.setValueSerializer(new Jackson2JsonRedisSerializer<>(obj.getClass()));
    }

    public Object get(String key) {
        return redisTemplate.opsForValue().get(key);
    }

    public boolean delete(String key) {

        return Boolean.TRUE.equals(redisTemplate.delete(key));
    }

    public boolean hasKey(String key) {

        return Boolean.TRUE.equals(redisTemplate.hasKey(key));
    }

}

