package com.atguigu.gmall.product.service.impl;

import com.atguigu.gmall.common.util.Jsons;
import com.atguigu.gmall.model.product.SpuSaleAttr;
import com.atguigu.gmall.model.product.SpuSaleAttrValue;
import com.atguigu.gmall.model.to.ValueSkuJsonTo;
import com.atguigu.gmall.product.mapper.SpuSaleAttrValueMapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import com.atguigu.gmall.product.service.SpuSaleAttrService;
import com.atguigu.gmall.product.mapper.SpuSaleAttrMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *
 */
@Service
public class SpuSaleAttrServiceImpl extends ServiceImpl<SpuSaleAttrMapper, SpuSaleAttr>
    implements SpuSaleAttrService{

    @Autowired
    SpuSaleAttrMapper spuSaleAttrMapper;
    @Autowired
    SpuSaleAttrValueMapper spuSaleAttrValueMapper;

    @Override
    public List<SpuSaleAttr> spuSaleAttrList(Long spuId) {

        QueryWrapper<SpuSaleAttr> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("spu_id", spuId);
        List<SpuSaleAttr> spuSaleAttrList = spuSaleAttrMapper.selectList(queryWrapper);

        for (SpuSaleAttr spuSaleAttr : spuSaleAttrList) {

            QueryWrapper<SpuSaleAttrValue> queryWrapper2 = new QueryWrapper<>();
            queryWrapper2.eq("spu_id", spuId);
            queryWrapper2.eq("base_sale_attr_id", spuSaleAttr.getBaseSaleAttrId());

            List<SpuSaleAttrValue> spuSaleAttrValueList = spuSaleAttrValueMapper.selectList(queryWrapper2);
            spuSaleAttr.setSpuSaleAttrValueList(spuSaleAttrValueList);

        }





        return spuSaleAttrList;
    }

    @Override
    public String getAllSkuSaleAttrValueJson(Long spuId) {

        List<ValueSkuJsonTo> valueSkuJsonTos=spuSaleAttrMapper.getAllSkuValueJson(spuId);
        //封装{"118|120":50,"119|121":49} 这样的json字符串
        Map<String,Long> map=new HashMap<>();
        for (ValueSkuJsonTo valueSkuJsonTo : valueSkuJsonTos) {
            Long skuId = valueSkuJsonTo.getSkuId();
            String valueJson = valueSkuJsonTo.getValueJson();
            map.put(valueJson,skuId);
        }

        //转字符串
        String json= Jsons.toStr(map);
        return json;
    }
}




