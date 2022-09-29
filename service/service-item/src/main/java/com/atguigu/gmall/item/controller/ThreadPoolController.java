package com.atguigu.gmall.item.controller;

import com.atguigu.gmall.common.result.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.ThreadPoolExecutor;

@RestController
public class ThreadPoolController {
    @Autowired
    ThreadPoolExecutor executor;

    @GetMapping("close/pool")
    public Result closePool(){
        executor.shutdown();//关闭线程池
        return Result.ok();
    }
}
