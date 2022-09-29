package com.atguigu.gmall.product.service;


import com.atguigu.gmall.model.product.BaseCategory3;
import com.atguigu.gmall.model.to.CategoryViewTo;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 *
 */
public interface BaseCategory3Service extends IService<BaseCategory3> {

    //获取某个二级分类下的三级分类
    List<BaseCategory3> getCategory2Child(Long c2Id);

    /**
     * 根据三级分类id 查出整个精确分类路径
     * @param c3Id
     * @return
     */
    CategoryViewTo getCategoryView(Long c3Id);
}
