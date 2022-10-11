package com.atguigu.gmall.order.controller;


import com.atguigu.gmall.common.result.Result;
import com.atguigu.gmall.model.vo.order.OrderWareMapVo;
import com.atguigu.gmall.model.vo.order.WareChildOrderVo;
import com.atguigu.gmall.order.biz.OrderBizService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Slf4j
@RequestMapping("/api/order")
@RestController
public class OrderSpiltController {

    @Autowired
    OrderBizService orderBizService;
    /**
     * 拆单
     * @param params
     * @return
     */
    @PostMapping("/orderSplit")
    public List<WareChildOrderVo> orderSpilt(OrderWareMapVo params){
        log.info("订单执行拆单{}",params);

        return orderBizService.orderSplit(params);
    }

}
