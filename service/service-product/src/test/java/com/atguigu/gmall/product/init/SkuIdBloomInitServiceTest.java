package com.atguigu.gmall.product.init;

import com.atguigu.gmall.common.constant.SysRedisConst;
import org.junit.jupiter.api.Test;
import org.redisson.api.RBloomFilter;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class SkuIdBloomInitServiceTest {

    @Autowired
    RedissonClient redissonClient;

    @Test
    void t1(){
        RBloomFilter<Object> oldBloomFilter = redissonClient.getBloomFilter(SysRedisConst.BLOOM_SKUID);

        oldBloomFilter.rename("hello");

        RBloomFilter<Object> helloBloomFilter = redissonClient.getBloomFilter("hello");

        System.out.println(helloBloomFilter);
        System.out.println(oldBloomFilter);


    }

}