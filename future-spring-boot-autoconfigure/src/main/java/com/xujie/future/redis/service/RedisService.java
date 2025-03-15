package com.xujie.future.redis.service;

import com.alibaba.fastjson.JSON;
import com.xujie.future.redis.util.RedisKeyUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.core.*;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.scheduling.annotation.Async;
import org.springframework.util.CollectionUtils;

import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.concurrent.TimeUnit;

@Slf4j
@SuppressWarnings("unchecked")
public class RedisService {
    /**
     * 注入redisTemplate bean
     */

    private final RedisTemplate<String, Object> redisTemplate;

    private final StringRedisTemplate stringRedisTemplate;

    private final RedisKeyUtil redisKeyUtil;

    public RedisService(RedisTemplate<String, Object> redisTemplate, StringRedisTemplate stringRedisTemplate, RedisKeyUtil redisKeyUtil) {
        this.redisTemplate = redisTemplate;
        this.stringRedisTemplate = stringRedisTemplate;
        this.redisKeyUtil = redisKeyUtil;
    }

    // ============================String(字符串)=============================

    /**
     * 普通缓存获取
     *
     * @param key 键
     * @return 值
     */
    public <T> T get(String key) {
        return key == null ? null : (T) redisTemplate.opsForValue().get(key);
    }

    /**
     * 普通缓存获取
     *
     * @param key 键
     * @return 值
     */
    public <T> T getAndSet(String key, T value) {
        return key == null ? null : (T) redisTemplate.opsForValue().getAndSet(key, value);
    }

    /**
     * 普通缓存放入
     *
     * @param key   键
     * @param value 值
     * @return true成功 false失败
     */
    public <T> boolean set(String key, T value) {
        try {
            redisTemplate.opsForValue().set(key, value);
            return true;
        } catch (Exception e) {
            log.error("redis set error", e);
            return false;
        }
    }

    /**
     * 普通缓存放入并设置时间
     *
     * @param key   键
     * @param value 值
     * @param time  时间(秒) time要大于0 如果time小于等于0 将设置无限期
     * @return true成功 false 失败
     */
    public <T> boolean set(String key, T value, Long time) {
        try {
            if (time > 0) {
                redisTemplate.opsForValue().set(key, value, time, TimeUnit.SECONDS);
            } else {
                return set(key, value);
            }
            return true;
        } catch (Exception e) {
            log.error("redis set error", e);
            return false;
        }
    }

    /**
     * 普通缓存放入并设置时间
     *
     * @param key      键
     * @param value    值
     * @param time     时间(秒) time要大于0 如果time小于等于0 将设置无限期
     * @param timeUnit 过期时间单位 {@link TimeUnit}
     * @return true成功 false 失败
     */
    public <T> boolean set(String key, T value, Long time, TimeUnit timeUnit) {
        try {
            if (time > 0) {
                redisTemplate.opsForValue().set(key, value, time, timeUnit);
            } else {
                return set(key, value);
            }
            return true;
        } catch (Exception e) {
            log.error("redis set error", e);
            return false;
        }
    }

    /**
     * 普通缓存放入并设置时间 （ key 不存在时，为 key 设置指定的值）
     *
     * @param key   键
     * @param value 值
     * @return true key不存在保存成功 false key存在，失败
     */
    public <T> boolean setnx(String key, T value) {
        return Boolean.TRUE.equals(redisTemplate.opsForValue().setIfAbsent(key, value));
    }

    /**
     * 普通缓存放入并设置时间 （ key 不存在时，为 key 设置指定的值）
     *
     * @param key   键
     * @param value 值
     * @param time  时间(秒) time要大于0 如果time小于等于0 将设置无限期
     * @return true key不存在保存成功 false key存在，失败
     */
    public <T> boolean setnx(String key, Object value, Long time) {
        try {
            if (time > 0) {
                return Boolean.TRUE.equals(redisTemplate.opsForValue().setIfAbsent(key, value, time, TimeUnit.SECONDS));
            } else {
                return setnx(key, value);
            }
        } catch (Exception e) {
            log.error("redis setnx error", e);
            return false;
        }
    }

