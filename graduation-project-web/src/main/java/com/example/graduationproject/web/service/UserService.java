package com.example.graduationproject.web.service;

import com.example.graduationproject.web.dto.ResultDto;
import com.example.graduationproject.web.entity.User;

import javax.servlet.http.HttpServletRequest;

/**
 * @author Zzwen
 * @date 2020/4/21 9:36
 */
public interface UserService {
    /**
     * 登录
     * @param user 用户
     * @return 登陆结果
     */
    ResultDto login(User user);

    /**
     * 退出登录
     * @param user 用户
     * @return 退出登陆结果
     */
    ResultDto logout(User user);

    /**
     * 注册
     * @param user 用户
     * @return 注册结果
     */
    ResultDto insertUser(User user);

    /**
     * 获取用户信息
     *
     * @param request 请求体
     * @return 信息
     */
    ResultDto getInfo(HttpServletRequest request);
}
