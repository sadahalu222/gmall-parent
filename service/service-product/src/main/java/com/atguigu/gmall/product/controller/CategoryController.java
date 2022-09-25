package com.atguigu.gmall.product.controller;

import com.atguigu.gmall.common.result.Result;
import com.atguigu.gmall.model.product.BaseCategory1;
import com.atguigu.gmall.model.product.BaseCategory2;
import com.atguigu.gmall.product.service.BaseCategory1Service;
import com.atguigu.gmall.product.service.BaseCategory2Service;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.api.R;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
@RequestMapping("/admin/product")
@RestController
public class CategoryController {
    //提交git的令牌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
    @Autowired
    BaseCategory1Service baseCategory1Service;
    @Autowired
    BaseCategory2Service baseCategory2Service;

    /**
     * 获取所有的一级分类 admin/product/getCategory1
     * @return
     */
    @GetMapping("getCategory1")
    public Result getCategory1(){

        //TODO 查询所有的一级分类
        List<BaseCategory1> list = baseCategory1Service.list(null);

        return Result.ok(list);
    }

    /**
     * 获取某个一级分类下的二级分类
     * @param c1Id  一级分类id
     * @return
     */
    @GetMapping("getCategory2/{category1Id}")
    public Result getCategory2(@PathVariable("category1Id") Long c1Id ){

        List<BaseCategory2> list=baseCategory2Service.getCategory1Child(c1Id);

        return Result.ok(list);
    }















}