    /**
     * 多个key 保存
     *
     * @param map
     * @return
     */
    public Boolean multiSet(Map<String, Object> map) {
        try {
            redisTemplate.opsForValue().multiSet(mapCovert(map));
            return true;
        } catch (Exception e) {
            log.error("redis multi set error", e);
            return false;
        }
    }


    /**
     * 获取多个值
     *
     * @param keys
     * @return
     */
    public <T> List<T> multiGet(Collection<String> keys) {
        try {
            return (List<T>) redisTemplate.opsForValue().multiGet(keys);
        } catch (Exception e) {
            log.error("redis multi get error", e);
            return null;
        }
    }

    /**
     * 递增
     *
     * @param key   键
     * @param delta 要增加几(大于0)
     * @return
     */
    public Long incr(String key, Long delta) {
        if (delta < 0) {
            throw new RuntimeException("递增因子必须大于0");
        }
        return redisTemplate.opsForValue().increment(key, delta);
    }

    /**
     * 递减
     *
     * @param key   键
     * @param delta 要减少几(小于0)
     * @return
     */
    public Long decr(String key, Long delta) {
        if (delta < 0) {
            throw new RuntimeException("递减因子必须大于0");
        }
        return redisTemplate.opsForValue().increment(key, -delta);
    }
    // ================================Hash(哈希)=================================

    /**
     * HashGet
     *
     * @param key  键 不能为null
     * @param item 项 不能为null
     * @return 值
     */
    public <T> T hget(String key, String item) {
        HashOperations<String, String, T> hashOperations = redisTemplate.opsForHash();
        return hashOperations.get(key, item);
    }

    /**
     * HashGet
     *
     * @param key          键 不能为null
     * @param item         项 不能为null
     * @param defaultValue 默认值
     * @return 值
     */
    public <T> T hgetOrDefault(String key, String item, T defaultValue) {
        HashOperations<String, String, T> hashOperations = redisTemplate.opsForHash();
        T obj = hashOperations.get(key, item);
        if (obj == null) {
            return defaultValue;
        }
        return obj;
    }

    /**
     * 获取hashKey对应的所有键值
     *
     * @param key 键
     * @return 对应的多个键值
     */
    public <T> Map<String, T> hmget(String key) {
        HashOperations<String, String, T> hashOperations = redisTemplate.opsForHash();
        return hashOperations.entries(key);
    }

    /**
     * HashSet
     *
     * @param key 键
     * @param map 对应多个键值
     * @return true 成功 false 失败
     */
    public <T> boolean hmset(String key, Map<String, T> map) {
        try {
            redisTemplate.opsForHash().putAll(key, map);
            return true;
        } catch (Exception e) {
            log.error("redis hash multi set error", e);
            return false;
        }
    }

    /**
     * HashSet 并设置时间
     *
     * @param key  键
     * @param map  对应多个键值
     * @param time 时间(秒)
     * @return true成功 false失败
     */
    public <T> boolean hmset(String key, Map<String, T> map, Long time) {
        try {
            redisTemplate.opsForHash().putAll(key, map);
            if (time > 0) {
                redisKeyUtil.expire(key, time, TimeUnit.SECONDS);
            }
            return true;
        } catch (Exception e) {
            log.error("redis hash multi set error", e);
            return false;
        }
    }

    public <T> boolean hmset(String key, Map<String, T> map, Long time, TimeUnit timeUnit) {
        try {
            redisTemplate.opsForHash().putAll(key, map);
            if (time > 0) {
                redisKeyUtil.expire(key, time, timeUnit);
            }
            return true;
        } catch (Exception e) {
            log.error("redis hash multi set error", e);
            return false;
        }
    }

    /**
     * 向一张hash表中放入数据,如果不存在将创建
     *
     * @param key   键
     * @param item  项
     * @param value 值
     * @return true 成功 false失败
     */
    public <T> boolean hset(String key, String item, T value) {
        try {
            redisTemplate.opsForHash().put(key, item, value);
            return true;
        } catch (Exception e) {
            log.error("redis hash set error", e);
            return false;
        }
    }

    /**
     * 向一张hash表中放入数据,如果不存在将创建
     *
     * @param key   键
     * @param item  项
     * @param value 值
     * @param time  时间(秒) 注意:如果已存在的hash表有时间,这里将会替换原有的时间
     * @return true 成功 false失败
     */
    public <T> boolean hset(String key, String item, T value, Long time) {
        return hset(key, item, value, time, TimeUnit.SECONDS);
    }

