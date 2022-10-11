package com.atguigu.gmall.search;

import org.springframework.boot.SpringApplication;
import org.springframework.cloud.client.SpringCloudApplication;
import org.springframework.data.elasticsearch.repository.config.EnableElasticsearchRepositories;

/**
 * 普通的crud 写Bean 写接口
 * 复杂的crud ElasticsearchRestTemplate自己调用相关的方法构造复杂的DSL完成功能
 */
@EnableElasticsearchRepositories //开启es的自动仓库功能
@SpringCloudApplication
public class SearchMainApplication {
    public static void main(String[] args) {
        SpringApplication.run(SearchMainApplication.class, args);
    }

}
