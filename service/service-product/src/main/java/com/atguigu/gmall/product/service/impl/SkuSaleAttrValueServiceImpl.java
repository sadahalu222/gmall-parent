package com.atguigu.gmall.product.service.impl;

import com.atguigu.gmall.model.product.SkuSaleAttrValue;
import com.atguigu.gmall.model.product.SpuSaleAttr;
import com.atguigu.gmall.product.mapper.SpuSaleAttrMapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import com.atguigu.gmall.product.service.SkuSaleAttrValueService;
import com.atguigu.gmall.product.mapper.SkuSaleAttrValueMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 *
 */
@Service
public class SkuSaleAttrValueServiceImpl extends ServiceImpl<SkuSaleAttrValueMapper, SkuSaleAttrValue>
    implements SkuSaleAttrValueService{

    @Autowired
    SpuSaleAttrMapper spuSaleAttrMapper;
    @Override
    public List<SpuSaleAttr> getSaleAttrAndValueBySpuId(Long spuId) {
        List<SpuSaleAttr> list=spuSaleAttrMapper.getSaleAttrAndValueBySpuId(spuId);
        return list;
    }

    @Override
    public List<SpuSaleAttr> getSaleAttrAndValueMarkSku(Long spuId, Long skuId) {
        return spuSaleAttrMapper.getSaleAttrAndValueMarkSku(spuId, skuId);
    }
}




