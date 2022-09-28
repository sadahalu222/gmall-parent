package com.atguigu.gmall.product.service.impl;

import com.atguigu.gmall.model.product.BaseCategory2;
import com.atguigu.gmall.model.product.BaseCategory3;
import com.atguigu.gmall.model.to.CategoryTreeTo;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import com.atguigu.gmall.product.service.BaseCategory2Service;
import com.atguigu.gmall.product.mapper.BaseCategory2Mapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 *
 */
@Service
public class BaseCategory2ServiceImpl extends ServiceImpl<BaseCategory2Mapper, BaseCategory2>
    implements BaseCategory2Service{

    @Autowired
    BaseCategory2Mapper baseCategory2Mapper;

    //获取某个一级分类下的二级分类
    @Override
    public List<BaseCategory2> getCategory1Child(Long c1Id) {

        QueryWrapper<BaseCategory2> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("category1_id", c1Id);
        List<BaseCategory2> list = baseMapper.selectList(queryWrapper);

        return list;
    }

    @Override
    public List<CategoryTreeTo> getAllCategoryWithTree() {
        return baseCategory2Mapper.getAllCategoryWithTree();

    }


}




