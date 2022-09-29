package com.atguigu.gmall.product.service.impl;

import com.atguigu.gmall.model.product.BaseCategory3;
import com.atguigu.gmall.model.to.CategoryViewTo;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import com.atguigu.gmall.product.service.BaseCategory3Service;
import com.atguigu.gmall.product.mapper.BaseCategory3Mapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 *
 */
@Service
public class BaseCategory3ServiceImpl extends ServiceImpl<BaseCategory3Mapper, BaseCategory3>
    implements BaseCategory3Service{

    @Autowired
    BaseCategory3Mapper baseCategory3Mapper;
    //获取某个二级分类下的三级分类
    @Override
    public List<BaseCategory3> getCategory2Child(Long c2Id) {

        QueryWrapper<BaseCategory3> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("category2_id", c2Id);
        List<BaseCategory3> list = baseMapper.selectList(queryWrapper);

        return list;
    }

    @Override
    public CategoryViewTo getCategoryView(Long c3Id) {
        CategoryViewTo categoryView = baseCategory3Mapper.getCategoryView(c3Id);
        return categoryView;
    }
}




