package com.atguigu.gmall.common.config.threadPool;


import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@ConfigurationProperties(prefix = "app.thread-pool")
@Component
@Data
public class AppThreadPoolProperties {

    Integer core=4;
    Integer max=8;
    Integer queueSize=2000;
    Long keepAliveTime=300L;
}
