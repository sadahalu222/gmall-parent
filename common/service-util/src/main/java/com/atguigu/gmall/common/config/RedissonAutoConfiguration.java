package com.atguigu.gmall.common.config;

import org.redisson.Redisson;
import org.redisson.api.RedissonClient;
import org.redisson.config.Config;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.data.redis.RedisAutoConfiguration;
import org.springframework.boot.autoconfigure.data.redis.RedisProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 1.导入 redisson.jar
 * 2.给容器中放好RedissonClient组件
 *
 * 如果redis没配置好 redisson不能用
 */
@Configuration
@AutoConfigureAfter(RedisAutoConfiguration.class)
public class RedissonAutoConfiguration {

    @Autowired
    RedisProperties redisProperties;

    @Bean
    public RedissonClient redissonClient(){
        //1.创建一个配置
        Config config = new Config();
        String host = redisProperties.getHost();
        int port = redisProperties.getPort();
        String password = redisProperties.getPassword();
        //2.指定好redisson的配置项
        config.useSingleServer()
                .setAddress("redis://"+host+":"+port)
                .setPassword(password);

        //创建RedissonClient
        RedissonClient redissonClient = Redisson.create(config);

        return redissonClient;
    }
}













