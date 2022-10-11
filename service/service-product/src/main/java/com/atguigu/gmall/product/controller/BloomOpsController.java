package com.atguigu.gmall.product.controller;

import com.atguigu.gmall.common.constant.SysRedisConst;
import com.atguigu.gmall.common.result.Result;
import com.atguigu.gmall.product.bloom.BloomDataQueryService;
import com.atguigu.gmall.product.bloom.BloomOpsService;
import com.atguigu.gmall.product.bloom.impl.SkuBloomDataQueryServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 布隆过滤器操作
 */
@RestController
@RequestMapping("/admin/product")
public class BloomOpsController {

    @Autowired
    BloomOpsService bloomOpsService;

    @Autowired
    SkuBloomDataQueryServiceImpl skuBloomDataQueryService;

    @GetMapping("/rebuild/sku/now")
    public Result rebuildBloom(){
        String bloomName = SysRedisConst.BLOOM_SKUID;
        bloomOpsService.rebuildBloom(bloomName,skuBloomDataQueryService);

        return Result.ok();
    }






















}
