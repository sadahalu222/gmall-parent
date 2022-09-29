package com.atguigu.gmall.product.api;

import com.atguigu.gmall.common.result.Result;
import com.atguigu.gmall.model.to.CategoryTreeTo;
import com.atguigu.gmall.product.service.BaseCategory2Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 分类有关的API
 * 以后远程调用都是内部接口: 命名规范 /api/inner/rpc
 */
@RequestMapping("/api/inner/rpc/product")
@RestController
public class CategoryApiController {

    @Autowired
    BaseCategory2Service baseCategory2Service;
    /**
     * 查询所有的分类并封装成树形菜单结构
     * @return
     */
    @GetMapping("/category/tree")
    public Result<List<CategoryTreeTo>> getAllCategoryWithTree() {

        List<CategoryTreeTo> categoryTreeTos = baseCategory2Service.getAllCategoryWithTree();

        return Result.ok(categoryTreeTos);
    }
}
