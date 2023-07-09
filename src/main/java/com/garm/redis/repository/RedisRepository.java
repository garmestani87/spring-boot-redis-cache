package com.garm.redis.repository;

import com.garm.redis.cache.Cache;
import org.springframework.data.repository.CrudRepository;

public interface RedisRepository extends CrudRepository<Cache, Long> {

    Cache findByMetadata(String metadata);

    Cache findById(long id);

}
