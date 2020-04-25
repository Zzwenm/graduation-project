package com.example.graduationproject.web.mapper;

import com.example.graduationproject.web.entity.Book;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author Zzwen
 * @date 2020/4/21 14:15
 */
@Mapper
public interface BookMapper {

    /**
     * 查询所有书籍
     *
     * @param queryName 查询内容
     * @return 书籍列表
     */
    List<Book> queryAll(@Param("queryName") String queryName);

    /**
     * 根据id列表查询
     *
     * @param topBookIdList id列表
     * @return 书籍列表
     */
    List<Book> selectByIdList(@Param("topBookIdList") List<String> topBookIdList);

    /**
     * 根据id查询
     * @param id id
     * @return 查询结果
     */
    Book findBookById(@Param("id") String id);
}
