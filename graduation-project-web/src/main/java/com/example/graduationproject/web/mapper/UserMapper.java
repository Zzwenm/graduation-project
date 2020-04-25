package com.example.graduationproject.web.mapper;

import com.example.graduationproject.web.entity.User;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * @author Zzwen
 * @date 2020/4/21 10:10
 */
@Mapper
public interface UserMapper {

    /**
     * 根据用户名查询用户
     * @param username 用户名
     * @return 用户
     */
    User findUserByUsername(String username);

    /**
     * 添加用户
     * @param user 用户
     * @return 结果
     */
    int insertUser(@Param("user")User user);

}
