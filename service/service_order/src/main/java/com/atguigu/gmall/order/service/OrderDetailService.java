package com.atguigu.gmall.order.service;

import com.atguigu.gmall.model.order.OrderDetail;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 *
 */
public interface OrderDetailService extends IService<OrderDetail> {

    /**
     * 查询某个订单的明细
     * @param orderId
     * @param userId
     * @return
     */
    List<OrderDetail> getOrderDetail(Long orderId, Long userId);
}
