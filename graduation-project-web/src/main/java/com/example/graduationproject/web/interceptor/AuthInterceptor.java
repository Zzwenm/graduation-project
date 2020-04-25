package com.example.graduationproject.web.interceptor;

import com.example.graduationproject.web.dto.ResultDto;
import com.example.graduationproject.web.service.UserService;
import com.example.graduationproject.web.util.JwtUtil;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.Claims;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;

/**
 * 拦截器，对身份进行验证
 * @author Zzwen
 * @date 2020/4/4 17:25
 */
@Slf4j
@Component
public class AuthInterceptor implements HandlerInterceptor {

    /**
     * 登陆路径
     */
    private final String LOGIN_URL = "/user/doLogin";
    /**
     * 注册路径
     */
    private final String REGISTER_URL = "/user/doInsert";
    /**
     * 登出路径
     */
    private final String LOGOUT_URL = "/user/doLogout";

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String url = request.getRequestURI();
        log.info("***************************");
        log.info("url : " + url);
        log.info("***************************");
        String token = request.getHeader("X-Token");
        //登入、登出 放行
        if (LOGIN_URL.equals(url) ||REGISTER_URL.equals(url) || LOGOUT_URL.equals(url)) {
            return true;
        }
        //token失效，直接返回身份过期
        try{
            JwtUtil.parseJwt(token);
        }catch (Exception e){
            response.setHeader("Content-type","application/json; charset=UTF-8");
            OutputStream outputStream=response.getOutputStream();
            ResultDto resultDto = new ResultDto<>(500000,"身份认证失败！",null);
            outputStream.write(new ObjectMapper().writeValueAsString(resultDto).getBytes(StandardCharsets.UTF_8));
            return false;
        }
        return true;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {

    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {

    }
}
