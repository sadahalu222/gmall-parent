package com.atguigu.gmall.product.service;


import com.atguigu.gmall.model.product.SkuInfo;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 *
 */
public interface SkuInfoService extends IService<SkuInfo> {


   void onSale(Long skuId) ;

    void cancelSale(Long skuId);

    void saveSkuInfo(SkuInfo skuInfo);
}
