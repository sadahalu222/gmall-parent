package com.atguigu.starter.cache.annotation;

import java.lang.annotation.*;

/**
 * 缓存注解
 */
@Target({ElementType.TYPE, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Inherited
public @interface GmallCache {
    String cacheKey() default ""; //就是cacheKey
    String bloomName() default "";//如果指定了布隆过滤器的名字 就用
    String bloomValue() default "";//如果指定了布隆过滤器如果需要判断的话 用什么表达式计算出的值进行判定


    String lockName() default "";//传入精确的就要精确的

    long ttl() default 60*30L;

}
