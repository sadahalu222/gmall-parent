package com.atguigu.gmall.feign.cart;

import com.atguigu.gmall.common.result.Result;
import com.atguigu.gmall.model.cart.CartInfo;
import com.atguigu.gmall.model.product.SkuInfo;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@FeignClient("service-cart")
@RequestMapping("/api/inner/rpc/cart")
public interface CartFeignClient {
    @GetMapping("/addToCart")
    public Result<SkuInfo> addToCart(@RequestParam("skuId")Long skuId,
                                     @RequestParam("skuNum")Integer skuNum);

    /**
     * 删除购物车中选中的商品
     * @return
     */
    @GetMapping("/deleteChecked")
    public Result deleteChecked();

    /**
     * 获取当前购物车中选中的所有商品
     * @return
     */
    @GetMapping("/checked/list")
    public Result<List<CartInfo>> getChecked();
}
