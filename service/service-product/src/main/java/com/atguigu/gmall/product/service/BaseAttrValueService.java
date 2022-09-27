package com.atguigu.gmall.product.service;


import com.atguigu.gmall.model.product.BaseAttrValue;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 *
 */
public interface BaseAttrValueService extends IService<BaseAttrValue> {
    //根据平台属性id 获取这个属性的所有属性值
    List<BaseAttrValue> getAttrValueList(Long attrId);
}
