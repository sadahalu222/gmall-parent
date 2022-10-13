package com.atguigu.gmall.order.service.impl;

import java.math.BigDecimal;

import com.atguigu.gmall.common.auth.AuthUtils;
import com.atguigu.gmall.common.constant.SysRedisConst;
import com.atguigu.gmall.common.util.Jsons;
import com.atguigu.gmall.constant.MqConst;
import com.atguigu.gmall.model.enums.OrderStatus;
import com.atguigu.gmall.model.enums.ProcessStatus;
import com.atguigu.gmall.model.order.OrderDetail;
import com.atguigu.gmall.model.to.mq.OrderMsg;
import com.atguigu.gmall.order.service.OrderDetailService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.google.common.collect.Lists;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import com.atguigu.gmall.model.activity.CouponInfo;

import com.atguigu.gmall.model.order.OrderInfo;
import com.atguigu.gmall.model.vo.order.OrderSubmitVo;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import com.atguigu.gmall.order.service.OrderInfoService;
import com.atguigu.gmall.order.mapper.OrderInfoMapper;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 */
@Service
public class OrderInfoServiceImpl extends ServiceImpl<OrderInfoMapper, OrderInfo>
        implements OrderInfoService {

    @Autowired
    OrderInfoMapper orderInfoMapper;

    @Autowired
    OrderDetailService orderDetailService;
    @Autowired
    RabbitTemplate rabbitTemplate;

    @Transactional
    @Override
    public Long saveOrder(OrderSubmitVo submitVo, String tradeNo) {
        //1.准备订单id
        OrderInfo orderInfo = prepareOrderInfo(submitVo, tradeNo);
        //2.保存 orderInfo
        orderInfoMapper.insert(orderInfo);

        //2.保存 OrderDetail
        List<OrderDetail> list = prepareOrderDetail(submitVo, orderInfo);
        orderDetailService.saveBatch(list);

        //发送订单创建完成消息
        OrderMsg orderMsg = new OrderMsg(orderInfo.getId(), orderInfo.getUserId());

        rabbitTemplate.convertAndSend(MqConst.EXCHANGE_ORDER_EVNT,
                MqConst.RK_ORDER_CREATED, Jsons.toStr(orderMsg));

        //3.返回订单id
        return orderInfo.getId();
    }

    @Override
    public void changeOrderStatus(Long orderId, Long userId, ProcessStatus closed, List<ProcessStatus> expected) {

        String orderStatus = closed.getOrderStatus().name();
        String processStatus = closed.name();

        List<String> espects = expected.stream().map(status -> status.name()).collect(Collectors.toList());

        //修改订单
        orderInfoMapper.updateOrderStatus(orderId,userId,processStatus,orderStatus,espects);

    }

    @Override
    public OrderInfo getOrderInfoByOrderIdAndUserId(Long orderId, Long userId) {
        LambdaQueryWrapper<OrderInfo> wrapper = new LambdaQueryWrapper<OrderInfo>()
                .eq(OrderInfo::getUserId, userId)
                .eq(OrderInfo::getId, orderId);
        OrderInfo orderInfo = orderInfoMapper.selectOne(wrapper);
        return orderInfo;
    }

    @Override
    public OrderInfo getOrderInfoByOutTradeNoAndUserId(String outTradeNo, Long userId) {
        LambdaQueryWrapper<OrderInfo> wrapper = new LambdaQueryWrapper<OrderInfo>()
                .eq(OrderInfo::getUserId, userId)
                .eq(OrderInfo::getOutTradeNo, outTradeNo);
        OrderInfo orderInfo = orderInfoMapper.selectOne(wrapper);


        return orderInfo;
    }

    @Override
    public Long submitSeckillOrder(OrderInfo info) {
        //保存秒杀单
        orderInfoMapper.insert(info);
        Long id = info.getId();
        //2.保存明细
        List<OrderDetail> list = info.getOrderDetailList();

        list.stream().forEach(item->{
            item.setOrderId(id);
        });
        orderDetailService.saveBatch(list);


        return id;
    }

    /**
     * 订单明细
     *
     * @param submitVo
     * @param orderInfo
     * @return
     */
    private List<OrderDetail> prepareOrderDetail(OrderSubmitVo submitVo, OrderInfo orderInfo) {
        List<OrderDetail> detailList = submitVo.getOrderDetailList().stream()
                .map(vo -> {
                    OrderDetail detail = new OrderDetail();
                    //订单id
                    detail.setOrderId(orderInfo.getId());
                    //skuId
                    detail.setSkuId(vo.getSkuId());
                    //用户id
                    detail.setUserId(orderInfo.getUserId());

                    detail.setSkuName(vo.getSkuName());
                    detail.setImgUrl(vo.getImgUrl());
                    detail.setOrderPrice(vo.getOrderPrice());
                    detail.setSkuNum(vo.getSkuNum());
                    detail.setHasStock(vo.getHasStock());
                    detail.setCreateTime(new Date());
                    detail.setSplitTotalAmount(vo.getOrderPrice().multiply(new BigDecimal(vo.getSkuNum() + "")));
                    detail.setSplitActivityAmount(new BigDecimal("0"));
                    detail.setSplitCouponAmount(new BigDecimal("0"));
                    return detail;
                }).collect(Collectors.toList());
        return detailList;

    }


    private OrderInfo prepareOrderInfo(OrderSubmitVo submitVo, String tradeNo) {
        OrderInfo info = new OrderInfo();
        //收货人
        info.setConsignee(submitVo.getConsignee());
        info.setConsigneeTel(submitVo.getConsigneeTel());


        //用户id
        Long userId = AuthUtils.getCurrentAuthinfo().getUserId();
        info.setUserId(userId);

        //支付方式
        info.setPaymentWay(submitVo.getPaymentWay());
        //配送地址
        info.setDeliveryAddress(submitVo.getDeliveryAddress());
        //订单备注
        info.setOrderComment(submitVo.getOrderComment());

        //对外交易号
        info.setOutTradeNo(tradeNo);

        //交易体。 拿到这个订单中购买的第一个商品的名字，作为订单的体
        info.setTradeBody(submitVo.getOrderDetailList().get(0).getSkuName());
        //创建时间
        info.setCreateTime(new Date());

        //过期时间。订单多久没支付以后就过期。过期未支付：订单就会成为已关闭状态；
        //new Date() + 45min = x
        info.setExpireTime(new Date(System.currentTimeMillis() + 1000 * SysRedisConst.ORDER_CLOSE_TTL));

        //订单的处理状态
        info.setProcessStatus(ProcessStatus.UNPAID.name());
        //订单状态。默认未支付
        info.setOrderStatus(OrderStatus.UNPAID.name());


        //物流单编号，发货后才有
        info.setTrackingNo("");

        //父订单id（拆单场景）
        info.setParentOrderId(0L);

        //订单的图片
        info.setImgUrl(submitVo.getOrderDetailList().get(0).getImgUrl());


        //当前单被优惠活动减掉的金额
        info.setActivityReduceAmount(new BigDecimal("0"));
        //当前单被优惠券减掉的金额
        info.setCouponAmount(new BigDecimal("0"));

        //计算总价
        BigDecimal totalAmount = submitVo.getOrderDetailList()
                .stream()
                .map(o -> o.getOrderPrice()
                        .multiply(new BigDecimal(o.getSkuNum() + "")))
                .reduce((o1, o2) -> o1.add(o2))
                .get();
        //订单原始总额
        info.setOriginalTotalAmount(totalAmount);

        //订单总额 = OriginalTotalAmount - ActivityReduceAmount - CouponAmount
        info.setTotalAmount(totalAmount);

        //可退款的最后时间
        info.setRefundableTime(new Date(System.currentTimeMillis() + SysRedisConst.ORDER_REFUND_TTL * 1000));

        //运费。第三方物流平台，动态计算运费
        info.setFeightFee(new BigDecimal("0"));

        info.setOperateTime(new Date());


        return info;
    }
}




