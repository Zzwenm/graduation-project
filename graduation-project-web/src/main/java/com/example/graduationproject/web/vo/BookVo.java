package com.example.graduationproject.web.vo;

import com.example.graduationproject.web.entity.Book;
import lombok.Data;

import java.util.List;

/**
 * bookVo 展示实体类
 *
 * @author Zzwen
 * @date 2020/4/21 14:12
 */
@Data
public class BookVo {

    /**
     * id,用string，防止前台精度丢失
     */
    private String id;
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
