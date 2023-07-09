package com.garm.redis.util;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.Objects;
import java.util.Set;
import java.util.concurrent.TimeUnit;

@Component
@RequiredArgsConstructor
public class RedisUtils {

    private final RedisTemplate<String, Object> redisTemplate;
    private static RedisTemplate<String, Object> redis;

    @PostConstruct
    public void init() {
        redis = redisTemplate;
    }

    public static void resetExpire(String keyPattern, long timeout) {
        redis.setKeySerializer(new StringRedisSerializer());
        Set<String> keys = redis.keys(keyPattern);
        redis.executePipelined((RedisCallback<Object>) connection -> {
            Objects.requireNonNull(keys).forEach(key -> redis.expire(key, timeout, TimeUnit.SECONDS));
            return null;
        });
    }
}