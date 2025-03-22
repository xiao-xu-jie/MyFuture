package com.xujie.future.redis.keyBuilder;

import org.springframework.beans.factory.annotation.Value;

/**
 * Redis KeyBuilder 用于规范业务Redis key
 *
 * @author Xujie
 * @since 2025/3/22 19:28
 **/
public abstract class RedisKeyBuilder {

    @Value("${spring.application.name}")
    private String applicationName;

    abstract String buildKey(String info);
}
