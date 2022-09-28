package com.atguigu.gmall.product.service;


import com.atguigu.gmall.model.product.SkuInfo;
import com.atguigu.gmall.model.to.SkuDetailTo;
import com.baomidou.mybatisplus.extension.service.IService;

import java.math.BigDecimal;

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
}
