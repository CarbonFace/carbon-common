package cn.carbonface.carboncommon.tools;

import org.springframework.data.redis.core.*;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;

/**
 * Classname: RedisUtil
 * Description: only string type redis
 * @author CarbonFace <553127022@qq.com>
 * Date: 2021/3/16 15:35
 * @version V1.0
 */
@Component
public class RedisUtil {

    public static StringRedisTemplate redisTemplate;

    public RedisUtil(StringRedisTemplate redisTemplate ) {
        RedisUtil.redisTemplate = redisTemplate;
    }

    public static StringRedisTemplate getRedisTemplate() {
        return redisTemplate;
    }

    public static void expire(String key, long time) {
        if (time > 0) {
            getRedisTemplate().expire(key, time, TimeUnit.SECONDS);
        }
    }

    /**
     * 根据key 获取过期时间
     *
     * @param key 键 不能为null
     * @return 时间(秒) 返回0代表为永久有效
     */
    public static long getExpire(String key) {
        return getRedisTemplate().getExpire(key, TimeUnit.SECONDS);
    }

    /**
     * 判断key是否存在
     *
     * @param key 键
     * @return true 存在 false不存在
     */
    public static boolean hasKey(String key) {
        return getRedisTemplate().hasKey(key);
    }

    /**
     * 判断key和hasKey下是否有值
     *
     * @param key
     * @param hasKey
     */
    public static Boolean hasKey(String key, String hasKey) {
        return getRedisTemplate().opsForHash().hasKey(key, hasKey);
    }

    /**
     * 删除缓存
     *
     * @param key 可以传一个值 或多个
     */
    @SuppressWarnings("unchecked")
    public static void del(String... key) {
        if (key != null && key.length > 0) {
            if (key.length == 1) {
                getRedisTemplate().delete(key[0]);
            } else {
                getRedisTemplate().delete((Collection<String>) CollectionUtils.arrayToList(key));
            }
        }
    }

    // ============================String=============================

    /**
     * 普通缓存获取
     *
     * @param key 键
     * @return 值
     */
    public static Object get(String key) {
        return key == null ? null : getRedisTemplate().opsForValue().get(key);
    }

    /**
     * 普通缓存放入
     *
     * @param key   键
     * @param value 值
     */
    public static void set(String key, Object value) {
        getRedisTemplate().opsForValue().set(key, (String) value);
    }

    /**
     * 普通缓存放入并设置时间
     *
     * @param key   键
     * @param value 值
     * @param time  时间(秒) time要大于0 如果time小于等于0 将设置无限期
     */
    public static void set(String key, Object value, long time) {
        if (time > 0) {
            getRedisTemplate().opsForValue().set(key, (String) value, time, TimeUnit.SECONDS);
        } else {
            set(key, value);
        }
    }

    /**
     * 递增
     *
     * @param key   键
     * @param delta 要增加几(大于0)
     * @return
     */
    public static long incr(String key, long delta) {
        if (delta < 0) {
            throw new RuntimeException("递增因子必须大于0");
        }
        return getRedisTemplate().opsForValue().increment(key, delta);
    }

    /**
     * 递减
     *
     * @param key   键
     * @param delta 要减少几(小于0)
     * @return
     */
    public static long decr(String key, long delta) {
        if (delta < 0) {
            throw new RuntimeException("递减因子必须大于0");
        }
        return getRedisTemplate().opsForValue().increment(key, -delta);
    }

    // ================================Map=================================

    /**
     * HashGet
     *
     * @param key  键 不能为null
     * @param item 项 不能为null
     * @return 值
     */
    public static Object hGet(String key, String item) {
        return getRedisTemplate().opsForHash().get(key, item);
    }

    /**
     * 获取hashKey对应的所有键值
     *
     * @param key 键
     * @return 对应的多个键值
     */
    public static Map<Object, Object> hmGet(String key) {
        return getRedisTemplate().opsForHash().entries(key);
    }

