package com.atguigu.gmall.item.config;

import net.minidev.json.JSONUtil;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.w3c.dom.ls.LSOutput;

import java.util.concurrent.ThreadPoolExecutor;

import static org.junit.jupiter.api.Assertions.*;
@SpringBootTest
class AppThreadPoolTest {
    @Autowired
    ThreadPoolExecutor executor;
    @Test
    void testPool(){
        for (int i = 0; i <100 ; i++) {
            executor.submit(()->{
                System.out.println(Thread.currentThread().getName()+"sdaf");
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            });

        }

        try {
            Thread.sleep(10000000000000000L);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

}