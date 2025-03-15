package com.xujie.future.redis;

import com.xujie.future.redis.config.RedisCacheConfig;
import com.xujie.future.redis.service.RedisService;
import com.xujie.future.redis.util.RedisKeyUtil;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.data.redis.core.RedisCommand;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;

/**
 * @author Xujie
 * @since 2025/3/9 16:08
 **/
@ConditionalOnProperty(prefix = "spring.data.redis", value = "host")
@Configuration
@Import({RedisCacheConfig.class})
public class FutureRedisAutoConfiguration {

    @Bean
    public RedisKeyUtil redisKeyUtil(RedisTemplate<String, Object> redisTemplate) {
        return new RedisKeyUtil(redisTemplate);
    }

    @Bean
    public RedisService redisService(RedisTemplate<String, Object> redisTemplate, StringRedisTemplate stringRedisTemplate, RedisKeyUtil redisKeyUtil) {
        return new RedisService(redisTemplate, stringRedisTemplate, redisKeyUtil);
    }
}
