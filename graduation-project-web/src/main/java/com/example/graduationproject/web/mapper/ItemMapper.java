package com.example.graduationproject.web.mapper;

import com.example.graduationproject.web.entity.Item;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 产品持久层
 *
 * @author Zzwen
 * @date 2020/4/17 13:48
 */
@Mapper
public interface ItemMapper {

    /**
     * 获取指定条数的产品id
     *
     * @param size 数量
     * @return id列表
     */
    List<String> getLimitItemList(@Param("size") int size);

    /**
     * 根据id获取产品列表
     *
     * @param idList id列表
     * @return 产品列表
     */
    List<Item> getItemListByIds(@Param("idList") List<String> idList);

}
