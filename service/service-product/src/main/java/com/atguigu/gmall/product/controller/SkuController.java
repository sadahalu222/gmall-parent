package com.atguigu.gmall.product.controller;

import com.atguigu.gmall.common.result.Result;
import com.atguigu.gmall.model.product.SkuInfo;
import com.atguigu.gmall.model.product.SpuImage;
import com.atguigu.gmall.model.product.SpuSaleAttr;
import com.atguigu.gmall.product.service.SkuImageService;
import com.atguigu.gmall.product.service.SkuInfoService;
import com.atguigu.gmall.product.service.SpuSaleAttrService;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.checkerframework.checker.units.qual.A;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * sku管理
 */
@RequestMapping("/admin/product")
@RestController
public class SkuController {

    @Autowired
    SkuInfoService skuInfoService;
    @Autowired
    SkuImageService skuImageService;
    @Autowired
    SpuSaleAttrService spuSaleAttrService;

    /**
     * sku分页
     * @param page
     * @param limit
     * @return
     */
    @GetMapping("list/{page}/{limit}")
    public Result skuInfoPageList(@PathVariable("page")Long page,
                                  @PathVariable("limit")Long limit){
        Page<SkuInfo> skuInfoPage = new Page<>(page, limit);
        skuInfoService.page(skuInfoPage);
        return Result.ok(skuInfoPage);

    }

    /**
     * 根据spuId获取图片列表
     * @param spuId
     * @return
     */
    @GetMapping("spuImageList/{spuId}")
    public Result spuImageList(@PathVariable("spuId")Long spuId
                                 ){

        List<SpuImage> list = skuImageService.spuImageList(spuId);
        return Result.ok(list);

    }

    /**
     * 根据spuId获取销售属性
     * @param spuId
     * @return
     */
    @GetMapping("spuSaleAttrList/{spuId}")
    public Result spuSaleAttrList(@PathVariable("spuId")Long spuId
    ){

        List<SpuSaleAttr> list = spuSaleAttrService.spuSaleAttrList(spuId);
        return Result.ok(list);

    }

    /**
     * 添加sku
     * @param skuInfo
     * @return
     */
    @PostMapping("saveSkuInfo")
    public Result saveSkuInfo(@RequestBody SkuInfo skuInfo) {

        skuInfoService.saveSkuInfo(skuInfo);
        return Result.ok();
    }

    /**
     * 上架
     * @param skuId
     * @return
     */
    @GetMapping("/onSale/{skuId}")
    public Result onSale(@PathVariable("skuId")Long skuId){

        skuInfoService.onSale(skuId);

        return Result.ok();

    }
    /**
     * 修改sku信息
     */
    public void updateSkuInfo(SkuInfo skuInfo){
     //   skuInfoService.updateSkuInfo(skuInfo);
       // cacheOpsService.delay2Delete(skuInfo.getId());
    }

    /**
     * 下架
     * @param skuId
     * @return
     */
    @GetMapping("/cancelSale/{skuId}")
    public Result cancelSale(@PathVariable("skuId")Long skuId){

        skuInfoService.cancelSale(skuId);
        return Result.ok();

    }

}
