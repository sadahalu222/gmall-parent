package com.atguigu.gmall.item.config;

import org.junit.jupiter.api.Test;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class RedussinAutoConfigurationTest {

    @Autowired
    RedissonClient redissonClient;

    @Test
    void t1() {
        System.out.println(redissonClient);
    }


}