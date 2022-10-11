package com.atguigu.gmall.web.controller;

import com.atguigu.gmall.common.constant.SysRedisConst;
import com.atguigu.gmall.common.result.Result;
import com.atguigu.gmall.feign.cart.CartFeignClient;
import com.atguigu.gmall.model.product.SkuInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.concurrent.ConcurrentHashMap;

@Controller
public class CartController {

 //   public static final ConcurrentHashMap<Thread,String> map=new ConcurrentHashMap<>();
//public static final ThreadLocal<String> threadLocal=new ThreadLocal<>();
    @Autowired
    CartFeignClient cartFeignClient;

    /**
     * 添加商品到购物车
     * @param skuId
     * @param skuNum
     * @return
     */
    @GetMapping("/addCart.html")
    public String addCartHtml(@RequestParam("skuId")Long skuId,
                              @RequestParam("skuNum")Integer skuNum, Model model){

        //1 把指定商品添加到购物车
       // map.put(Thread.currentThread(), userId);
      //  threadLocal.set(userId);
        Result<SkuInfo> result = cartFeignClient.addToCart(skuId, skuNum);
        if (result.isOk()){
            model.addAttribute("skuInfo", result.getData());
            model.addAttribute("skuNum", skuNum);
        }
        return "cart/addCart";
    }

    /**
     * 购物车列表页
     * @return
     */
    @GetMapping("/cart.html")
    public String cartHtml(){


        return "cart/index";
    }
    /**
     * 删除购物车中选中商品
     * @return
     */
    @GetMapping("/cart/deleteChecked")
    public String deleteChecked(){

        /**
         * redirect: 重定向
         * forward: 转发
         */
       cartFeignClient.deleteChecked();
        return "redirect:http://cart.gmall.com/cart.html";
    }


}
