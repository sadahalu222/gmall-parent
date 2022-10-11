package com.atguigu.gmall.cart.api;


import com.atguigu.gmall.cart.service.CartService;
import com.atguigu.gmall.common.auth.AuthUtils;
import com.atguigu.gmall.common.constant.SysRedisConst;
import com.atguigu.gmall.common.result.Result;
import com.atguigu.gmall.model.cart.CartInfo;
import com.atguigu.gmall.model.product.SkuInfo;
import com.atguigu.gmall.model.vo.user.UserAuthInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/inner/rpc/cart")
public class CartApiController {

    @Autowired
    CartService cartService;

    /**
     * 把商品加入购物车
     * @param skuId
     * @param
     * @return
     */
    @GetMapping("/addToCart")
    public Result<SkuInfo> addToCart(@RequestParam("skuId")Long skuId,
                                     @RequestParam("skuNum")Integer skuNum){

       SkuInfo skuInfo = cartService.addToCart(skuId,skuNum);

        return Result.ok(skuInfo);
    }

    /**
     * 删除购物车中选中的商品
     * @return
     */
    @GetMapping("/deleteChecked")
    public Result deleteChecked(){
        String cartKey = cartService.determinCartKey();
        cartService.deleteChecked(cartKey);
        return Result.ok();
    }


    /**
     * 获取当前购物车中选中的所有商品
     * @return
     */
    @GetMapping("/checked/list")
    public Result<List<CartInfo>> getChecked(){
        String cartKey = cartService.determinCartKey();
        List<CartInfo> checkedItems = cartService.getCheckedItems(cartKey);
        return Result.ok(checkedItems);
    }
}
