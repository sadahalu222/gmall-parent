package com.atguigu.gmall.item.service.impl;

import com.atguigu.gmall.common.constant.SysRedisConst;
import com.atguigu.gmall.common.result.Result;

import com.atguigu.gmall.item.feign.SkuDetailFeignClient;
import com.atguigu.gmall.item.service.SkuDetailService;
import com.atguigu.gmall.model.product.SkuImage;
import com.atguigu.gmall.model.product.SkuInfo;
import com.atguigu.gmall.model.product.SpuSaleAttr;
import com.atguigu.gmall.model.to.CategoryViewTo;
import com.atguigu.gmall.model.to.SkuDetailTo;
import com.atguigu.starter.cache.annotation.GmallCache;
import com.atguigu.starter.cache.service.CacheOpsService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.concurrent.*;

@Slf4j
@Service
public class SkuDetailServiceImpl implements SkuDetailService {

    @Autowired
    SkuDetailFeignClient skuDetailFeignClient;

    @Autowired
    ThreadPoolExecutor executor;

    @Autowired
    StringRedisTemplate redisTemplate;
    @Autowired
    CacheOpsService cacheOpsService;

    /**
     * //表达式中params代表方法的所有参数列表
     * @param skuId
     * @return
     */
    //@Transactional
    @Override
    @GmallCache(
            cacheKey = SysRedisConst.SKU_INFO_PREFIX+"#{#params[0]}",
            bloomName = SysRedisConst.BLOOM_SKUID,
            bloomValue = "#{#params[0]}",
            lockName = SysRedisConst.LOCK_SKU_DETAIL+"#{#params[0]}"
    )
    public SkuDetailTo getSkuDetail(Long skuId) {
        SkuDetailTo fromRpc = getSkuDetailFromRpc(skuId);
        return fromRpc;
    }






    //每个skuId,关联一把自己的锁
   // Map<Long, ReentrantLock> lockPool = new ConcurrentHashMap<>();

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
            if(skuInfo != null){

                Result<List<SkuImage>> skuImages = skuDetailFeignClient.getSkuImages(skuId);
                skuInfo.setSkuImageList(skuImages.getData());
            }

        }, executor);



        //3.查实时价格
        CompletableFuture<Void> priceFuture = CompletableFuture.runAsync(() -> {
            Result<BigDecimal> price = skuDetailFeignClient.getSku101Price(skuId);
            detailTo.setPrice(price.getData());
        }, executor);
        


        //4.查销售属性名
        CompletableFuture<Void> saleAttrFuture = skuInfoFuture.thenAcceptAsync(skuInfo -> {
            if (skuInfo!=null) {

                Result<List<SpuSaleAttr>> skuSaleAttrValues = skuDetailFeignClient.getSkuSaleAttrValues(skuId, skuInfo.getSpuId());
                detailTo.setSpuSaleAttrList(skuSaleAttrValues.getData());
            }
        }, executor);

        //5.查sku组合
        CompletableFuture<Void> valueJsonFuture = skuInfoFuture.thenAcceptAsync(skuInfo -> {
                    if (skuInfo!=null) {
                        Result<String> skuValueJson = skuDetailFeignClient.getSkuValueJson(skuInfo.getSpuId());
                        detailTo.setValuesSkuJson(skuValueJson.getData());
                    }
        }, executor);


        //6.查分类
        CompletableFuture<Void> categoryViewFuture = skuInfoFuture.thenAcceptAsync(skuInfo -> {
                    if (skuInfo!=null) {
                        Result<CategoryViewTo> categoryView = skuDetailFeignClient.getCategoryView(skuInfo.getCategory3Id());
                        detailTo.setCategoryView(categoryView.getData());
                    }
        }, executor);

        CompletableFuture
                .allOf(imageFuture,priceFuture,saleAttrFuture,valueJsonFuture,categoryViewFuture)
                .join();
        return detailTo;
    }



    /*public SkuDetailTo getSkuDetailWithCache(Long skuId) {
        String cacheKey = SysRedisConst.SKU_INFO_PREFIX +skuId;
        //1.先查缓存
       SkuDetailTo cacheData = cacheOpsService.getCacheData(cacheKey,SkuDetailTo.class);
        //2.判断
        if(cacheData == null){
            //3.缓存没有
            //4.先问布隆 是否有这个商品
             boolean contain= cacheOpsService.bloomContains(skuId);
            if(!contain){
                //5.布隆说没有 一定没有
                log.info("布龙说没有 ,有隐藏的攻击风险");
                return new SkuDetailTo();
            }
            //6.布隆说有 可能有 需要回源查数据
            //为当前商品加自己的分布式锁 ,假如有100万请求查67号商品只会放进去一个请求
            boolean lock = cacheOpsService.tryLock(skuId);
            if(lock){
                //7.获取锁成功 查询远程
                System.out.println("------回源");
                log.info("[{}]缓存未命中,布隆说有 准备-----回源",skuId);
                SkuDetailTo fromRpc = getSkuDetailFromRpc(skuId);
                //8.数据放缓存
                cacheOpsService.saveData(cacheKey,fromRpc);
                //9.解锁
                cacheOpsService.unlock(skuId);
                return fromRpc;

            }
            //10.没获取到锁
            try {
                Thread.sleep(1000);

                return cacheOpsService.getCacheData(cacheKey,SkuDetailTo.class);

            } catch (InterruptedException e) {
                e.printStackTrace();
            }


        }
        //11.缓存中有

        return cacheData;
    }*/


   /* public SkuDetailTo getSkuDetailXXX(Long skuId) {



        //1. 看缓存中有没有 sku:info:20
        String jsonStr = redisTemplate.opsForValue().get("sku:info:" + skuId);

        if("x".equals(jsonStr)){
            //说明以前查过,但是数据库没有 为了避免再次回源 缓存了一个x作为占位符
            return null;
        }

        if(StringUtils.isEmpty(jsonStr)){
            //2.redis中没有
            //2.1回源 之前可以判断redis中保存到sku的id集合有没有这个id
            //防止随机值穿透? 回源之前 先要用布隆或者bitmap判断数据库中有没有

            //加锁解决击穿
            SkuDetailTo fromRpc = getSkuDetailFromRpc(skuId);
            //将查到的对象转成json字符串并放入redis
            String cacheJson="x";
            if(fromRpc!=null){
                cacheJson=Jsons.toStr(fromRpc);
                //加入雪崩解决方案  固定业务时间+随机过期时间
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
*/

}
