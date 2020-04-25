package com.example.graduationproject.web.controller;

import com.example.graduationproject.web.dto.ResultDto;
import com.example.graduationproject.web.entity.User;
import com.example.graduationproject.web.service.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Zzwen
 * @date 2020/4/21 9:37
 */
@Controller
@ResponseBody
@RequestMapping("/user")
public class UserController {

    @Resource
    private UserService userService;

    @PostMapping("/doLogin")
    public ResultDto login(@RequestBody @Validated User user) {
        return userService.login(user);
    }

    @RequestMapping("/doLogout")
    public ResultDto logout(User user) {
        return userService.logout(user);
    }

    @PostMapping("/doInsert")
    public ResultDto insert(@RequestBody User user) {
        return userService.insertUser(user);
    }

    @RequestMapping("/doInfo")
    public ResultDto info(HttpServletRequest request){
        return userService.getInfo(request);
    }

    @RequestMapping("/testException1")
    public ResultDto exceptionTest1(){
        int i = 1/0;
        return new ResultDto<>(20000,"",null);
    }

    @RequestMapping("/testException2")
    public ResultDto exceptionTest2(){
        int[] a = new int[2];
        a[3] = 1;
        return new ResultDto<>(20000,"",null);
    }

}
