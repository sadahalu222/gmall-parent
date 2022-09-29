package com.atguigu.gmall.item.service.impl;

import com.atguigu.gmall.common.result.Result;
import com.atguigu.gmall.common.util.Jsons;
import com.atguigu.gmall.item.feign.SkuDetailFeignClient;
import com.atguigu.gmall.item.service.SkuDetailService;
import com.atguigu.gmall.model.product.SkuImage;
import com.atguigu.gmall.model.product.SkuInfo;
import com.atguigu.gmall.model.product.SpuSaleAttr;
import com.atguigu.gmall.model.to.CategoryViewTo;
import com.atguigu.gmall.model.to.SkuDetailTo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.math.BigDecimal;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

@Service
public class SkuDetailServiceImpl implements SkuDetailService {

    @Autowired
    SkuDetailFeignClient skuDetailFeignClient;

    @Autowired
    ThreadPoolExecutor executor;

    @Autowired
    StringRedisTemplate redisTemplate;

    public SkuDetailTo getSkuDetailFromRpc(Long skuId) {


        SkuDetailTo detailTo = new SkuDetailTo();
        //都执行完了发送
        //1.查基本信息
        CompletableFuture<SkuInfo> skuInfoFuture = CompletableFuture.supplyAsync(() -> {
            Result<SkuInfo> result = skuDetailFeignClient.getSkuInfo(skuId);
            SkuInfo skuInfo = result.getData();
            detailTo.setSkuInfo(skuInfo);
            return skuInfo;
        }, executor);


        //2.查商品图片
        CompletableFuture<Void> imageFuture = skuInfoFuture.thenAcceptAsync(skuInfo -> {
            Result<List<SkuImage>> skuImages = skuDetailFeignClient.getSkuImages(skuId);
            skuInfo.setSkuImageList(skuImages.getData());
        }, executor);



        //3.查实时价格
        CompletableFuture<Void> priceFuture = CompletableFuture.runAsync(() -> {
            Result<BigDecimal> price = skuDetailFeignClient.getSku101Price(skuId);
            detailTo.setPrice(price.getData());
        }, executor);
        


        //4.查销售属性名
        CompletableFuture<Void> saleAttrFuture = skuInfoFuture.thenAcceptAsync(skuInfo -> {
            Result<List<SpuSaleAttr>> skuSaleAttrValues = skuDetailFeignClient.getSkuSaleAttrValues(skuId, skuInfo.getSpuId());
            detailTo.setSpuSaleAttrList(skuSaleAttrValues.getData());

        }, executor);

        //5.查sku组合
        CompletableFuture<Void> valueJsonFuture = skuInfoFuture.thenAcceptAsync(skuInfo -> {

            Result<String> skuValueJson = skuDetailFeignClient.getSkuValueJson(skuInfo.getSpuId());
            detailTo.setValuesSkuJson(skuValueJson.getData());
        }, executor);


        //6.查分类
        CompletableFuture<Void> categoryViewFuture = skuInfoFuture.thenAcceptAsync(skuInfo -> {
            Result<CategoryViewTo> categoryView = skuDetailFeignClient.getCategoryView(skuInfo.getCategory3Id());
            detailTo.setCategoryView(categoryView.getData());
        }, executor);

        CompletableFuture
                .allOf(imageFuture,priceFuture,saleAttrFuture,valueJsonFuture,categoryViewFuture)
                .join();
        return detailTo;
    }

    @Override
    public SkuDetailTo getSkuDetail(Long skuId) {
        //1. 看缓存中有没有 sku:info:20
        String jsonStr = redisTemplate.opsForValue().get("sku:info:" + skuId);

        if("x".equals(jsonStr)){
            //说明以前查过,但是数据库没有 为了避免再次回源 缓存了一个x作为占位符
            return null;
        }

        if(StringUtils.isEmpty(jsonStr)){
            //redis中没有
            //回源
            SkuDetailTo fromRpc = getSkuDetailFromRpc(skuId);
            //将查到的对象转成json字符串并放入redis
            String cacheJson="x";
            if(fromRpc!=null){
                cacheJson=Jsons.toStr(fromRpc);
                redisTemplate.opsForValue().set("sku:info:" + skuId, cacheJson,7, TimeUnit.DAYS);

            }else {
                redisTemplate.opsForValue().set("sku:info:" + skuId, cacheJson,30, TimeUnit.MINUTES);

            }

            //返回查到的对象
            return fromRpc;

        }
        //redis中有
        SkuDetailTo skuDetailTo = Jsons.toObj(jsonStr, SkuDetailTo.class);


        return skuDetailTo;
    }
}
