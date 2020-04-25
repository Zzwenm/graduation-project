package com.example.graduationproject.web.dto;

import com.example.graduationproject.web.entity.Item;
import com.example.graduationproject.web.entity.ItemDetail;
import lombok.Data;

/**
 * 产品dto,包括产品、产品的详情信息
 *
 * @author Zzwen
 * @date 2020/4/17 9:55
 */
@Data
public class ItemDto {
    /**
     * 产品
     */
    private Item item;
    /**
     * 产品详细信息
     */
    private ItemDetail itemDetail;
    /**
     * 评分
     */
    private double score;
}
