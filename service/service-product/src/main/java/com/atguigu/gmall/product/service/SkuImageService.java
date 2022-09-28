package com.atguigu.gmall.product.service;


import com.atguigu.gmall.model.product.SkuImage;
import com.atguigu.gmall.model.product.SpuImage;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 *
 */
public interface SkuImageService extends IService<SkuImage> {

    List<SpuImage> spuImageList(Long spuId);

    List<SkuImage> getSkuImages(Long skuId);
}
