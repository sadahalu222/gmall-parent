package com.atguigu.gmall.order.controller;


import com.atguigu.gmall.common.auth.AuthUtils;
import com.atguigu.gmall.common.result.Result;
import com.atguigu.gmall.model.order.OrderDetail;
import com.atguigu.gmall.model.order.OrderInfo;
import com.atguigu.gmall.model.vo.order.OrderSubmitVo;
import com.atguigu.gmall.order.biz.OrderBizService;
import com.atguigu.gmall.order.service.OrderDetailService;
import com.atguigu.gmall.order.service.OrderInfoService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/order/auth")
public class OrderRestController {

    @Autowired
    OrderBizService orderBizService;

  /*  @Autowired
    RabbitTemplate rabbitTemplate;*/

    @Autowired
    OrderInfoService orderInfoService;

    @Autowired
    OrderDetailService orderDetailService;
    /**
     * 提交订单
     * @return
     */
    @PostMapping("/submitOrder")
    public Result submitOrder(@RequestParam("tradeNo") String tradeNo,
                              @RequestBody OrderSubmitVo submitVo){

        Long orderId = orderBizService.submitOrder(submitVo,tradeNo);


        return Result.ok(orderId.toString());
    }

    /**
     * 订单列表展示
     * @param pn
     * @param ps
     * @return
     */
    @GetMapping("/{pn}/{ps}")
    public Result orderList(@PathVariable("pn") Long pn, @PathVariable("ps") Long ps){
        Long userId = AuthUtils.getCurrentAuthinfo().getUserId();

        Page<OrderInfo> page = new Page<>(pn,ps);
        LambdaQueryWrapper<OrderInfo> wrapper = new LambdaQueryWrapper<OrderInfo>()
                .eq(OrderInfo::getUserId, userId);
        //1、查询orderInfo
        Page<OrderInfo> infoPage = orderInfoService.page(page, wrapper);

        //2、查询orderInfo的所有商品
        infoPage.getRecords().stream()
                .parallel()
                .forEach(orderInfo -> {
                    //查询订单详情
                    List<OrderDetail> orderDetails = orderDetailService.getOrderDetail(orderInfo.getId(), orderInfo.getUserId());
                    orderInfo.setOrderDetailList(orderDetails);
                });


        return Result.ok(infoPage);
    }
}
