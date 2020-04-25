package com.example.graduationproject.domain;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * 窗口聚合结果样例类
 *
 * @author Zzwen
 * @date 2020/4/14 14:36
 */
@Data
@AllArgsConstructor
public class ItemViewCount {
    /**
     * 产品id
     */
    Long itemId;
    /**
     * flink窗口结算时间
     */
    Long windowEnd;
    /**
     * 统计出现的次数
     */
    Long count;
    /**
     * 排名
     */
    String rank;
}
