package com.garm.redis.cache;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.garm.redis.repository.RedisRepository;
import com.garm.redis.util.RedisUtils;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public abstract class CacheHandlerImpl<T> implements CacheHandler<T> {

    private final String prefix;
    private final RedisRepository redisRepository;

    public CacheHandlerImpl(RedisRepository redisRepository) {
        prefix = "";
        this.redisRepository = redisRepository;
    }

    public CacheHandlerImpl(String prefix, RedisRepository redisRepository) {
        this.prefix = prefix;
        this.redisRepository = redisRepository;
    }

    @Override
    public T get(String key, Class objectClass) {
        T ans = null;
        try {
            Cache localCache = this.redisRepository.findByMetadata(makeKey(key));
            ans = (T) new ObjectMapper().readValue(localCache.getJsonObject(), objectClass);
        } catch (Exception e) {
            log.info("this key is not available in local cache.");
        }
        return ans;
    }

    @SneakyThrows
    @Override
    public T put(String key, T object, Class objectClass) {

        if (this.get(key, objectClass) == null) {
            this.redisRepository.save(Cache.builder()
                    .metadata(makeKey(key))
                    .jsonObject(new ObjectMapper().writeValueAsString(object))
                    .build());
            return object;
        } else {
            Cache localCache = this.redisRepository.findByMetadata(makeKey(key));
            this.redisRepository.delete(localCache);
            return this.put(key, object, objectClass);
        }
    }

    @Override
    public void remove(String key) {
        Cache localCache = this.redisRepository.findByMetadata(makeKey(key));
        this.redisRepository.delete(localCache);
    }

    @Override
    public T put(String key, T object, Class objectClass, long timeout) {
        T out = this.put(key, object, objectClass);
        RedisUtils.resetExpire(makeKey(key), timeout);
        return out;
    }

    private String makeKey(String key) {
        return prefix + key;
    }
}
