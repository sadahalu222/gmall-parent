package com.atguigu.gmall.web.controller;

import com.atguigu.gmall.common.constant.SysRedisConst;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HelloController {

    @GetMapping({"/order/haha","/test/haha"})
    public String myTest(@RequestHeader(SysRedisConst.USERID_HEADER)String uid){

        return "ok"+uid;
    }
}
