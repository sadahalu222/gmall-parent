package com.atguigu.gmall.product.controller;

import com.atguigu.gmall.common.result.Result;
import com.atguigu.gmall.model.product.BaseAttrInfo;
import com.atguigu.gmall.model.product.BaseAttrValue;
import com.atguigu.gmall.product.service.BaseAttrInfoService;
import com.atguigu.gmall.product.service.BaseAttrValueService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 平台属性相关API
 */
@RequestMapping("/admin/product")
@RestController
public class BaseAttrController {
    @Autowired
    BaseAttrInfoService baseAttrInfoService;
    @Autowired
    BaseAttrValueService baseAttrValueService;

    /**
     * //查询分类下的所有平台属性
     * @param c1Id 一级分类id
     * @param c2Id 二级分类id
     * @param c3Id  三级分类id
     * @return
     */
    @GetMapping("attrInfoList/{c1Id}/{c2Id}/{c3Id}")
    public Result getAttrInfoList(@PathVariable("c1Id")Long c1Id,
                                  @PathVariable("c2Id")Long c2Id,
                                  @PathVariable("c3Id")Long c3Id){
        List<BaseAttrInfo> infos = baseAttrInfoService.getAttrInfoAndValueByCategoryId(c1Id,c2Id,c3Id);

        return Result.ok(infos);
    }

    /**
     * 保存 修改属性信息二合一的方法  根据id判断
     * 前端把所有页面录入的数据以json的格式post请求发给我们
     * 请求体:
     * {"id":null,"attrName":"上市日期","category1Id":0,"category2Id":0,"category3Id":0,"attrValueList":[{"valueName":"2019","edit":false},{"valueName":"2020","edit":false},{"valueName":"2021","edit":false}],"categoryId":2,"categoryLevel":1}
     * 取出前端发送请求的请求题数据@RequestBody
     * 并把这个数据(json)转成指定的BaseAttrInfo对象
     * @param info  BaseAttrInfo对象 封装前端提交来的所有数据
     * @return
     */
    @PostMapping("saveAttrInfo")
    public Result saveAttrInfo(@RequestBody BaseAttrInfo info){

        //保存或者修改 平台新增
        baseAttrInfoService.saveAttrInfo(info);
        return Result.ok();
    }

    /**
     * 根据平台属性id 获取这个属性的所有属性值
     * @param attrId
     * @return
     */
    @GetMapping("getAttrValueList/{attrId}")
    public Result getAttrValueList(@PathVariable("attrId")Long attrId){

        List<BaseAttrValue> values = baseAttrValueService.getAttrValueList(attrId);

        return Result.ok(values);
    }















}