    /**
     * HashSet
     *
     * @param key 键
     * @param map 对应多个键值
     */
    public static void hmSet(String key, Map<String, Object> map) {
        getRedisTemplate().opsForHash().putAll(key, map);
    }

    /**
     * HashSet 并设置时间
     *
     * @param key  键
     * @param map  对应多个键值
     * @param time 时间(秒)
     */
    public static void hmSet(String key, Map<String, Object> map, long time) {
        getRedisTemplate().opsForHash().putAll(key, map);
        if (time > 0) {
            expire(key, time);
        }
    }

    /**
     * 向一张hash表中放入数据,如果不存在将创建
     *
     * @param key   键
     * @param item  项
     * @param value 值
     */
    public static void hSet(String key, String item, Object value) {
        getRedisTemplate().opsForHash().put(key, item, value);
    }

    /**
     * 向一张hash表中放入数据,如果不存在将创建
     *
     * @param key   键
     * @param item  项
     * @param value 值
     * @param time  时间(秒) 注意:如果已存在的hash表有时间,这里将会替换原有的时间
     */
    public static void hSet(String key, String item, Object value, long time) {
        getRedisTemplate().opsForHash().put(key, item, value);
        if (time > 0) {
            expire(key, time);
        }
    }

    /**
     * 删除hash表中的值
     *
     * @param key  键 不能为null
     * @param item 项 可以使多个 不能为null
     */
    public static void hDel(String key, Object... item) {
        getRedisTemplate().opsForHash().delete(key, item);
    }

    /**
     * 判断hash表中是否有该项的值
     *
     * @param key  键 不能为null
     * @param item 项 不能为null
     * @return true 存在 false不存在
     */
    public static boolean hHasKey(String key, String item) {
        return getRedisTemplate().opsForHash().hasKey(key, item);
    }

    /**
     * hash递增 如果不存在,就会创建一个 并把新增后的值返回
     *
     * @param key  键
     * @param item 项
     * @param by   要增加几(大于0)
     */
    public static double hIncr(String key, String item, double by) {
        return getRedisTemplate().opsForHash().increment(key, item, by);
    }

    /**
     * hash递减
     *
     * @param key  键
     * @param item 项
     * @param by   要减少记(小于0)
     */
    public static double hDecr(String key, String item, double by) {
        return getRedisTemplate().opsForHash().increment(key, item, -by);
    }

    // ============================set=============================

    /**
     * 根据key获取Set中的所有值
     *
     * @param key 键
     * @return
     */
    public static Set<String> sGet(String key) {
        return getRedisTemplate().opsForSet().members(key);

    }

    /**
     * 根据value从一个set中查询,是否存在
     *
     * @param key   键
     * @param value 值
     * @return true 存在 false不存在
     */
    public static boolean sHasKey(String key, Object value) {
        return getRedisTemplate().opsForSet().isMember(key, value);
    }

    /**
     * 将数据放入set缓存
     *
     * @param key    键
     * @param values 值 可以是多个
     * @return 成功个数
     */
    public static long sSet(String key, String... values) {
        return getRedisTemplate().opsForSet().add(key, values);
    }

    /**
     * 将set数据放入缓存
     *
     * @param key    键
     * @param time   时间(秒)
     * @param values 值 可以是多个
     * @return 成功个数
     */
    public static long sSetAndTime(String key, long time, String... values) {
        Long count = getRedisTemplate().opsForSet().add(key, values);
        if (time > 0)
            expire(key, time);
        return count;
    }

    /**
     * 获取set缓存的长度
     *
     * @param key 键
     * @return 长度
     */
    public static long sGetSetSize(String key) {
        return getRedisTemplate().opsForSet().size(key);
    }

    /**
     * 移除值为value的
     *
     * @param key    键
     * @param values 值 可以是多个
     * @return 移除的个数
     */
    public static long setRemove(String key, Object... values) {
        Long count = getRedisTemplate().opsForSet().remove(key, values);
        return count;
    }
    // ===============================list=================================

