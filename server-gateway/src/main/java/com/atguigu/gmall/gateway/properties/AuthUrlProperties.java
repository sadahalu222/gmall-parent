package com.atguigu.gmall.gateway.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.List;

@ConfigurationProperties(prefix = "app.auth")
@Component
@Data
public class AuthUrlProperties {
    List<String> noAuthUrl; //无需登入即可访问的路径
    List<String> loginAuthUrl; //登入才可访问的路径
    String loginPage; //登入页地址
    List<String>  denyUrl; //内部接口 紧张外部直接访问
}
