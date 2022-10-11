package com.atguigu.starter.cache.service;

import java.lang.reflect.Type;

public interface CacheOpsService {
    /**
     * 从缓存中获取一个json并转为普通对象
     * @param cacheKey
     * @param clz
     * @param <T>
     * @return
     */
    <T>T getCacheData(String cacheKey, Class<T> clz);

    /**
     *  从缓存中获取一个json并转为复杂对象
     * @param cacheKey
     * @param type
     * @return
     */
    Object getCacheData(String cacheKey, Type type);

    /**
     * 延迟双删
     * @param cacheKey
     */
    void delay2Delete(String cacheKey );



    /**
     * 布隆过滤器判断有没有这个商品
     * @param skuId
     * @return
     */
    boolean bloomContains(Object skuId);

    /**
     * 判定指定布隆过滤器里边是否包含指定值
     * @param bloomName
     * @param bloomValue
     * @return
     */
    boolean bloomContains(String bloomName, Object bloomValue);

    /**
     * 给指定商品枷锁
     * @param skuId
     * @return
     */
    boolean tryLock(Long skuId);


    /**
     * 切面改造的 加指定的分布式锁
     * @param lockName
     * @return
     */
    boolean tryLock(String lockName);

    /**
     * 把指定对象使用指定的key保存到redis
     * @param cacheKey
     * @param fromRpc
     */
    void saveData(String cacheKey, Object fromRpc);

    /**
     *
     * @param cacheKey
     * @param fromRpc
     * @param dataTtl 数据过期时间 秒
     */
    void saveData(String cacheKey, Object fromRpc,Long dataTtl);

    void unlock(Long skuId);

    /**
     * 切面改造解锁
     * @param lockName
     */
    void unlock(String lockName);
}