    /**
     * 向一张hash表中放入数据,如果不存在将创建
     *
     * @param key      键
     * @param item     项
     * @param value    值
     * @param time     时间 注意:如果已存在的hash表有时间,这里将会替换原有的时间
     * @param timeUnit 时间单位
     * @return true 成功 false失败
     */
    public <T> boolean hset(String key, String item, T value, Long time, TimeUnit timeUnit) {
        try {
            redisTemplate.opsForHash().put(key, item, value);
            if (time > 0) {
                redisKeyUtil.expire(key, time, timeUnit);
            }
            return true;
        } catch (Exception e) {
            log.error("redis hash set error", e);
            return false;
        }
    }

    /**
     * 删除hash表中的值
     *
     * @param key  键 不能为null
     * @param item 项 可以使多个 不能为null
     */
    public void hdel(String key, String... item) {
        redisTemplate.opsForHash().delete(key, item);
    }

    /**
     * 判断hash表中是否有该项的值
     *
     * @param key  键 不能为null
     * @param item 项 不能为null
     * @return true 存在 false不存在
     */
    public boolean hHasKey(String key, String item) {
        return redisTemplate.opsForHash().hasKey(key, item);
    }

    /**
     * hash递增 如果不存在,就会创建一个 并把新增后的值返回
     *
     * @param key  键
     * @param item 项
     * @param by   要增加几(大于0)
     * @return
     */
    public double hincr(String key, String item, double by) {
        return redisTemplate.opsForHash().increment(key, item, by);
    }

    /**
     * hash递减
     *
     * @param key  键
     * @param item 项
     * @param by   要减少记(小于0)
     * @return
     */
    public double hdecr(String key, String item, double by) {
        return redisTemplate.opsForHash().increment(key, item, -by);
    }

    /**
     * hash Entries
     *
     * @param key 键
     * @return
     */
    public Map<String, Object> hEntries(String key) {
        HashOperations<String, String, Object> hashOperations = redisTemplate.opsForHash();
        return hashOperations.entries(key);
    }

    // ============================Set(集合)=============================

    /**
     * 根据key获取Set中的所有值
     *
     * @param key 键
     * @return
     */
    public <T> Set<T> sGet(String key) {
        try {
            SetOperations<String, Object> setOperations = redisTemplate.opsForSet();

            return (Set<T>) setOperations.members(key);
        } catch (Exception e) {
            log.error("redis set get all error", e);
            return null;
        }
    }

    /**
     * 根据key获取Set中的所有值
     *
     * @param key 键
     * @return
     */
    public <T> T sPop(String key) {
        try {
            SetOperations<String, Object> setOperations = redisTemplate.opsForSet();
            return (T) setOperations.pop(key);
        } catch (Exception e) {
            log.error("redis set pop error", e);
            return null;
        }
    }

    /**
     * 根据key获取Set中的所有值
     *
     * @param key 键
     * @return
     */
    public <T> List<T> sPop(String key, Long count) {
        try {
            SetOperations<String, Object> setOperations = redisTemplate.opsForSet();
            return (List<T>) setOperations.pop(key, count);
        } catch (Exception e) {
            log.error("redis set pop error", e);
            return null;
        }
    }

    /**
     * 根据value从一个set中查询,是否存在
     *
     * @param key   键
     * @param value 值
     * @return true 存在 false不存在
     */
    public <T> boolean sHasKey(String key, T value) {
        try {
            return redisTemplate.opsForSet().isMember(key, value);
        } catch (Exception e) {
            log.error("redis set has value error", e);
            return false;
        }
    }

    /**
     * 将数据放入set缓存
     *
     * @param key    键
     * @param values 值 可以是多个
     * @return 成功个数
     */
    public <T> Long sAdd(String key, T... values) {
        try {
            return redisTemplate.opsForSet().add(key, values);
        } catch (Exception e) {
            log.error("redis set add value error", e);
            return 0L;
        }
    }

