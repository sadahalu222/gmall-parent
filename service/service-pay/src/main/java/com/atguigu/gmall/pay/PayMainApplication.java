package com.atguigu.gmall.pay;

import com.atguigu.gmall.common.annotation.EnableAutoException;
import com.atguigu.gmall.common.annotation.EnableAutoFeignConfiguration;
import org.springframework.boot.SpringApplication;
import org.springframework.cloud.client.SpringCloudApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@EnableAutoException
@EnableAutoFeignConfiguration
@EnableFeignClients({
        "com.atguigu.gmall.feign.order"
})
@SpringCloudApplication
public class PayMainApplication {
    public static void main(String[] args) {
        SpringApplication.run(PayMainApplication.class, args);
    }
}
