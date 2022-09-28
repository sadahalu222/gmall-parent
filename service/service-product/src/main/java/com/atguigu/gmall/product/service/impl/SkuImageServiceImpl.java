package com.atguigu.gmall.product.service.impl;


import com.atguigu.gmall.model.product.SkuImage;
import com.atguigu.gmall.model.product.SpuImage;
import com.atguigu.gmall.product.mapper.SpuImageMapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import com.atguigu.gmall.product.service.SkuImageService;
import com.atguigu.gmall.product.mapper.SkuImageMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 *
 */
@Service
public class SkuImageServiceImpl extends ServiceImpl<SkuImageMapper, SkuImage>
    implements SkuImageService{

    @Autowired
    SpuImageMapper spuImageMapper;

    @Autowired
    SkuImageMapper skuImageMapper;
    @Override
    public List<SpuImage> spuImageList(Long spuId) {
        QueryWrapper<SpuImage> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("spu_id", spuId);
        List<SpuImage> imageList = spuImageMapper.selectList(queryWrapper);
        return imageList;

    }

    @Override
    public List<SkuImage> getSkuImages(Long skuId) {
        QueryWrapper<SkuImage> wrapper = new QueryWrapper<>();
        wrapper.eq("sku_id", skuId);
        List<SkuImage> imageList = skuImageMapper.selectList(wrapper);

        return imageList;
    }
}




