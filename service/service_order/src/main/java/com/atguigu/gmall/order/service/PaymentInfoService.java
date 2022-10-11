package com.atguigu.gmall.order.service;


import com.atguigu.gmall.model.payment.PaymentInfo;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.Map;

/**
 *
 */
public interface PaymentInfoService extends IService<PaymentInfo> {

    /**
     * 保存支付消息
     * @param map
     * @return
     */
    PaymentInfo savePaymentInfo(Map<String, String> map);
}
