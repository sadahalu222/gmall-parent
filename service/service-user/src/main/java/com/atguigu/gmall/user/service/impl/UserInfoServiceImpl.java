package com.atguigu.gmall.user.service.impl;

import com.atguigu.gmall.common.constant.SysRedisConst;
import com.atguigu.gmall.common.util.Jsons;
import com.atguigu.gmall.common.util.MD5;
import com.atguigu.gmall.model.user.UserInfo;
import com.atguigu.gmall.model.vo.user.LoginSuccessVo;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import com.atguigu.gmall.user.service.UserInfoService;
import com.atguigu.gmall.user.mapper.UserInfoMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.util.UUID;
import java.util.concurrent.TimeUnit;

/**
 *
 */
@Service
public class UserInfoServiceImpl extends ServiceImpl<UserInfoMapper, UserInfo>
    implements UserInfoService{

    @Autowired
    UserInfoMapper userInfoMapper;
    @Autowired
    StringRedisTemplate redisTemplate;

    @Override
    public LoginSuccessVo login(UserInfo info) {
        //1.查数据库
        LambdaQueryWrapper<UserInfo> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(UserInfo::getLoginName,info.getLoginName())
                .eq(UserInfo::getPasswd, MD5.encrypt(info.getPasswd()));
        UserInfo userInfo = userInfoMapper.selectOne(wrapper);

        //2.登入成功
        LoginSuccessVo vo = new LoginSuccessVo();
        if (userInfo!=null){
            //生成令牌
            String token = UUID.randomUUID().toString().replace("-", "");
            vo.setToken(token);

            //redis 绑定信息
            String userStr = Jsons.toStr(userInfo);
            redisTemplate.opsForValue().set(SysRedisConst.LOGIN_USER+token, userStr,7, TimeUnit.DAYS);

            vo.setNickName(userInfo.getNickName());
            return vo;
        }

        return null;
    }

    @Override
    public void logout(String token) {
        redisTemplate.delete(SysRedisConst.LOGIN_USER+token);
    }
}




