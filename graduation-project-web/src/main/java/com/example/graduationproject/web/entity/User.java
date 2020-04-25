package com.example.graduationproject.web.entity;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.NotNull;

/**
 * @author Zzwen
 * @date 2020/4/21 9:40
 */
@Data
public class User {
    /**
     * id
     */
    @Id
    private Long id;
    /**
     * 用户名
     */
    @NotNull(message = "用户名不能为空！")
    private String username;
    /**
     * 密码
     */
    @NotNull(message = "密码不能为空！")
    private String password;
}
