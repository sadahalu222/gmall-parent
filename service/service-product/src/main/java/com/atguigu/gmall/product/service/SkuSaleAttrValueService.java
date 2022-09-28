package com.atguigu.gmall.product.service;


import com.atguigu.gmall.model.product.SkuSaleAttrValue;
import com.atguigu.gmall.model.product.SpuSaleAttr;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 *
 */
public interface SkuSaleAttrValueService extends IService<SkuSaleAttrValue> {

    List<SpuSaleAttr> getSaleAttrAndValueBySpuId(Long spuId);

    //     并标识出当前sku到底是spu的哪种组合 页面要有高亮框 sku_sale_attr_value
    List<SpuSaleAttr> getSaleAttrAndValueMarkSku(Long spuId, Long skuId);
}
