package com.atguigu.gmall.product.mapper;

import com.atguigu.gmall.model.product.BaseCategory3;

import com.atguigu.gmall.model.to.CategoryViewTo;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

/**
 * @Entity com.atguigu.gmall.product.domain.BaseCategory3
 */
public interface BaseCategory3Mapper extends BaseMapper<BaseCategory3> {

    /**
     * 根据三级分类id查出所有三个等级分类的id和name
     * 并封装成CategoryView
     * @param category3Id
     * @return
     */
    CategoryViewTo getCategoryView(@Param("category3Id") Long category3Id);
}




