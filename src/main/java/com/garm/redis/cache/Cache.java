package com.garm.redis.cache;

import lombok.Builder;
import lombok.Data;
import lombok.ToString;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;
import org.springframework.data.redis.core.index.Indexed;

import java.io.Serializable;

@RedisHash("cache")
@Builder
@Data
@ToString
public class Cache implements Serializable {
    @Id
    private Long Id;
    @Indexed
    private String metadata;
    private String jsonObject;

}