    /**
     * 将set数据放入缓存
     *
     * @param key    键
     * @param time   时间(秒)
     * @param values 值 可以是多个
     * @return 成功个数
     */
    public <T> Long sAddAndTime(String key, Long time, T... values) {
        try {
            Long count = redisTemplate.opsForSet().add(key, values);
            if (time > 0) {
                redisKeyUtil.expire(key, time, TimeUnit.SECONDS);
            }
            return count;
        } catch (Exception e) {
            e.printStackTrace();
            return 0L;
        }
    }

    /**
     * 获取set缓存的长度
     *
     * @param key 键
     * @return
     */
    public Long sGetSetSize(String key) {
        try {
            return redisTemplate.opsForSet().size(key);
        } catch (Exception e) {
            log.error("redis get set size error", e);
            return 0L;
        }
    }

    /**
     * 移除值为value的
     *
     * @param key    键
     * @param values 值 可以是多个
     * @return 移除的个数
     */
    public <T> Long setRemove(String key, T... values) {
        try {
            Long count = redisTemplate.opsForSet().remove(key, values);
            return count;
        } catch (Exception e) {
            log.error("redis set remove error", e);
            return 0L;
        }
    }

    // ============================ZSet(集合)=============================

    /**
     * 根据key获取ZSet中的值
     *
     * @param start 开始
     * @param end   结束
     * @return
     */
    public <T> Set<T> zGet(String key, Long start, Long end) {
        try {
            ZSetOperations<String, Object> zSetOperations = redisTemplate.opsForZSet();
            return (Set<T>) zSetOperations.range(key, start, end);
        } catch (Exception e) {
            log.error("redis zset get all error", e);
            return null;
        }
    }

    /**
     * 根据key获取ZSet中的值
     *
     * @param min 最小分数
     * @param max 最大分数
     * @return
     */
    public <T> Set<T> zGetByScore(String key, double min, double max) {
        try {
            ZSetOperations<String, Object> zSetOperations = redisTemplate.opsForZSet();

            return (Set<T>) zSetOperations.rangeByScore(key, min, max);
        } catch (Exception e) {
            log.error("redis zset get all error", e);
            return null;
        }
    }

    /**
     * 根据key获取ZSet中的值
     *
     * @param min    最小分数
     * @param max    最大分数
     * @param offset 位置
     * @param count  数量
     * @return
     */
    public <T> Set<T> zGetByScore(String key, double min, double max, Long offset, Long count) {
        try {
            ZSetOperations<String, Object> zSetOperations = redisTemplate.opsForZSet();

            return (Set<T>) zSetOperations.rangeByScore(key, min, max, offset, count);
        } catch (Exception e) {
            log.error("redis zset get all error", e);
            return null;
        }
    }

    // /**
    //  * 从zset 中获取值 并 删除
    //  *
    //  * @param key   键
    //  * @param start 开始位置
    //  * @param end   结束位置
    //  * @return
    //  */
    // public <T> Set<T> zGetAndRemove(String key, Long start, Long end) {
    //     synchronized (this) {
    //         Set<T> set = zGet(key, start, end);
    //         if (CollectionUtils.isEmpty(set) == false) {
    //             zRemove(key, set.toArray());
    //         }
    //         return set;
    //     }
    // }


    /**
     * 将数据放入ZSet缓存
     *
     * @param key   键
     * @param value 值
     * @param score 分数
     * @return 成功个数
     */
    public <T> boolean zAdd(String key, T value, double score) {
        try {
            return redisTemplate.opsForZSet().add(key, value, score);
        } catch (Exception e) {
            log.error("redis set add value error", e);
            return false;
        }
    }

    /**
     * 将多个数据放入ZSet缓存
     *
     * @param key     键
     * @param members 值
     * @param scores  分数
     * @return 成功个数
     */
    public Long multiZadd(String key, List<String> members, List<Double> scores) {
        Set<ZSetOperations.TypedTuple<Object>> tupleSet = new HashSet<ZSetOperations.TypedTuple<Object>>();
        for (int i = 0; i < members.size(); i++) {
            String member = members.get(i);
            Double score = scores.get(i);
            ZSetOperations.TypedTuple<Object> tuple = new DefaultTypedTuple<Object>(member, score);
            tupleSet.add(tuple);
        }

        return redisTemplate.opsForZSet().add(key, tupleSet);
    }

