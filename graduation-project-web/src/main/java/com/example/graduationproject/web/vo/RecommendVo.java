package com.example.graduationproject.web.vo;

import com.example.graduationproject.web.entity.Book;
import lombok.Data;

import java.util.List;

/**
 * @author Zzwen
 * @date 2020/4/24 17:39
 */
@Data
public class RecommendVo {
    /**
     * 展示的书籍页面信息
     */
    private List<BookVo> books;
    /**
     * 推荐的书籍信息
     */
    private List<BookVo> recommendBooks;
}
