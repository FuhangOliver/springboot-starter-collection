package com.ciwei.redisserver;

import org.springframework.data.redis.connection.DataType;
import org.springframework.data.redis.core.Cursor;
import org.springframework.data.redis.core.ScanOptions;
import org.springframework.data.redis.core.ZSetOperations;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

/**
 * @author Administrator
 * @title: RedisCommand
 * @projectName springbootlogin
 * @description: TODO
 * @date 2019/6/2414:11
 */
public interface RedisCommand<K, V> {
    /**
     * 获取Redis中所有的键的key
     */
    Set<K> getAllKeys();

    /**
     * 获取所有的普通key-value
     */
    Map<K, V> getAllString();

    /**
     * 获取所有的Set -key-value
     */
    Map<K, Set<V>> getAllSet();

    /**
     * 获取所有的ZSet正序  -key-value 不获取权重值
     */
    Map<K, Set<V>> getAllZSetReverseRange();

    /**
     * 获取所有的ZSet倒序  -key-value 不获取权重值
     */
    Map<K, Set<V>> getAllZSetRange();

    /**
     * 获取所有的List -key-value
     */
    Map<K, List<V>> getAllList();

    /**
     * 获取所有的Map -key-value
     */
    Map<K, Map<K, V>> getAllMap();

    /**
     * 添加一个list
     */
    void addList(K key, List<V> objectList);

    /**
     * 向list中增加值
     *
     * @return 返回在list中的下标
     */
    long addList(K key, V obj);

    /**
     * 向list中增加值
     *
     * @return 返回在list中的下标
     */
    long addList(K key, V... obj);

    /**
     * 输出list
     *
     * @param key List的key
     * @param s   开始下标
     * @param e   结束的下标
     */
    List<V> getList(K key, long s, long e);

    /**
     * 输出完整的list
     */
    List<V> getList(K key);

    /**
     * 获取list集合中元素的个数
     */
    long getListSize(K key);

    /**
     * 移除list中某值
     * 移除list中 count个value为object的值,并且返回移除的数量,
     * 如果count为0,或者大于list中为value为object数量的总和,
     * 那么移除所有value为object的值,并且返回移除数量
     *
     * @return 返回移除数量
     */
    long removeListValue(K key, V object);

    /**
     * 移除list中某值
     *
     * @return 返回移除数量
     */
    long removeListValue(K key, V... object);

    /**
     * 批量删除key对应的value
     */
    void remove(final K... keys);

    /**
     * 删除缓存
     * 根据key精确匹配删除
     */
    void remove(final K key);

    /**
     * 通过分数删除ZSet中的值
     */
    void removeZSetRangeByScore(String key, double s, double e);

    /**
     * 设置Set的过期时间
     */
    Boolean setSetExpireTime(String key, Long time);

    /**
     * 设置ZSet的过期时间
     */
    Boolean setZSetExpireTime(String key, Long time);

    /**
     * 判断缓存中是否有key对应的value
     */
    boolean exists(final K key);

    /**
     * 读取String缓存 可以是对象
     */
    V get(final K key);

    /**
     * 读取String缓存 可以是对象
     */
    List<V> get(final K... key);

    /**
     * 读取缓存 可以是对象 根据正则表达式匹配
     */
    List<Object> getByRegular(final K regKey);


    /**
     * 写入缓存 可以是对象
     */
    void set(final K key, V value);

    /**
     * 写入缓存
     *
     * @param expireTime 过期时间 -单位s
     */
    void set(final K key, V value, Long expireTime);

    /**
     * 写入缓存 可以是对象
     */
    void setIfAbsent(final K key, V value);

    /**
     * 设置一个key的过期时间（单位：秒）
     */
    boolean setExpireTime(K key, Long expireTime);

    /**
     * 获取key的类型
     */
    DataType getType(K key);

