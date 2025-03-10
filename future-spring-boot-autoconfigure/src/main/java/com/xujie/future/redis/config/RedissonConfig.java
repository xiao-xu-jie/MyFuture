package com.xujie.future.redis.config;

import org.apache.commons.lang3.ObjectUtils;
import org.redisson.Redisson;
import org.redisson.api.RedissonClient;
import org.redisson.config.Config;
import org.redisson.config.SingleServerConfig;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Redisson配置类
 *
 * @author Xujie
 * @since 2024/10/26 13:40
 */
public class RedissonConfig {

    @Value("${spring.data.redis.host}")
    private String host;
    @Value("${spring.data.redis.password}")
    private String password;
    @Value("${spring.data.redis.port}")
    private String port;

    private final String url = "redis://%s:%d";

    @Bean
    public RedissonClient redissonClient() {
        Config config = new Config();
        SingleServerConfig singleServerConfig = config.useSingleServer();
        singleServerConfig.setAddress(String.format(url, host, Integer.valueOf(port)));
        if (ObjectUtils.isNotEmpty(password)) {
            singleServerConfig.setPassword(password);
        }
        return Redisson.create(config);
    }
}