    /**
     * 获取zset缓存的长度
     *
     * @param key 键
     * @return
     */
    public Long zSize(String key) {
        try {
            return redisTemplate.opsForZSet().size(key);
        } catch (Exception e) {
            log.error("redis get zset size error", e);
            return 0L;
        }
    }

    /**
     * 计算 zset 中得分介于 {@code min}和{@code max} 之间的元素数
     *
     * @param key 键
     * @param min 最小分数
     * @param max 最大分数
     * @return
     */
    public Long zCount(String key, double min, double max) {
        try {
            return redisTemplate.opsForZSet().count(key, min, max);
        } catch (Exception e) {
            log.error("redis get zset count error", e);
            return 0L;
        }
    }

    /**
     * 移除值为value的
     *
     * @param key    键
     * @param values 值 可以是多个
     * @return 移除的个数
     */
    public <T> Long zRemove(String key, T... values) {
        try {
            Long count = redisTemplate.opsForZSet().remove(key, values);
            return count;
        } catch (Exception e) {
            log.error("redis zset remove error", e);
            return 0L;
        }
    }

    /**
     * 从带有zset中移除{@code start}和{@code end}之间的元素。
     *
     * @param key   键
     * @param start 开始位置
     * @param end   结束位置
     * @return 移除的个数
     */
    public Long zRemoveByRange(String key, Long start, Long end) {
        try {
            Long count = redisTemplate.opsForZSet().removeRange(key, start, end);
            return count;
        } catch (Exception e) {
            log.error("redis zset remove error", e);
            return 0L;
        }
    }

    /**
     * 从带有zset中移除分数{@code min}和{@code max}之间的元素。
     *
     * @param key 键
     * @param min 最小分数
     * @param max 最大分数
     * @return 移除的个数
     */
    public Long zRemoveByScore(String key, double min, double max) {
        try {
            Long count = redisTemplate.opsForZSet().removeRangeByScore(key, min, max);
            return count;
        } catch (Exception e) {
            log.error("redis zset remove error", e);
            return 0L;
        }
    }


    /**
     * 为zet 中元素添加分数
     *
     * @param key   键
     * @param value 值
     * @param delta 分数
     * @return
     */
    public <T> double zincrScore(String key, T value, double delta) {
        try {
            return redisTemplate.opsForZSet().incrementScore(key, value, delta);
        } catch (Exception e) {
            log.error("redis zset inc error", e);
            return 0;
        }
    }

    /**
     * 为zet 中元素减分数
     *
     * @param key   键
     * @param value 值
     * @param delta 分数
     * @return
     */
    public <T> double zdecrScore(String key, T value, double delta) {
        try {
            return redisTemplate.opsForZSet().incrementScore(key, value, -delta);
        } catch (Exception e) {
            log.error("redis zset inc error", e);
            return 0;
        }
    }

    // ===============================List(列表)=================================

    /**
     * 获取list缓存的内容
     *
     * @param key   键
     * @param start 开始
     * @param end   结束 0 到 -1代表所有值
     * @return
     */
    public <T> List<T> lGet(String key, Long start, Long end) {
        try {
            ListOperations<String, Object> listOperations = redisTemplate.opsForList();
            return (List<T>) listOperations.range(key, start, end);
        } catch (Exception e) {
            log.error("redis list get range error", e);
            return null;
        }
    }

    /**
     * 获取list缓存的内容 (left)
     *
     * @param key 键
     * @return
     */
    public <T> T lLeftPop(String key) {
        try {
            return (T) redisTemplate.opsForList().leftPop(key);
        } catch (Exception e) {
            log.error("redis list left pop error", e);
            return null;
        }
    }

    /**
     * 获取list缓存的内容 (right)
     *
     * @param key 键
     * @return
     */
    public <T> T lRightPop(String key) {
        try {
            return (T) redisTemplate.opsForList().rightPop(key);
        } catch (Exception e) {
            log.error("redis list right pop error", e);
            return null;
        }
    }

    /**
     * 获取list缓存的长度
     *
     * @param key 键
     * @return
     */
    public Long lGetSize(String key) {
        try {
            return redisTemplate.opsForList().size(key);
        } catch (Exception e) {
            log.error("redis get list size error", e);
            return 0L;
        }
    }

