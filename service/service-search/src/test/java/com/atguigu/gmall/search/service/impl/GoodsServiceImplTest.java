package com.atguigu.gmall.search.service.impl;

import com.atguigu.gmall.model.vo.search.SearchParamVo;
import com.atguigu.gmall.search.service.GoodsService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

class GoodsServiceImplTest {

    @Autowired
    GoodsService goodsService;
    @Test
    void t1(){
        SearchParamVo vo = new SearchParamVo();

        goodsService.search(vo);

    }
    @Test
    void t2(){
        long l=1%100;
        System.out.println(l);
    }
    @Test
    void t3(){

    }

}