package com.atguigu.gmall.product.service;


import com.atguigu.gmall.model.product.BaseCategory2;
import com.atguigu.gmall.model.product.BaseCategory3;
import com.atguigu.gmall.model.to.CategoryTreeTo;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 *
 */
public interface BaseCategory2Service extends IService<BaseCategory2> {

    //获取某个一级分类下的二级分类
    List<BaseCategory2> getCategory1Child(Long c1Id);

    /**
     * 查询所有分类下边的子分类 并组装成三级树结构
     * @return
     */
    List<CategoryTreeTo> getAllCategoryWithTree();
}
