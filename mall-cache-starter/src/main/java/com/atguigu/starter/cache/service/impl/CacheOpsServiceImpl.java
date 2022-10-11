package com.atguigu.starter.cache.service.impl;


import com.atguigu.starter.cache.constant.SysRedisConst;
import com.atguigu.starter.cache.service.CacheOpsService;
import com.atguigu.starter.cache.utils.Jsons;
import com.fasterxml.jackson.core.type.TypeReference;
import org.redisson.api.RBloomFilter;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.lang.reflect.Type;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * 封装了缓存操作
 */
@Service
public class CacheOpsServiceImpl implements CacheOpsService {
    @Autowired
    StringRedisTemplate redisTemplate;
    @Autowired
    RedissonClient redissonClient;
    //专门执行延迟任务的线程池
    ScheduledExecutorService scheduledExecutor= Executors.newScheduledThreadPool(4);
    /**
     * 从缓存中获取一个数据,并转换指定类型的对象
     * @param cacheKey
     * @param clz
     * @param <T>
     * @return
     */
    @Override
    public <T> T getCacheData(String cacheKey, Class<T> clz) {
        String jsonStr = redisTemplate.opsForValue().get(cacheKey);
        //引入null值缓存机制
        if(SysRedisConst.NULL_VAL.equals(jsonStr)){
            return null;
        }

        T t = Jsons.toObj(jsonStr, clz);

        return t;
    }

    @Override
    public Object getCacheData(String cacheKey, Type type) {

        String jsonStr = redisTemplate.opsForValue().get(cacheKey);
        //引入null值缓存机制
        if(SysRedisConst.NULL_VAL.equals(jsonStr)){
            return null;
        }

       Object obj = Jsons.toObj(jsonStr, new TypeReference<Object>() {
           @Override
           public Type getType() {
               return type;
           }
       });

        return obj;
    }

    @Override
    public void delay2Delete(String cacheKey) {
        redisTemplate.delete(cacheKey);
        //提交一个延迟任务,断电失效 可以用分布式池框架
        scheduledExecutor.schedule(()->{
            redisTemplate.delete(cacheKey);
        }, 5, TimeUnit.SECONDS);

    }

    @Override
    public boolean bloomContains(Object skuId) {
        RBloomFilter<Object> filter = redissonClient.getBloomFilter(SysRedisConst.BLOOM_SKUID);
        boolean b = filter.contains(skuId);
        return b;
    }

    @Override
    public boolean bloomContains(String bloomName, Object bloomValue) {

        RBloomFilter<Object> bloomFilter = redissonClient.getBloomFilter(bloomName);
        boolean contains = bloomFilter.contains(bloomValue);
        return contains;
    }

    @Override
    public boolean tryLock(Long skuId) {
        //1.准备锁用的key             lock:sku:detail:48
        String lockKey = SysRedisConst.LOCK_SKU_DETAIL+skuId;
        RLock lock = redissonClient.getLock(lockKey);
        //2.尝试加锁
        boolean tryLock = lock.tryLock();


        return tryLock;
    }

    @Override
    public boolean tryLock(String lockName) {
        RLock lock = redissonClient.getLock(lockName);
        boolean b = lock.tryLock();

        return b;
    }

    @Override
    public void saveData(String cacheKey, Object fromRpc) {
        if (fromRpc == null){
            //空值缓存一个占位字符  并且时间短一点
            redisTemplate.opsForValue().set(cacheKey,SysRedisConst.NULL_VAL,SysRedisConst.NULL_VAL_TTL, TimeUnit.SECONDS);

        }

        //有值 存七天
        String jsonStr = Jsons.toStr(fromRpc);

        redisTemplate.opsForValue().set(cacheKey,jsonStr,SysRedisConst.SKU_DETAIL_TTL,TimeUnit.SECONDS);
    }

    @Override
    public void saveData(String cacheKey, Object fromRpc, Long dataTtl) {
        if (fromRpc == null){
            //空值缓存一个占位字符  并且时间短一点
            redisTemplate.opsForValue().set(cacheKey,SysRedisConst.NULL_VAL,SysRedisConst.NULL_VAL_TTL, TimeUnit.SECONDS);

        }

        //有值 自定义时间
        String jsonStr = Jsons.toStr(fromRpc);

        redisTemplate.opsForValue().set(cacheKey,jsonStr,
                dataTtl,TimeUnit.SECONDS);

    }

    @Override
    public void unlock(Long skuId) {
        String lockKey = SysRedisConst.LOCK_SKU_DETAIL+skuId;
        RLock lock = redissonClient.getLock(lockKey);

        //解锁
        lock.unlock();
    }

    @Override
    public void unlock(String lockName) {
        RLock lock = redissonClient.getLock(lockName);
        lock.unlock();
    }
}
