package com.example.graduationproject.web.service;

import com.example.graduationproject.web.entity.ItemDetail;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author Zzwen
 * @date 2020/4/17 13:35
 */
public interface ItemDetailService {
    /**
     * 根据id获取产品的详细信息
     * @param idList id列表
     * @return 产品详细信息列表
     */
    List<ItemDetail> getItemDetailListByIds(List<String> idList);

}
