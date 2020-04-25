package com.example.graduationproject.web.service;

import com.example.graduationproject.web.entity.Item;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 产品逻辑层
 *
 * @author Zzwen
 * @date 2020/4/17 11:40
 */
public interface ItemService {


    /**
     * 获取指定条数的产品id
     * @param size 数量
     * @return id列表
     */
    List<String> getLimitItemList(int size);

    /**
     * 根据id获取产品列表
     * @param idList id列表
     * @return 产品列表
     */
    List<Item> getItemListByIds(List<String> idList);
}
