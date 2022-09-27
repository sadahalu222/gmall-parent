package com.atguigu.gmall.product.service;


import com.atguigu.gmall.model.product.BaseAttrInfo;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 *
 */
public interface BaseAttrInfoService extends IService<BaseAttrInfo> {


    /**
     * //查询分类下的所有平台属性
     * @param c1Id 一级分类id
     * @param c2Id 二级分类id
     * @param c3Id  三级分类id
     * @return
     */
    List<BaseAttrInfo> getAttrInfoAndValueByCategoryId(Long c1Id, Long c2Id, Long c3Id);

    /**
     * 保存平台属性
     * @param info
     */
    void saveAttrInfo(BaseAttrInfo info);
}
