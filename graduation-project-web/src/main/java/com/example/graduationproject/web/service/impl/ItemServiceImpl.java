package com.example.graduationproject.web.service.impl;

import com.example.graduationproject.web.entity.Item;
import com.example.graduationproject.web.mapper.ItemMapper;
import com.example.graduationproject.web.service.ItemService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author Zzwen
 * @date 2020/4/17 13:50
 */
@Service
public class ItemServiceImpl implements ItemService {

    @Resource
    private ItemMapper itemMapper;

    @Override
    public List<String> getLimitItemList(int size) {

        return itemMapper.getLimitItemList(size);
    }

    @Override
    public List<Item> getItemListByIds(List<String> idList) {
        return itemMapper.getItemListByIds(idList);
    }
}
