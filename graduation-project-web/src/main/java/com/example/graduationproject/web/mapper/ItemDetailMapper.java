package com.example.graduationproject.web.mapper;

import com.example.graduationproject.web.entity.ItemDetail;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 产品详细信息持久层
 *
 * @author Zzwen
 * @date 2020/4/17 13:50
 */
@Mapper
public interface ItemDetailMapper {
    /**
     * 根据id列表查询产品的详细信息
     *
     * @param idList id列表
     * @return
     */
    List<ItemDetail> getItemDetailListByIds(@Param("idList") List<String> idList);

}
