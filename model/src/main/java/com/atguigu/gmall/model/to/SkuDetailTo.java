package com.atguigu.gmall.model.to;

import com.atguigu.gmall.model.product.SkuInfo;
import com.atguigu.gmall.model.product.SpuSaleAttr;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

@Data
public class SkuDetailTo {
    private CategoryViewTo categoryView;//当前sku所属的商品分类信息
    private SkuInfo skuInfo;//商品的基本信息
    private BigDecimal price;

    private List<SpuSaleAttr> spuSaleAttrList;//spu的所有属性列表
    private String valuesSkuJson;

}
