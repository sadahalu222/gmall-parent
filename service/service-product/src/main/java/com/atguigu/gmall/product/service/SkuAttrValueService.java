package com.atguigu.gmall.product.service;


import com.atguigu.gmall.model.list.SearchAttr;
import com.atguigu.gmall.model.product.SkuAttrValue;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 *
 */
public interface SkuAttrValueService extends IService<SkuAttrValue> {
    /**
     * 查当前sku所有平台属性名和值
     * @param skuId
     * @return
     */
    List<SearchAttr> getSkuAttrNameAndValue(Long skuId);
}
