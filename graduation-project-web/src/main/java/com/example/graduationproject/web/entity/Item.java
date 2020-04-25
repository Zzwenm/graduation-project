package com.example.graduationproject.web.entity;

import lombok.Data;

/**
 * 产品实体类
 * @author Zzwen
 * @date 2020/4/17 11:18
 */
@Data
public class Item {
    private int productId;
    private String productName;
    private String color;
    private String diameter;
    private String style;
    private String material;
    private String country;
//    private Long id;
//    private String name;
}
