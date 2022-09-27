package com.atguigu.gmall.product;

import com.baomidou.mybatisplus.extension.plugins.MybatisPlusInterceptor;
import com.baomidou.mybatisplus.extension.plugins.PaginationInterceptor;
import com.baomidou.mybatisplus.extension.plugins.inner.PaginationInnerInterceptor;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.cloud.client.SpringCloudApplication;
import org.springframework.context.annotation.Bean;

@MapperScan(basePackages = "com.atguigu.gmall.product.mapper")
@SpringCloudApplication
public class ProductMainApplication {
    public static void main(String[] args) {
        SpringApplication.run(ProductMainApplication.class, args);

    }



    @Bean
    public MybatisPlusInterceptor paginationInnerInterceptor(){
        //插件主题
        MybatisPlusInterceptor interceptor = new MybatisPlusInterceptor();

        //加入内部的小插件
        PaginationInnerInterceptor paginationInnerInterceptor = new PaginationInnerInterceptor();
        paginationInnerInterceptor.setOverflow(true); //页码溢出后 默认访问最后一页

        //分页插件
        interceptor.addInnerInterceptor(paginationInnerInterceptor);

        return interceptor;
    }
 /*@Bean 过期了
 public PaginationInterceptor paginationInterceptor() {
     return new PaginationInterceptor();
 }*/


}
