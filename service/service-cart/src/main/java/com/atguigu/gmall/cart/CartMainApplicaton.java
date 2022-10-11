package com.atguigu.gmall.cart;

import com.atguigu.gmall.common.annotation.EnableAutoException;
import com.atguigu.gmall.common.annotation.EnableAutoFeignConfiguration;
import com.atguigu.gmall.common.annotation.EnableThreadPool;
import org.springframework.boot.SpringApplication;
import org.springframework.cloud.client.SpringCloudApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;
@EnableAutoException
@EnableFeignClients("com.atguigu.gmall.feign.product")
@SpringCloudApplication
@EnableThreadPool
@EnableAutoFeignConfiguration
public class CartMainApplicaton {
    public static void main(String[] args) {
        SpringApplication.run(CartMainApplicaton.class, args);
    }
}