    /**
     * 删除map中的某个对象
     *
     * @param key   map对应的key
     * @param field map中该对象的key
     */
    void removeMapField(K key, V... field);

    /**
     * 获取map对象
     *
     * @param key map对应的key
     */
    Map<K, V> getMap(K key);

    /**
     * 获取map对象
     *
     * @param key map对应的key
     */
    Long getMapSize(K key);

    /**
     * 获取map缓存中的某个对象
     *
     * @param key   map对应的key
     * @param field map中该对象的key
     */
    <T> T getMapField(K key, K field);

    /**
     * 判断map中对应key的key是否存在
     *
     * @param key map对应的key
     */
    Boolean hasMapKey(K key, K field);

    /**
     * 获取map对应key的value
     *
     * @param key map对应的key
     */
    List<V> getMapFieldValue(K key);

    /**
     * 获取map的key
     *
     * @param key map对应的key
     */
    Set<V> getMapFieldKey(K key);

    /**
     * 添加map
     */
    void addMap(K key, Map<K, V> map);

    /**
     * 向key对应的map中添加缓存对象
     *
     * @param key   cache对象key
     * @param field map对应的key
     * @param value 值
     */
    void addMap(K key, K field, Object value);

    /**
     * 向key对应的map中添加缓存对象
     * @author FuHang
     * @date 2019/7/25
     * @param key
     * @param field
     * @param value
     * @return void
     */
    void addHashMap(K key, K field, Object value);

    void delHashMap(String key, String hashKey);

    void incrementHashMap(String key, String hashKey,long value);

    void incrementHashMapDouble(String key, Object hashKey,double value);

    Cursor<Map.Entry<Object, Object>> scantHashMap(String key, ScanOptions scanOptions);

    V getOneHashMap(String key, String hashKey);
    /**
     * 向key对应的map中添加缓存对象
     *
     * @param key   cache对象key
     * @param field map对应的key
     * @param time  过期时间-整个MAP的过期时间
     * @param value 值
     */
    void addMap(K key, K field, V value, long time);

    /**
     * 向set中加入对象
     *
     * @param key 对象key
     * @param obj 值
     */
    void addSet(K key, V... obj);

    /**
     * 处理事务时锁定key
     */
    void watch(String key);

    /**
     * 移除set中的某些值
     *
     * @param key 对象key
     * @param obj 值
     */
    long removeSetValue(K key, V obj);

    /**
     * 移除set中的某些值
     *
     * @param key 对象key
     * @param obj 值
     */
    long removeSetValue(K key, V... obj);

    /**
     * 获取set的对象数
     *
     * @param key 对象key
     */
    long getSetSize(K key);

    /**
     * 判断set中是否存在这个值
     *
     * @param key 对象key
     */
    Boolean hasSetValue(K key, V obj);

    /**
     * 获得整个set
     *
     * @param key 对象key
     */
    Set<V> getSet(K key);

    /**
     * pop 获得set并删除
     *
     * @param key 对象key
     */
    V popSet(K key);

    /**
     * 获得set 并集
     */
    Set<V> getSetUnion(K key, K otherKey);

    /**
     * 获得set 并集
     */
    Set<V> getSetUnion(K key, Set<Object> set);

    /**
     * 获得set 交集
     */
    Set<V> getSetIntersect(K key, K otherKey);

    /**
     * 获得set 交集
     */
    Set<V> getSetIntersect(K key, Set<Object> set);

    /**
     * 模糊移除 支持*号等匹配移除
     */
    void removeBlear(K... blears);

    /**
     * 修改key名 如果不存在该key或者没有修改成功返回false
     */
    Boolean renameIfAbsent(String oldKey, String newKey);

    /**
     * 模糊移除 支持*号等匹配移除
     */
    void removeBlear(K blear);

    /**
     * 根据正则表达式来移除key-value
     */
    void removeByRegular(String... blears);

    /**
     * 根据正则表达式来移除key-value
     */
    void removeByRegular(String blears);

