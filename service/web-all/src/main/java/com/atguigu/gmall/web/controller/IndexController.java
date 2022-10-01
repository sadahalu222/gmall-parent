package com.atguigu.gmall.web.controller;

import com.atguigu.gmall.common.result.Result;
import com.atguigu.gmall.feign.product.CategoryFeignClient;
import com.atguigu.gmall.model.to.CategoryTreeTo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Controller
public class IndexController {

    @Autowired
    CategoryFeignClient categoryFeignClient;

    /**
     * 跳到首页
     * @return
     */
    @GetMapping({"/","/index"})
    public String indexPage(Model model){

        //远程调用
        Result<List<CategoryTreeTo>> result = categoryFeignClient.getAllCategoryWithTree();

        if (result.isOk()){
            model.addAttribute("list", result.getData());
        }




        return "index/index";
    }
}