    /**
     * 通过索引 获取list中的值
     *
     * @param key   键
     * @param index 索引 index>=0时， 0 表头，1 第二个元素，依次类推；index<0时，-1，表尾，-2倒数第二个元素，依次类推
     * @return
     */
    public <T> T lGetIndex(String key, Long index) {
        try {
            return (T) redisTemplate.opsForList().index(key, index);
        } catch (Exception e) {
            log.error("redis get list index error", e);
            return null;
        }
    }

    /**
     * 将list放入缓存
     *
     * @param key   键
     * @param value 值
     * @return
     */
    public <T> boolean lLeftPush(String key, T... value) {
        try {
            redisTemplate.opsForList().leftPush(key, value);
            return true;
        } catch (Exception e) {
            log.error("redis  list left push error", e);
            return false;
        }
    }

    /**
     * 将list放入缓存
     *
     * @param key    键
     * @param values 值
     * @return
     */
    public <T> boolean lLeftPushAll(String key, Collection<T> values) {
        try {
            if (CollectionUtils.isEmpty(values)) {
                throw new IllegalArgumentException("value is null");
            }
            redisTemplate.opsForList().leftPushAll(key, values.toArray());
            return true;
        } catch (Exception e) {
            log.error("redis  list left push error", e);
            return false;
        }
    }

    /**
     * 将list放入缓存
     *
     * @param key   键
     * @param value 值
     * @return
     */
    public <T> boolean lRightPush(String key, T... value) {
        try {
            redisTemplate.opsForList().rightPush(key, value);
            return true;
        } catch (Exception e) {
            log.error("redis list right push error", e);
            return false;
        }
    }

    /**
     * 将list放入缓存
     *
     * @param key    键
     * @param values 值
     * @return
     */
    public <T> boolean lRightPushAll(String key, Collection<T> values) {
        try {
            if (CollectionUtils.isEmpty(values)) {
                throw new IllegalArgumentException("values is null");
            }
            redisTemplate.opsForList().rightPushAll(key, values.toArray());
            return true;
        } catch (Exception e) {
            log.error("redis list right push error", e);
            return false;
        }
    }

    /**
     * 根据索引修改list中的某条数据
     *
     * @param key   键
     * @param index 索引
     * @param value 值
     * @return
     */
    public <T> boolean lUpdateIndex(String key, Long index, T value) {
        try {
            redisTemplate.opsForList().set(key, index, value);
            return true;
        } catch (Exception e) {
            log.error("redis list update value error", e);
            return false;
        }
    }

    public Long bitCount(String key) {
        return redisTemplate.execute((RedisCallback<Long>) con -> con.bitCount(key.getBytes()));
    }

    /**
     * 删除对应的value
     *
     * @param key
     */
    public void remove(final String key) {
        if (exists(key)) {
            redisTemplate.delete(key);
        }
    }

    /**
     * 删除对应的value
     *
     * @param collection
     */
    public void remove(final Collection collection) {
        redisTemplate.delete(collection);
    }

    /**
     * 判断缓存中是否有对应的value
     *
     * @param key
     * @return
     */
    public boolean exists(final String key) {
        return redisTemplate.hasKey(key);
    }

    /**
     * redis 发布
     *
     * @param channel channel key
     * @param message message
     */
    public void publish(String channel, Object message) {
        redisTemplate.convertAndSend(channel, parseStrValue(message));
    }

    private String parseStrValue(Object value) {
        if (null == value) {
            return StringUtils.EMPTY;
        }
        if (value instanceof String) {
            return (String) value;
        }

        return JSON.toJSONString(value);
    }

    private String[] parseStrValues(Object... values) {
        String[] arrs = new String[values.length];
        for (int i = 0; i < values.length; i++) {
            arrs[i] = parseStrValue(values[i]);
        }

        return arrs;
    }


    private Map<String, String> mapCovert(Map<String, Object> map) {
        if (map == null || map.isEmpty()) {
            return new HashMap<>();
        }
        Map<String, String> result = new HashMap<>(map.size());
        for (Map.Entry<String, Object> entry : map.entrySet()) {
            result.put(entry.getKey(), parseStrValue(entry.getValue()));
        }
        return result;
    }

