package com.example.graduationproject.web.service.impl;

import com.example.graduationproject.client.HbaseClient;
import com.example.graduationproject.web.client.RedisClient;
import com.example.graduationproject.web.dto.ItemDto;
import com.example.graduationproject.web.entity.Item;
import com.example.graduationproject.web.entity.ItemDetail;
import com.example.graduationproject.web.service.ItemCfCoefficientService;
import com.example.graduationproject.web.service.ItemDetailService;
import com.example.graduationproject.web.service.ItemService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * @author Zzwen
 * @date 2020/4/17 11:17
 */
@Slf4j
@Service
public class ItemCfCoefficientServiceImpl implements ItemCfCoefficientService {

    /**
     * 实时热度榜产品个数
     */
    private static final int TOP_SIZE = 10;
    /**
     * 相关产品推荐个数
     */
    private final static int PRODUCT_LIMIT = 3;

    /**
     * hbase产品推荐表名
     */
    private final static String TABLE_NAME = "px";

    @Resource
    private RedisClient redisClient;

    @Resource
    private ItemService itemService;

    @Resource
    private ItemDetailService itemDetailService;

    /**
     * 获取topN产品列表
     *
     * @return ItemDto列表
     */
    @Override
    public List<ItemDto> getTopNHotItemList() {
        // 获取top榜单产品id
        List<String> topList = getTopItems();
        int topSize = topList.size();
        // 拿到产品详情列表
        List<ItemDetail> itemDetails = itemDetailService.getItemDetailListByIds(topList);
        // 拿到产品基本信息列表
        List<Item> items = itemService.getItemListByIds(topList);
        //结合两者转成dto返回
        return fillProductDto(topList, items, itemDetails, topSize);
    }

    /**
     * 依据协同算法进行用户个性化推荐
     *
     * @return 产品列表
     */
    @Override
    public List<ItemDto> getCfItemList() {
        //获取榜单产品
        List<String> topItems = getTopItems();
        //从hbase获取推荐表中的推荐产品列表
        List<String> relatedItems = getRelatedItemFromHbase(topItems);
        //去重
        relatedItems = relatedItems.stream().distinct().collect(Collectors.toList());
        // 拿到产品详情表
        List<ItemDetail> itemDetails = itemDetailService.getItemDetailListByIds(relatedItems);
        // 拿到产品基本信息表
        List<Item> productEntities = itemService.getItemListByIds(relatedItems);
        int topSize = Math.min(Math.min(relatedItems.size(), itemDetails.size()), productEntities.size());
        return fillProductDto(relatedItems, productEntities, itemDetails, topSize);
    }

    /**
     * 从hbase获取用户感兴趣的产品
     *
     * @param topList hbase中的rowKey列表
     * @return 产品id列表
     */
    private List<String> getRelatedItemFromHbase(List<String> topList) {
        List<String> relatedList = new ArrayList<>();
        for (String s : topList) {
            //首先将top产品添加进结果集
            relatedList.add(s);
            List<Map.Entry> ps = new ArrayList<>();
            //获取的产品list是已经排好序的,根据得分排序
            try {
                ps = HbaseClient.getRow(TABLE_NAME, s);
                ps.sort(((o1, o2) -> -(new BigDecimal(o1.getValue().toString()).compareTo(new BigDecimal(o2.getValue().toString())))));
            } catch (Exception e) {
                log.warn("Hbase中没有产品【{}】记录", s);
            }
            if (CollectionUtils.isEmpty(ps)) {
                continue;
            }
            // 只保留最相关的3个产品
            int end = Math.min(ps.size(), PRODUCT_LIMIT);
            for (int i = 0; i < end; i++) {
                if (Objects.nonNull(ps.get(i))) {
                    relatedList.add((String) ps.get(i).getKey());
                }

            }
        }
        return relatedList;
    }


    /**
     * 结合产品的基本信息和详细信息成一个dto返回
     *
     * @param topList     产品的id列表
     * @param items       产品的基本信息
     * @param itemDetails 产品的详细信息
     * @return 结合后的dto列表
     */
    private List<ItemDto> fillProductDto(List<String> topList, List<Item> items, List<ItemDetail> itemDetails, int size) {
        List<ItemDto> itemDtoList = new ArrayList<>(size);
        for (int i = 0; i < size; i++) {
            String topId = topList.get(i);
            ItemDto itemDto = new ItemDto();
            itemDto.setScore(TOP_SIZE + i - 1);
            for (int j = 0; j < size; j++) {
                if (topId.equals(String.valueOf(itemDetails.get(j).getId()))) {
                    itemDto.setItemDetail(itemDetails.get(j));
                }
                if (topId.equals(String.valueOf(items.get(j).getProductId()))) {
                    itemDto.setItem(items.get(j));
                }
            }
            itemDtoList.add(itemDto);
        }
        return itemDtoList;
    }


    /**
     * 获取热度榜单，当榜单数据不足时，从数据库中取
     *
     * @return 榜单数据
     */
    private List<String> getTopItems() {
        List<String> topList = redisClient.getTopList(TOP_SIZE);
        topList = topList.stream().filter(Objects::nonNull).collect(Collectors.toList());
        if (topList.size() < TOP_SIZE) {
            // 尽量多的拿产品列表
            topList.addAll(itemService.getLimitItemList(100));
            //去重
            topList = topList.stream().distinct().collect(Collectors.toList());
            log.info("top : {}", topList);
        }
        return topList;
    }
}
