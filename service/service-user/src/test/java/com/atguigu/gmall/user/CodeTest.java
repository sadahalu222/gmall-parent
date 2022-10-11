package com.atguigu.gmall.user;

import com.atguigu.gmall.common.util.MD5;
import com.atguigu.gmall.model.user.UserInfo;
import com.atguigu.gmall.user.mapper.UserInfoMapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class CodeTest {
    @Autowired
    UserInfoMapper userInfoMapper;

    @Test
    void t1() {

        UserInfo userInfo = userInfoMapper.selectById(3);
        System.out.println(userInfo);
        String passwd = userInfo.getPasswd();
        System.out.println(passwd);
        userInfo.setPasswd(MD5.encrypt("123456"));
        userInfoMapper.updateById(userInfo);

    }

}
