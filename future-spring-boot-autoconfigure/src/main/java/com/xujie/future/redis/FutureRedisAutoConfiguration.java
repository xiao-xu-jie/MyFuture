package com.xujie.future.redis;

import com.xujie.future.redis.config.RedisCacheConfig;
import com.xujie.future.redis.config.RedissonConfig;
import com.xujie.future.redis.util.RedisUtils;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.data.redis.core.RedisTemplate;

/**
 * @author Xujie
 * @since 2025/3/9 16:08
 **/

@ConditionalOnProperty(prefix = "spring.data.redis", value = "host")
@Configuration
@Import({RedisCacheConfig.class, RedissonConfig.class})
public class FutureRedisAutoConfiguration {
    @Bean
    public RedisUtils redisUtils(RedisTemplate<String, Object> redisTemplate) {
        return new RedisUtils(redisTemplate);
    }
}
