package com.atguigu.gmall.common.annotation;

import com.atguigu.gmall.common.config.threadPool.AppThreadPoolAutoConfiguration;
import org.springframework.context.annotation.Import;

import java.lang.annotation.*;

@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE})
@Documented
@Import(AppThreadPoolAutoConfiguration.class)
public @interface EnableThreadPool {
}
