package com.example.graduationproject.domain;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * 用户行为
 * @author Zzwen
 * @date 2020/4/14 14:34
 */
@Data
@AllArgsConstructor
public class UserBehavior {
    private Long userId;
    private Long itemId;
    /**
     * 用户行为，
     * 1、浏览
     * 2、收藏
     * 3、分享
     */
    private String behavior;
    private Long timeTemp;
}
