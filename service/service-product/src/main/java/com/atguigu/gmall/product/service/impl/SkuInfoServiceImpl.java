package com.atguigu.gmall.product.service.impl;

import com.atguigu.gmall.model.product.*;
import com.atguigu.gmall.model.to.CategoryViewTo;
import com.atguigu.gmall.model.to.SkuDetailTo;
import com.atguigu.gmall.product.mapper.BaseCategory2Mapper;
import com.atguigu.gmall.product.mapper.BaseCategory3Mapper;
import com.atguigu.gmall.product.service.*;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import com.atguigu.gmall.product.mapper.SkuInfoMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

/**
 *
 */
@Service
public class SkuInfoServiceImpl extends ServiceImpl<SkuInfoMapper, SkuInfo>
    implements SkuInfoService{
    @Autowired
    SkuInfoMapper skuInfoMapper;
    @Autowired
    SkuAttrValueService skuAttrValueService;

    @Autowired
    SkuImageService skuImageService;
    @Autowired
    SkuSaleAttrValueService skuSaleAttrValueService;
    @Autowired
    BaseCategory3Mapper baseCategory3Mapper;
    @Autowired
    SpuSaleAttrService spuSaleAttrService;

    @Override
    public void onSale(Long skuId) {
        SkuInfo skuInfo = skuInfoMapper.selectById(skuId);
        skuInfo.setIsSale(1);
        skuInfoMapper.updateById(skuInfo);
        //todo 从es中保存产品
    }

    @Override
    public void cancelSale(Long skuId) {
        SkuInfo skuInfo = skuInfoMapper.selectById(skuId);
        skuInfo.setIsSale(0);
        skuInfoMapper.updateById(skuInfo);
        //todo 从es中删除产品
    }

    @Override
    public void saveSkuInfo(SkuInfo skuInfo) {
        skuInfoMapper.insert(skuInfo);

        List<SkuAttrValue> skuAttrValueList = skuInfo.getSkuAttrValueList();
        for (SkuAttrValue skuAttrValue : skuAttrValueList) {
            skuAttrValue.setSkuId(skuInfo.getId());
        }
        skuAttrValueService.saveBatch(skuAttrValueList);


        List<SkuImage> skuImageList = skuInfo.getSkuImageList();
        for (SkuImage skuImage : skuImageList) {
            skuImage.setSkuId(skuInfo.getId());
        }
        skuImageService.saveBatch(skuImageList);

        List<SkuSaleAttrValue> skuSaleAttrValueList = skuInfo.getSkuSaleAttrValueList();
        for (SkuSaleAttrValue skuSaleAttrValue : skuSaleAttrValueList) {
        //前端提交的不是都要的 有冗余字段
           skuSaleAttrValue.setSkuId(skuInfo.getId());
           skuSaleAttrValue.setSpuId(skuInfo.getSpuId());
        }
        skuSaleAttrValueService.saveBatch(skuSaleAttrValueList);

    }

    @Deprecated
    @Override
    public SkuDetailTo getSkuDetail(Long skuId) {
        SkuDetailTo detailTo = new SkuDetailTo();
        //0. 插叙到skuInfo
        SkuInfo skuInfo = skuInfoMapper.selectById(skuId);

        //1.商品(sku)所属的完整分类信息 base_category1 base_category2  base_category3
         CategoryViewTo categoryView = baseCategory3Mapper.getCategoryView(skuInfo.getCategory3Id());
        detailTo.setCategoryView(categoryView);
        //2.商品(sku)的基本信息[价格 重量 名字..] sku_info
        detailTo.setSkuInfo(skuInfo);

        //3.商品(sku)的图片  sku_image
        List<SkuImage> imageList = skuImageService.getSkuImages(skuId);
        skuInfo.setSkuImageList(imageList);
        //实时价格查询
        BigDecimal price = this.get1010Price(skuId);
        detailTo.setPrice(price);
        //4.商品(sku)的所属SPU当时定义的所有销售属性名值组合
        //          spu_sale_attr  spu_sale_attr_value
        //     并标识出当前sku到底是spu的哪种组合 页面要有高亮框 sku_sale_attr_value
       List<SpuSaleAttr> saleAttrList = skuSaleAttrValueService.getSaleAttrAndValueMarkSku(skuInfo.getSpuId(),skuId);
        detailTo.setSpuSaleAttrList(saleAttrList);
        //5.商品(sku)的所有兄弟的销售属性名和值组合关系全部查出来,并封装成
        // {"118|120":50,"119|121":49} 这样的json字符串
        Long spuId=skuInfo.getSpuId();
        String valueJson= spuSaleAttrService.getAllSkuSaleAttrValueJson(spuId);
        detailTo.setValuesSkuJson(valueJson);
        //5.商品(sku)类型推荐                         (x)
        //6.商品(sku)介绍 [所属spu的海报]  spu_poster  (x)
        //7.商品(sku)规格参数  sku_attr_value
        //8.商品(sku)售后 评论..   相关的表            (x)
        return detailTo;
    }

    @Override
    public BigDecimal get1010Price(Long skuId) {
        SkuInfo skuInfo = skuInfoMapper.selectById(skuId);

        return skuInfo.getPrice();
    }

    @Override
    public SkuInfo getDetailSkuInfo(Long skuId) {
        SkuInfo skuInfo = skuInfoMapper.selectById(skuId);
        return skuInfo;
    }

    @Override
    public List<SkuImage> getDetailSkuImages(Long skuId) {

        List<SkuImage> imageList = skuImageService.getSkuImages(skuId);
        return imageList;
    }


}