    /**
     * 根据正则表达式来移除 Map中的key-value
     */
    void removeMapFieldByRegular(K key, K... blears);

    /**
     * 根据正则表达式来移除 Map中的key-value
     */
    void removeMapFieldByRegular(K key, K blear);

    /**
     * 移除key 对应的value
     */
    Long removeZSetValue(K key, V... value);

    /**
     * 移除key ZSet
     */
    void removeZSet(K key);

    /**
     * 删除，键为K的集合，索引start<=index<=end的元素子集
     */
    void removeZSetRange(K key, Long start, Long end);

    /**
     * 并集 将key对应的集合和key1对应的集合合并到key2中
     * 如果分数相同的值，都会保留
     * 原来key2的值会被覆盖
     */
    void setZSetUnionAndStore(String key, String key1, String key2);

    /**
     * 获取整个有序集合ZSET，正序
     */
    <T> T getZSetRange(K key);

    /**
     * 获取有序集合ZSET
     * 键为K的集合，索引start<=index<=end的元素子集，正序
     * @param start 开始位置
     * @param end   结束位置
     */
    <T> T getZSetRange(K key, long start, long end);

    /**
     * 获取整个有序集合ZSET，倒序
     */
    Set<Object> getZSetReverseRange(K key);

    /**
     * 获取有序集合ZSET
     * 键为K的集合，索引start<=index<=end的元素子集，倒序
     * @param start 开始位置
     * @param end   结束位置
     */
    Set<V> getZSetReverseRange(K key, long start, long end);

    /**
     * 通过分数(权值)获取ZSET集合 正序 -从小到大
     */
    Set<V> getZSetRangeByScore(String key, double start, double end);

    /**
     * 通过分数(权值)获取ZSET集合 倒序 -从大到小
     */
    Set<V> getZSetReverseRangeByScore(String key, double start, double end);

    /**
     * 键为K的集合，索引start<=index<=end的元素子集
     * 返回泛型接口（包括score和value），正序
     */
    Set<ZSetOperations.TypedTuple<V>> getZSetRangeWithScores(K key, long start, long end);

    /**
     * 键为K的集合，索引start<=index<=end的元素子集
     * 返回泛型接口（包括score和value），倒序
     */
    Set<ZSetOperations.TypedTuple<V>> getZSetReverseRangeWithScores(K key, long start, long end);

    /**
     * 键为K的集合
     * 返回泛型接口（包括score和value），正序
     */
    Set<ZSetOperations.TypedTuple<V>> getZSetRangeWithScores(K key);

    /**
     * 键为K的集合
     * 返回泛型接口（包括score和value），倒序
     */
    Set<ZSetOperations.TypedTuple<V>> getZSetReverseRangeWithScores(K key);

    /**
     * 键为K的集合，sMin<=score<=sMax的元素个数
     */
    long getZSetCountSize(K key, double sMin, double sMax);

    /**
     * 获取Zset 键为K的集合元素个数
     */
    long getZSetSize(K key);

    /**
     * 获取键为K的集合，value为obj的元素分数
     */
    double getZSetScore(K key, V value);

    /**
     * 元素分数增加，delta是增量
     */
    double incrementZSetScore(K key, V value, double delta);

    /**
     * 添加有序集合ZSET
     * 默认按照score升序排列，存储格式K(1)==V(n)，V(1)=S(1)
     */
    Boolean addZSet(String key, double score, Object value);

    /**
     * 添加有序集合ZSET
     */
    Long addZSet(K key, TreeSet<ZSetOperations.TypedTuple<V>> value);

    /**
     * 添加有序集合ZSET
     */
    Boolean addZSet(K key, double[] score, Object[] value);

    /**
     * 计数
     * @param key
     * @param value
     */
    Long addUv(String key, Object... value);

    /**
     * 获取总数
     * @param key
     */
    Long getUv(String key);
}

