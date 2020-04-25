package com.example.graduationproject.web.entity;

import lombok.Data;

/**
 * 用户收藏书籍关联表
 *
 * @author Zzwen
 * @date 2020/4/21 9:41
 */
@Data
public class UserBook {
    /**
     * id
     */
    private Long id;
    /**
     * 用户id
     */
    private Long userId;
    /**
     * 书籍id
     */
    private Long bookId;
}
