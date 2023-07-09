package com.garm.redis.logger;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;


@Slf4j
@Aspect
@Component
public class CacheLogger {

    @AfterReturning(pointcut = "execution(* com.garm.redis.cache.CacheHandler.get(..))", returning = "object")
    public void find(Object object) {
        log.info("Redis >> " + object);
    }

    @AfterReturning(pointcut = "execution(* com.garm.redis.cache.CacheHandler.put(..))", returning = "object")
    public void save(Object object) {
        log.info("Redis [Cached] >> " + object);
    }


}