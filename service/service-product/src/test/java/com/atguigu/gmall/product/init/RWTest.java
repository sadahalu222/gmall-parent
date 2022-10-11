package com.atguigu.gmall.product.init;

import com.atguigu.gmall.model.product.BaseTrademark;
import com.atguigu.gmall.product.mapper.BaseTrademarkMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class RWTest {

    @Autowired
    BaseTrademarkMapper baseTrademarkMapper;

    @Test
    void t1() {
        BaseTrademark baseTrademark = baseTrademarkMapper.selectById(4L);
        System.out.println("baseTrademark = " + baseTrademark);
    }





}
