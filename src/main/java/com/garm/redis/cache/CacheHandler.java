package com.garm.redis.cache;


public interface CacheHandler<T> {
    T get(String key, Class objectClass);

    T put(String key, T object, Class objectClass);

    T put(String key, T object, Class objectClass, long timeout);

    void remove(String key);


}
