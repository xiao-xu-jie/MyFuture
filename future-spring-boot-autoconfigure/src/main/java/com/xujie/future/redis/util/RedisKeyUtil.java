package com.xujie.future.redis.util;

import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.util.CollectionUtils;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.TimeUnit;

/**
 * Redis Key 操作工具类
 *
 * @author Xujie
 * @since 2025/3/14 20:25
 **/
@Slf4j
@SuppressWarnings(value = "unchecked")
public class RedisKeyUtil {

    private final RedisTemplate<String, Object> redisTemplate;

    public RedisKeyUtil(RedisTemplate<String, Object> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    /**
     * 是否存在Key
     *
     * @param key 键
     * @return 结果
     */
    public Boolean hasKey(String key) {
        if (StringUtils.isBlank(key)) {
            return false;
        }
        Boolean hasKey = false;
        try {
            hasKey = redisTemplate.hasKey(key);
        } catch (Exception e) {
            log.error("发生异常：{}", e.getMessage(), e);
            throw new RuntimeException(e);
        }
        return hasKey;
    }

    /**
     * 设置键过期时间
     *
     * @param key      键
     * @param time     时间
     * @param timeUnit 时间单位
     * @return 是否成功
     */
    public Boolean expire(String key, long time, TimeUnit timeUnit) {
        try {
            if (time > 0) {
                redisTemplate.expire(key, time, timeUnit);
            }
            return Boolean.TRUE;
        } catch (Exception e) {
            log.error("发生异常：{}", e.getMessage(), e);
            return Boolean.FALSE;
        }
    }

    /**
     * 获取key的过期时间
     *
     * @param key 键
     * @return 过期时间（秒）
     */
    public Long getExpire(String key) {
        return redisTemplate.getExpire(key, TimeUnit.SECONDS);
    }

    /**
     * 删除缓存
     *
     * @param key 可以传一个值 或多个
     */
    public void del(String... key) {
        if (key != null && key.length > 0) {
            if (key.length == 1) {
                redisTemplate.delete(key[0]);
            } else {
                redisTemplate.delete(Arrays.asList(key));
            }
        }
    }

    /**
     * 根据正则表达式获取key列表
     *
     * @param patternKey 正则表达式
     * @return 匹配key列表
     */
    public Set<String> keys(String patternKey) {
        try {
            return (Set<String>) redisTemplate.keys(patternKey);
        } catch (Exception e) {
            log.error("发生异常：{}", e.getMessage(), e);
            return new HashSet<>();
        }
    }


}
