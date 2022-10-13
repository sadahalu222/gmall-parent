package com.atguigu.gmall.order.listener;

import com.atguigu.gmall.common.constant.SysRedisConst;
import com.atguigu.gmall.common.util.Jsons;
import com.atguigu.gmall.constant.MqConst;
import com.atguigu.gmall.model.to.mq.OrderMsg;
import com.atguigu.gmall.order.biz.OrderBizService;
import com.rabbitmq.client.Channel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * 订单关闭监听器
 */
@Slf4j
@Component
public class OrderCloseListener {

    @Autowired
    StringRedisTemplate redisTemplate;

    @Autowired
    OrderBizService orderBizService;

    @RabbitListener(queues = MqConst.QUEUE_ORDER_DEAD)
    public void orderClose(Message message, Channel channel) throws IOException {
        long tag = message.getMessageProperties().getDeliveryTag();
        //1、拿到订单消息
        OrderMsg orderMsg = Jsons.toObj(message, OrderMsg.class);


        try {
            //2.进行订单关闭
            log.info("监听到超时订单{},正在关闭",orderMsg);
            orderBizService.closeOrder(orderMsg.getOrderId(),orderMsg.getUserId());

            channel.basicAck(tag, false);
        } catch (IOException e) {
           log.error("订单关闭业务失败.消息{},失败原因{}",orderMsg,e);
            Long aLong = redisTemplate.opsForValue().increment(SysRedisConst.MQ_RETRY +"order:"+ orderMsg.getOrderId());
            if (aLong<=10){
                channel.basicNack(tag, false,true); //重试 消息入队
            }else {
                channel.basicNack(tag, false,false); //消息不入队
                redisTemplate.delete(SysRedisConst.MQ_RETRY +"order:"+ orderMsg.getOrderId());
            }

        }

    }

}