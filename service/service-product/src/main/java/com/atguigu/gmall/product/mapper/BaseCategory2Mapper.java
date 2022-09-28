package com.atguigu.gmall.product.mapper;

import com.atguigu.gmall.model.product.BaseCategory2;

import com.atguigu.gmall.model.to.CategoryTreeTo;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

import java.util.List;

/**
 * @Entity com.atguigu.gmall.product.domain.BaseCategory2
 */
public interface BaseCategory2Mapper extends BaseMapper<BaseCategory2> {
    //查询所有分类以及子分类
    List<CategoryTreeTo> getAllCategoryWithTree();
}