    /**
     * 获得缓存的基本对象列表
     *
     * @param pattern 字符串前缀
     * @return 对象列表
     */
    public Collection<String> keys(final String pattern) {
        return redisTemplate.keys(pattern);
    }

    public List<String> multiLuaHget(List<String> params) {
        String luaScript =
                "local results = {}  " +
                        "local i = 1  " +
                        "while i < #KEYS do  " +
                        "  local key = KEYS[i]  " +
                        "  local field = KEYS[i+1]  " +
                        "  local value = redis.call('hget', key, field)  " +
                        "  if value then " +
                        "    table.insert(results, value) " +
                        "  end  " +
                        "  i = i + 2  " +
                        "end  " +
                        "return results";
        DefaultRedisScript<List> script = new DefaultRedisScript<>();
        script.setScriptText(luaScript);
        script.setResultType(List.class);
        // 调用RedisTemplate的执行方法
        try {
            List execute = stringRedisTemplate.execute(script, params);
            return execute;
        } catch (Exception e) {
            log.error("redis hash multi hget value error", e);
            return null;
        }
    }

    public Long multiLuaHset(List<String> params) {
        String luaScript =
                "local i = 1  " +
                        "while i < #KEYS do  " +
                        "    local key = KEYS[i]  " +
                        "    local field = KEYS[i+1]  " +
                        "    local value = KEYS[i+2]  " +
                        "    redis.call('hset', key, field, value)  " +
                        "    i = i + 3  " +
                        "end  " +
                        "return #KEYS / 3";
        DefaultRedisScript<Long> script = new DefaultRedisScript<>();
        script.setScriptText(luaScript);
        script.setResultType(Long.class);
        // 调用RedisTemplate的执行方法
        try {
            Long execute = redisTemplate.execute(script, params);
            return execute;
        } catch (Exception e) {
            log.error("redis hash multi hset value error", e);
            return null;
        }
    }

    public List<Object> batchQueryHashValues(List<String> params) {
        try {
            List<Object> datas = redisTemplate.executePipelined(new RedisCallback<Object>() {
                @Override
                public Object doInRedis(RedisConnection connection) throws DataAccessException {
                    for (int i = 0; i < params.size(); i = i + 2) {
                        connection.hGet(params.get(i).getBytes(StandardCharsets.UTF_8), params.get(i + 1).getBytes(StandardCharsets.UTF_8));
                    }
                    // 这里必须返回null，在 connection.closePipeline() 时覆盖原来的返回值，所以返回值没有必要设置，设置会报错
                    return null;
                }
            }, redisTemplate.getStringSerializer());
            return datas;
        } catch (Exception e) {
            log.error("batch query hash values error");
            return null;
        }
    }

    public List<Object> batchInsertHashValues(List<String> params) {
        try {
            List<Object> datas = redisTemplate.executePipelined(new RedisCallback<Object>() {
                @Override
                public Object doInRedis(RedisConnection connection) throws DataAccessException {
                    // 开始事务
                    connection.multi();
                    for (int i = 0; i < params.size(); i = i + 3) {
                        connection.hSet(params.get(i).getBytes(StandardCharsets.UTF_8), params.get(i + 1).getBytes(StandardCharsets.UTF_8), params.get(i + 2).getBytes(StandardCharsets.UTF_8));
                    }
                    // 提交事务
                    connection.exec();
                    // 这里必须返回null，在 connection.closePipeline() 时覆盖原来的返回值，所以返回值没有必要设置，设置会报错
                    return null;
                }
            });
            return datas;
        } catch (Exception e) {
            log.error("batch insert hash values error");
            return null;
        }
    }


    public Set<String> scanKeys(String pattern) {
        Set<String> keysFound = new HashSet<>();
        //
        Cursor<byte[]> cursor = redisTemplate.executeWithStickyConnection(
                redisConnection -> redisConnection.scan(
                        ScanOptions
                                .scanOptions().match(pattern)
                                // count 可以限制匹配到的key数量 不设置默认匹配所以
                                //.count(1000)
                                .build()));
        while (cursor.hasNext()) {
            keysFound.add(new String(cursor.next()));
        }
        try {
            cursor.close();
        } catch (Exception e) {
            // Handle exception if necessary
        }
        return keysFound;
    }

}
