package com.atguigu.gmall.product.api;

import com.atguigu.gmall.common.constant.SysRedisConst;
import com.atguigu.gmall.common.result.Result;
import org.redisson.api.RBloomFilter;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HelloController {
    @Autowired
    RedissonClient redissonClient;

    @GetMapping("/bloom/contains/{skuId}")
    public Result bloomContains(@PathVariable("skuId")Long skuId){
        //拿到布隆测试器
        RBloomFilter<Object> filter = redissonClient.getBloomFilter(SysRedisConst.BLOOM_SKUID);

        boolean b = filter.contains(skuId);


        return Result.ok(b);

    }












}
