package com.example.graduationproject.web.service.impl;

import com.example.graduationproject.web.entity.ItemDetail;
import com.example.graduationproject.web.mapper.ItemDetailMapper;
import com.example.graduationproject.web.service.ItemDetailService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author Zzwen
 * @date 2020/4/17 13:51
 */
@Service
public class ItemDetailServiceImpl implements ItemDetailService {

    @Resource
    private ItemDetailMapper itemDetailMapper;

    @Override
    public List<ItemDetail> getItemDetailListByIds(List<String> idList) {
        return itemDetailMapper.getItemDetailListByIds(idList);
    }
}
