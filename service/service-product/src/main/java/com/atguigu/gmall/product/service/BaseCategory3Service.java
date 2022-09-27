package com.atguigu.gmall.product.service;


import com.atguigu.gmall.model.product.BaseCategory3;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 *
 */
public interface BaseCategory3Service extends IService<BaseCategory3> {

    //获取某个二级分类下的三级分类
    List<BaseCategory3> getCategory2Child(Long c2Id);
}
