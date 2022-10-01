package com.atguigu.gmall.product.mapper;


import com.atguigu.gmall.model.product.SkuInfo;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

import java.util.List;

/**
 * @Entity com.atguigu.gmall.product.domain.SkuInfo
 */
public interface SkuInfoMapper extends BaseMapper<SkuInfo> {

    List<Long> getAllSkuId();
}




