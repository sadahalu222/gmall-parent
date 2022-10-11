package com.atguigu.gmall.user.service;


import com.atguigu.gmall.model.user.UserInfo;
import com.atguigu.gmall.model.vo.user.LoginSuccessVo;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 *
 */
public interface UserInfoService extends IService<UserInfo> {
    /**
     * 用户登入
     * @param
     * @return
     */
    LoginSuccessVo login(UserInfo info);

    /**
     * 用户退出登入
     * @param token
     */
    void logout(String token);
}
