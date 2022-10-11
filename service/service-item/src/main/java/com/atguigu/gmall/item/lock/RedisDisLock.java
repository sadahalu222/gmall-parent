package com.atguigu.gmall.item.lock;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

/**
 *
 * 使用Redis是实现的分布式锁
 */
@Component
public class RedisDisLock {
    @Autowired
    StringRedisTemplate redisTemplate;

    /**
     * 枷锁
     * @return
     */
    public String lock(){
        String token = UUID.randomUUID().toString();
        //1.枷锁
        //2.设置过期时间
        //set nxex
        while (!redisTemplate.opsForValue().setIfAbsent("lock", token, 10, TimeUnit.SECONDS)){
            //自旋阻塞式加锁
        }
        //加锁成功返回令牌
        return token;
    }

    /**
     * 解锁
     * @param token
     */
    public void unlock(String token){
        String luaScript = "if redis.call('get',KEYS[1]) == ARGV[1]  then return redis.call('del',KEYS[1]); else  return 0;end;";

        redisTemplate.execute(new DefaultRedisScript<>(luaScript, Long.class), Arrays.asList("lock"), token);

    }


}
