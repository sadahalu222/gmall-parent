package com.atguigu.gmall.order;

import com.atguigu.gmall.annotation.EnableAppRabbit;
import com.atguigu.gmall.common.annotation.EnableAutoException;
import com.atguigu.gmall.common.annotation.EnableAutoFeignConfiguration;
import com.atguigu.gmall.feign.ware.callback.WareFeignClientCallBack;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.amqp.rabbit.annotation.EnableRabbit;
import org.springframework.boot.SpringApplication;
import org.springframework.cloud.client.SpringCloudApplication;
import org.springframework.cloud.client.circuitbreaker.EnableCircuitBreaker;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Import;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@Import({WareFeignClientCallBack.class})
@EnableAppRabbit
@EnableTransactionManagement
@EnableAutoException
@EnableAutoFeignConfiguration
@SpringCloudApplication
@MapperScan(basePackages = "com.atguigu.gmall.order.mapper")
@EnableFeignClients({"com.atguigu.gmall.feign.cart","com.atguigu.gmall.feign.user"
        ,"com.atguigu.gmall.feign.product","com.atguigu.gmall.feign.ware"
})
public class OrderApplication {
    public static void main(String[] args) {
        SpringApplication.run(OrderApplication.class, args);
    }
}
