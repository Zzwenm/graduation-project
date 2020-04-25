package com.example.graduationproject.web.service;

import com.example.graduationproject.web.dto.ResultDto;

/**
 * @author Zzwen
 * @date 2020/4/21 14:14
 */
public interface BookService {
    /**
     * 获取书籍信息 和 榜单信息
     * @param page 查询页数
     * @param queryName 查询内容
     * @return 结果
     */
    ResultDto fetchData(String page,String queryName);

    /**
     * 获取书籍的详细信息以及推荐书籍
     * @param bookId 书籍id
     * @param userId 用户id
     * @return 结果
     */
    ResultDto getBookDetail(String bookId,String userId);
}
