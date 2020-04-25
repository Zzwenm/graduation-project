package com.example.graduationproject.web.entity;

import lombok.Data;

/**
 * @author Zzwen
 * @date 2020/4/21 9:38
 */
@Data
public class Book {
    /**
     * id
     */
    private Long id;
    /**
     * 书名
     */
    private String name;
    /**
     * 作者
     */
    private String author;
    /**
     * 价格
     */
    private String price;
    /**
     * 图片来源
     */
    private String src;
    /**
     * 书的信息
     */
    private String remark;
}
