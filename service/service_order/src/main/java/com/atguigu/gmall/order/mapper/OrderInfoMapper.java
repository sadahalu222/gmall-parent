package com.atguigu.gmall.order.mapper;


import com.atguigu.gmall.model.order.OrderInfo;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @Entity com.atguigu.gmall.order.domain.OrderInfo
 */
public interface OrderInfoMapper extends BaseMapper<OrderInfo> {

    void updateOrderStatus(@Param("orderId") Long orderId,
                           @Param("userId") Long userId,
                           @Param("processStatus") String processStatus,
                           @Param("orderStatus") String orderStatus,
                           @Param("espects") List<String> espects);
}




