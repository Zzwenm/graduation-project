package com.example.graduationproject.util;

import com.example.graduationproject.domain.UserBehavior;
import lombok.extern.slf4j.Slf4j;

/**
 * @author Zzwen
 * @date 2020/4/14 17:26
 */
@Slf4j
public class LogToEntity {
    public static UserBehavior getEntity(String s) {
        //分割数据
        String[] data = s.split(",");
        if(data.length<3){
            log.info("数据不正确！");
            return null;
        }
        //封装成UserBehavior
        Long userId = Long.valueOf(data[0].trim());
        Long itemId = Long.valueOf(data[1].trim());
        String behavior = data[2].trim();
        Long timeTemp = Long.valueOf(data[3].trim());
        return new UserBehavior(userId, itemId, behavior, timeTemp);
    }
}
