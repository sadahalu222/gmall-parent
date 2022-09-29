package com.atguigu.gmall.product.service;


import com.atguigu.gmall.model.product.SpuSaleAttr;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 *
 */
public interface SpuSaleAttrService extends IService<SpuSaleAttr> {

    List<SpuSaleAttr> spuSaleAttrList(Long spuId);

    /**
     * 查询出所有sku销售属性组合 并封装成前端要的json
     * @param spuId
     * @return
     */
    String getAllSkuSaleAttrValueJson(Long spuId);
}
