package com.atguigu.gmall.product.controller;

import com.atguigu.gmall.common.result.Result;
import com.atguigu.gmall.model.product.SpuInfo;
import com.atguigu.gmall.product.service.SpuInfoService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/admin/product")
public class SupController {

    @Autowired
    SpuInfoService spuInfoService;

    /**
     * spu分页查询
     * @param page
     * @param limit
     * @param category3Id
     * @return
     */
    @GetMapping("/{page}/{limit}")
    public Result getSpuPage(@PathVariable("page")Long page,
                             @PathVariable("limit")Long limit,
                             @RequestParam("category3Id")Long category3Id){
        Page<SpuInfo> infoPage = new Page<>(page, limit);

        QueryWrapper<SpuInfo> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("category3_id", category3Id);
        spuInfoService.page(infoPage, queryWrapper);

        return Result.ok(infoPage);

    }

    /**
     * 添加spu
     * @param spuInfo
     * @return
     */
    @PostMapping("saveSpuInfo")
    public Result saveSpuInfo(@RequestBody SpuInfo spuInfo){
        spuInfoService.saveSpuInfo(spuInfo);

        return Result.ok();
    }
}
