package com.atguigu.gmall.product.service;


import com.atguigu.gmall.model.list.Goods;
import com.atguigu.gmall.model.product.SkuImage;
import com.atguigu.gmall.model.product.SkuInfo;
import com.atguigu.gmall.model.to.SkuDetailTo;
import com.baomidou.mybatisplus.extension.service.IService;

import java.math.BigDecimal;
import java.util.List;

/**
 *
 */
public interface SkuInfoService extends IService<SkuInfo> {


   void onSale(Long skuId) ;

    void cancelSale(Long skuId);

    void saveSkuInfo(SkuInfo skuInfo);

    /**
     * 商品详情
     * @param skuId
     * @return
     */
    SkuDetailTo getSkuDetail(Long skuId);

    /**
     * 获取商品实时价格
     * @param skuId
     * @return
     */
    BigDecimal get1010Price(Long skuId);

 /**
  * 获取sku的基本信息
  * @param skuId
  * @return
  */
 SkuInfo getDetailSkuInfo(Long skuId);

 /**
  * 查询sku的图片信息
  * @param skuId
  * @return
  */
 List<SkuImage> getDetailSkuImages(Long skuId);

 /**
  * 找到所有的商品id
  * @return
  */
    List<Long> findAllSkuId();

 /**
  * 得到某个sku在es中需要存储的所有数据
  * @param skuId
  * @return
  */
 Goods getGoodsBySkuId(Long skuId);

}
