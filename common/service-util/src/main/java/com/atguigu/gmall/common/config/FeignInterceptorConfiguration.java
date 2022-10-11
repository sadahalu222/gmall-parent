package com.atguigu.gmall.common.config;

import com.atguigu.gmall.common.constant.SysRedisConst;
import feign.RequestInterceptor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

@Configuration
public class FeignInterceptorConfiguration {
    @Bean
    public RequestInterceptor interceptor(){

        return ( template)->{
            //String userId = CartController.map.get(Thread.currentThread());
            //  String userId = CartController.threadLocal.get();
            ServletRequestAttributes requestAttributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
            String userId = requestAttributes.getRequest().getHeader(SysRedisConst.USERID_HEADER);

            String tempId = requestAttributes.getRequest().getHeader(SysRedisConst.USERTEMPID_HEADER);
            template.header(SysRedisConst.USERID_HEADER, userId);
            template.header(SysRedisConst.USERTEMPID_HEADER ,tempId);

            //CartController.threadLocal.remove();
        };
    }
}
