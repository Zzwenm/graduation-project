package com.example.graduationproject.web.service;

import com.example.graduationproject.web.dto.ItemDto;
import org.springframework.stereotype.Service;

import java.util.List;


/**
 * 产品相关系数逻辑层
 *
 * @author Zzwen
 * @date 2020/4/17 9:52
 */
public interface ItemCfCoefficientService {


    /**
     * 获取实时热度topN产品列表
     *
     * @return 产品列表
     */
    List<ItemDto> getTopNHotItemList();

    /**
     * 依据协同算法进行用户个性化推荐
     * @return 产品列表
     */
    List<ItemDto> getCfItemList();

}
