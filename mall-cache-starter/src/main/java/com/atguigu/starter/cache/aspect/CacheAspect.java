package com.atguigu.starter.cache.aspect;


import com.atguigu.starter.cache.annotation.GmallCache;
import com.atguigu.starter.cache.constant.SysRedisConst;
import com.atguigu.starter.cache.service.CacheOpsService;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.expression.Expression;
import org.springframework.expression.ExpressionParser;
import org.springframework.expression.ParserContext;
import org.springframework.expression.common.TemplateParserContext;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.expression.spel.support.StandardEvaluationContext;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.lang.reflect.Method;
import java.lang.reflect.Type;

@Component
@Aspect
public class CacheAspect {
    @Autowired
    CacheOpsService cacheOpsService;
    //创建一个表达式解析器 这个线程安全的
    ExpressionParser parser = new SpelExpressionParser();
    ParserContext context=new TemplateParserContext();

    @Around("@annotation(com.atguigu.starter.cache.annotation.GmallCache)")
    public Object around(ProceedingJoinPoint joinPoint) throws Throwable {

        Object result = null;
        //完成 动态获取key
        String cacheKey = determinCacheKey(joinPoint);
        //1.先查缓存
        //获取目标方法的精确返回值类型 动态
        Type returnType = getMethodGenericReturnType(joinPoint);
        Object cacheData = cacheOpsService.getCacheData(cacheKey,
                returnType);
        //2.缓存
        if (cacheData == null) {
            //3.准备回源
            //4.先问布隆  有些场景不用布隆 比如:三级分类(就是一个大数据)
   //         boolean contains = cacheOpsService.bloomContains(arg);
            String bloomName = determinBloomName(joinPoint);

            if(!StringUtils.isEmpty(bloomName)){
                //指定开启了布隆
                Object bloomValue = determinBloomValue(joinPoint);
                boolean contains = cacheOpsService.bloomContains(bloomName,bloomValue);
                if (!contains) {
                    return null;
                }
            }

            //5布隆说有 准备回源 有击穿风险
            boolean lock = false;
            String lockName="";
            try {
                //不同场景用自己的锁
                 lockName= determinLockName(joinPoint);

                lock = cacheOpsService.tryLock(lockName);
                if (lock) {

                    //6.获取到锁 开始回源
                    result = joinPoint.proceed(joinPoint.getArgs());
                    //7.调用成功 重新保存到缓存
                    //获取过期时间
                    Long ttl =determinTtl(joinPoint);
                    cacheOpsService.saveData(cacheKey, result,ttl);
                    return result;
                } else {
                    Thread.sleep(1000L);
                    //return cacheOpsService.getCacheData(cacheKey, SkuDetailTo.class);

                    return cacheOpsService.getCacheData(cacheKey, returnType);
                }
            } finally {
                if (lock) {
                    cacheOpsService.unlock(lockName);
                }
            }

        }

        // x .缓存中有 直接返回
        return cacheData;


    }

    private Long determinTtl(ProceedingJoinPoint joinPoint) {
        //1.拿到目标方法上的@GmallCache注解
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        Method method = signature.getMethod();
        //2.拿到注解
        GmallCache gmallCache = method.getDeclaredAnnotation(GmallCache.class);
        //3.拿到布隆表达式的值
        long ttl = gmallCache.ttl();
        return ttl;

    }

    /**
     * 根据表达式获取锁定名字
     * @param joinPoint
     * @return
     */
    private String determinLockName(ProceedingJoinPoint joinPoint) {
        //1.拿到目标方法上的@GmallCache注解
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        Method method = signature.getMethod();
        //2.拿到注解
        GmallCache gmallCache = method.getDeclaredAnnotation(GmallCache.class);
        //3.拿到布隆表达式的值

        String lockName = gmallCache.lockName();

        if (StringUtils.isEmpty(lockName)){
            //没指定锁用方法基本的锁
            return SysRedisConst.LOCK_PREFIX +method.getName();
        }

        String expression = evaluationExpression(lockName, joinPoint, String.class);

        return expression;


    }

    /**
     * 根据布隆过滤器值表达式计算出布隆需要判定的对象值
     * @param joinPoint
     * @return
     */
    private Object determinBloomValue(ProceedingJoinPoint joinPoint) {
        //1.拿到目标方法上的@GmallCache注解
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        Method method = signature.getMethod();
        //2.拿到注解
        GmallCache gmallCache = method.getDeclaredAnnotation(GmallCache.class);
        //3.拿到布隆表达式的值
        String bloomValue = gmallCache.bloomValue();
        Object expression = evaluationExpression(bloomValue, joinPoint, Object.class);

        return expression;

    }

    /**
     * 获取布隆过滤器的名字
     * @param joinPoint
     * @return
     */
    private String determinBloomName(ProceedingJoinPoint joinPoint) {
        //1.拿到目标方法上的@GmallCache注解
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        Method method = signature.getMethod();
        //2.拿到注解
        GmallCache gmallCache = method.getDeclaredAnnotation(GmallCache.class);

        String bloomName = gmallCache.bloomName();
        return bloomName;

    }

    /**
     * 获取目标方法的精确返回值类型
     * @param joinPoint
     * @return
     */
    private Type getMethodGenericReturnType(ProceedingJoinPoint joinPoint) {
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        Method method = signature.getMethod();
        Type type = method.getGenericReturnType();
        return type;

    }

    /**
     * 根据当前的执行信息.确定缓存用什么key
     * @param joinPoint
     * @return
     */
    private String determinCacheKey(ProceedingJoinPoint joinPoint) {
        //1.拿到目标方法上的@GmallCache注解
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        Method method = signature.getMethod();
        //2.拿到注解
        GmallCache gmallCache = method.getDeclaredAnnotation(GmallCache.class);

        String expression = gmallCache.cacheKey();

        //3.根据表达式计算缓存键
        String cacheKey = evaluationExpression(expression,joinPoint,String.class);

        return cacheKey;

    }

    private <T>T evaluationExpression(String expression,
                                        ProceedingJoinPoint joinPoint,
                                        Class<T> clz) {

        //1.得到表达式
        Expression exp = parser.parseExpression(expression, context);

        //2.sku:info:#{#params[0]}
        StandardEvaluationContext evaluationContext = new StandardEvaluationContext();
        //3.取出所有参数 绑定到上下文

        Object[] args = joinPoint.getArgs();
        evaluationContext.setVariable("params",args);

        T value = exp.getValue(evaluationContext, clz);
        return value;


    }


}
