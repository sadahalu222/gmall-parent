package com.atguigu.gmall.product.controller;

import com.atguigu.gmall.common.result.Result;
import com.atguigu.gmall.model.product.BaseTrademark;
import com.atguigu.gmall.product.service.BaseTrademarkService;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 品牌的API
 */
@RequestMapping("/admin/product")
@RestController
public class BaseTrademarkController {

    @Autowired
    BaseTrademarkService baseTrademarkService;

    /**
     * 分页查询所有品牌
     * @param page
     * @param size
     * @return
     */
    @GetMapping("baseTrademark/{page}/{limit}")
    public Result baseTrademark(@PathVariable("page")Long page,
                                @PathVariable("limit")Long size){

        Page<BaseTrademark> page1 = new Page<>(page, size);
        baseTrademarkService.page(page1);
        return Result.ok(page1);
    }

    /**
     * 根据Id获取品牌
     * @param id
     * @return
     */
    @GetMapping("baseTrademark/get/{id}")
    public Result getBaseTrademarkById(@PathVariable("id")Long id){

        BaseTrademark trademark = baseTrademarkService.getById(id);

        return Result.ok(trademark);
    }

    /**
     * 添加品牌
     * @param
     * @return
     */
    @PostMapping("baseTrademark/save")
    public Result saveBaseTrademark(@RequestBody BaseTrademark baseTrademark){

        baseTrademarkService.save(baseTrademark);

        return Result.ok();
    }

    /**
     * 3、修改品牌
     * @param baseTrademark
     * @return
     */
    @PutMapping("baseTrademark/update")
    public Result updateBaseTrademark(@RequestBody BaseTrademark baseTrademark) {

        baseTrademarkService.updateById(baseTrademark);
        return Result.ok();
    }


    /**
     * 4、根据id删除品牌
     * @param id
     * @return
     */
    @DeleteMapping("baseTrademark/remove/{id}")
    public Result deleteBaseTrademarkById(@PathVariable("id")Long id){
        baseTrademarkService.removeById(id);
        return Result.ok();
    }

    /**
     * 获取所有品牌
     * @return
     */
    @GetMapping("baseTrademark/getTrademarkList")
    public Result getTrademarkList(){
        List<BaseTrademark> list =
                baseTrademarkService.list();

        return Result.ok(list);
    }






}
