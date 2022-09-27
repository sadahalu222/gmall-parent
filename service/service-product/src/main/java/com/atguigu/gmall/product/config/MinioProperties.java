package com.atguigu.gmall.product.config;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@Data
@ConfigurationProperties(prefix = "app.minio")
public class MinioProperties {


    private String endpoint;
    private String ak;
    private  String sk;
    private String bucketName;
}
