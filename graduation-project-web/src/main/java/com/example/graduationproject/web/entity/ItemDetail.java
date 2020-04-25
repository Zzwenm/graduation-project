package com.example.graduationproject.web.entity;

import lombok.Data;

/**
 * 产品详细信息
 *
 * @author Zzwen
 * @date 2020/4/17 13:28
 */
@Data
public class ItemDetail {

    private int id;
    private String picUrl;
    private String itemName;
    private String subName;
    private double martPrice;
    private String brandName;

    /**
     * id
     */
//    private Long id;
    /**
     * 产品id
     */
//    private Long itemId;
    /**
     * 描述信息
     */
//    private String remark;

}
