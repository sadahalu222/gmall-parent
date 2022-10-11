package com.atguigu.gmall.item.service.impl;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.StringRedisTemplate;

import static org.junit.jupiter.api.Assertions.*;
@SpringBootTest
class SkuDetailServiceImplTest {


    @Autowired
    StringRedisTemplate redisTemplate;
    @Test
    void t1(){
        Long sadahalu = redisTemplate.opsForValue().increment("sadahalu11");
        System.out.println(sadahalu);
    }

}