    /**
     * 获取list缓存的内容
     *
     * @param key   键
     * @param start 开始
     * @param end   结束 0 到 -1代表所有值
     * @return
     */
    public static List<String> lGet(String key, long start, long end) {
        return getRedisTemplate().opsForList().range(key, start, end);
    }

    /**
     * 获取list缓存的长度
     *
     * @param key 键
     * @return
     */
    public static long lGetListSize(String key) {
        return getRedisTemplate().opsForList().size(key);
    }

    /**
     * 通过索引 获取list中的值
     *
     * @param key   键
     * @param index 索引 index>=0时， 0 表头，1 第二个元素，依次类推；index<0时，-1，表尾，-2倒数第二个元素，依次类推
     * @return
     */
    public static Object lGetIndex(String key, long index) {
        return getRedisTemplate().opsForList().index(key, index);
    }

    /**
     * 将list放入缓存
     *
     * @param key   键
     * @param value 值
     * @return
     */
    public static void lSet(String key, Object value) {
        getRedisTemplate().opsForList().rightPush(key, (String) value);
    }

    /**
     * 将list放入缓存
     *
     * @param key   键
     * @param value 值
     * @param time  时间(秒)
     * @return
     */
    public static void lSet(String key, Object value, long time) {
        getRedisTemplate().opsForList().rightPush(key, (String) value);
        if (time > 0)
            expire(key, time);
    }

    /**
     * 将list放入缓存
     *
     * @param key   键
     * @param value 值
     * @return
     */
    public static void lSet(String key, List<Object> value) {
        getRedisTemplate().opsForList().rightPushAll(key, String.valueOf(value));
    }

    /**
     * 将list放入缓存
     *
     * @param key   键
     * @param value 值
     * @param time  时间(秒)
     * @return
     */
    public static void lSet(String key, List<Object> value, long time) {
        getRedisTemplate().opsForList().rightPushAll(key, String.valueOf(value));
        if (time > 0)
            expire(key, time);
    }

    /**
     * 根据索引修改list中的某条数据
     *
     * @param key   键
     * @param index 索引
     * @param value 值
     */
    public static void lUpdateIndex(String key, long index, Object value) {
        getRedisTemplate().opsForList().set(key, index, (String) value);
    }

    /**
     * 移除N个值为value
     *
     * @param key   键
     * @param count 移除多少个
     * @param value 值
     * @return 移除的个数
     */
    public static long lRemove(String key, long count, Object value) {
        Long remove = getRedisTemplate().opsForList().remove(key, count, value);
        return remove;
    }

    // ===============================sorted set=================================

    /**
     * 向有序集合添加一个成员的
     * <p>
     * ZADD key score1 member1 [score2 member2]
     */
    public static void zAdd(String key, Object member, double score, long time) {
        getRedisTemplate().opsForZSet().add(key, (String) member, score);
        if (time > 0)
            expire(key, time);
    }

    /**
     * ZRANGEBYSCORE key min max [WITHSCORES] [LIMIT]
     * 通过分数返回有序集合指定区间内的成员
     * @return
     */
    public static Set<String> zRangeByScore(String key, double minScore, double maxScore) {
        return getRedisTemplate().opsForZSet().rangeByScore(key, minScore, maxScore);
    }

    /**
     * ZSCORE key member
     * 返回有序集中，成员的分数值
     */
    public static Double zScore(String key, Object member) {
        return getRedisTemplate().opsForZSet().score(key, member);
    }

    /**
     * ZRANK key member 返回有序集合中指定成员的索引
     */
    public static Long zRank(String key, Object member) {
        return getRedisTemplate().opsForZSet().rank(key, member);
    }

    /**
     * Zscan 迭代有序集合中的元素（包括元素成员和元素分值）
     * @return
     */
    public static Cursor<ZSetOperations.TypedTuple<String>> zScan(String key) {
        Cursor<ZSetOperations.TypedTuple<String>> cursor = getRedisTemplate().opsForZSet().scan(key, ScanOptions.NONE);
        return cursor;
    }

}