package com.atguigu.gmall.order.service;


import com.atguigu.gmall.model.enums.ProcessStatus;
import com.atguigu.gmall.model.order.OrderInfo;
import com.atguigu.gmall.model.vo.order.OrderSubmitVo;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 *
 */
public interface OrderInfoService extends IService<OrderInfo> {

    /**
     * 根据页面提交内容生成一个数据库的订单
     * @param submitVo
     * @return
     */
    Long saveOrder(OrderSubmitVo submitVo,String tradeNo);

    /**
     * 幂等修改订单状态
     * @param orderId
     * @param userId
     * @param closed
     * @param expected
     */
    void changeOrderStatus(Long orderId, Long userId, ProcessStatus closed, List<ProcessStatus> expected);


    /**
     * 查询订单数据
     * @param orderId
     * @param userId
     * @return
     */
    OrderInfo getOrderInfoByOrderIdAndUserId(Long orderId, Long userId);
    //根据对外交易号和用户id获取订单信息
    OrderInfo getOrderInfoByOutTradeNoAndUserId(String outTradeNo, Long userId);

}
