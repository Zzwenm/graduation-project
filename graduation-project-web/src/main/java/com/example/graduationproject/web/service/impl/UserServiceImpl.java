package com.example.graduationproject.web.service.impl;

import com.example.graduationproject.web.dto.ResultDto;
import com.example.graduationproject.web.entity.User;
import com.example.graduationproject.web.mapper.UserMapper;
import com.example.graduationproject.web.service.UserService;
import com.example.graduationproject.web.util.JwtUtil;
import com.example.graduationproject.web.vo.UserVo;
import io.jsonwebtoken.Claims;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

/**
 * @author Zzwen
 * @date 2020/4/21 10:09
 */
@Service
public class UserServiceImpl implements UserService {

    @Resource
    private UserMapper userMapper;

    /**
     * 登录
     *
     * @param user 用户
     * @return 结果
     */
    @Override
    public ResultDto login(User user) {
        User findUser = userMapper.findUserByUsername(user.getUsername());
        if (findUser == null || !user.getPassword().equals(findUser.getPassword())) {
            return new ResultDto<>(40010, "用户名或密码错误！", user);
        }
        String token = JwtUtil.createJwt(findUser);
        return new ResultDto<>(20000, "登陆成功！", token);
    }

    /**
     * 退出登录
     *
     * @param user 用户
     * @return 结果
     */
    @Override
    public ResultDto logout(User user) {
        return new ResultDto<>(20000, "", "");
    }

    /**
     * 注册
     *
     * @param user 用户
     * @return 结果
     */
    @Override
    public ResultDto insertUser(User user) {
        User findUser = userMapper.findUserByUsername(user.getUsername());
        if (findUser != null) {
            return new ResultDto<>(40020, "用户名已存在！", user);
        }
        int i = userMapper.insertUser(user);
        if (i < 0) {
            return new ResultDto<>(40020, "添加用户失败！", user);
        }
        return new ResultDto<>(20000, "添加用户成功！", user);
    }

    /**
     * 获取用户信息
     *
     * @param request 请求体
     * @return 信息
     */
    @Override
    public ResultDto getInfo(HttpServletRequest request) {
        String token = request.getHeader("X-Token");
        Claims claims = JwtUtil.parseJwt(token);
        String id = (String) claims.get("id");
        String username = (String) claims.get("username");
        UserVo userVo = new UserVo();
        userVo.setId(id);
        userVo.setUsername(username);
        return new ResultDto<>(20000,"",userVo);
    }
}
