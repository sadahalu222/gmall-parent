package com.atguigu.gmall.common.config.threadPool;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * 配置线程池
 */
@Configuration
@EnableConfigurationProperties(AppThreadPoolProperties.class)
public class AppThreadPoolAutoConfiguration {

    @Autowired
    AppThreadPoolProperties appThreadPoolProperties;
    @Value("${spring.application.name}")
    String applicationName;

    @Bean
    public ThreadPoolExecutor coreExecutor(){
        /**
         * int corePoolSize, 核心线程池: cpu核心数    4
         * int maximumPoolSize, 最大线程数:          8
         * long keepAliveTime, 线程存活时间
         * TimeUnit unit,       时间单位
         * BlockingQueue<Runnable> workQueue, 阻塞队列 大小需要合理
         * ThreadFactory threadFactory,  线程工厂:自定义创建线程的方法
         * RejectedExecutionHandler handler 拒绝策略
         */
        ThreadPoolExecutor executor =
                new ThreadPoolExecutor(
                        appThreadPoolProperties.getCore(),
                        appThreadPoolProperties.getMax(),
                        appThreadPoolProperties.getKeepAliveTime(),
                        TimeUnit.SECONDS,
                        new LinkedBlockingQueue<>(appThreadPoolProperties.getQueueSize()),
                        new ThreadFactory() {
                            int i=0;
                            @Override
                            public Thread newThread(Runnable r) {
                                Thread thread = new Thread(r);
                                thread.setName(applicationName +"[core-thread-"+i++ +"]");
                                return thread;
                            }
                        },
                        //生产环境用CallerRuns  保证就算线程池满了 不能提交的任务 由当前线程已同步的方法执行
                        new ThreadPoolExecutor.CallerRunsPolicy()

                );
        return executor;
    }


















}
