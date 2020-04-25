package com.example.graduationproject.scheduler;

import com.example.graduationproject.client.HbaseClient;

import java.io.IOException;
import java.util.List;
import java.util.Map;

/**
 * 每个item的相关度计算
 * 基于协同过滤的方法
 *
 * @author Zzwen
 * @date 2020/4/16 15:21
 */
public class ItemCfCalculate {
    /**
     * 获取当前产品的相关系数
     *
     * @param itemId 产品id
     * @param others 其他产品的id
     */
    void getItemCoefficient(String itemId, List<String> others) throws Exception {

        for (String other : others) {
            if (itemId.equals(other)) {
                continue;
            }
            //计算两个产品的相似度
            Double score = getTwoItemCfCoefficient(itemId, other);
            //将结果存入hbase，rowKey=itemId，便于后续取出与该产品相似的产品
            HbaseClient.putData("item_similar", itemId, "similar_id", other, score.toString());
        }
    }

    /**
     * 计算两个产品之间的相关系数
     * 计算公式：(a&b)/sqrt(a||b)
     * （同时喜欢a和b的人数）/sqrt(喜欢a或b的人数)
     *
     * @param itemAid 产品id
     * @param itemBid 另一个产品id
     * @return 相关系数
     * @throws IOException 异常
     */
    private Double getTwoItemCfCoefficient(String itemAid, String itemBid) throws IOException {
        //喜欢a的用户列表
        List<Map.Entry> p1 = HbaseClient.getRow("item_history", itemAid);
        //喜欢b的用户列表
        List<Map.Entry> p2 = HbaseClient.getRow("item_history", itemBid);

        int n = p1.size();
        int m = p2.size();
        int sum = 0;
        double total = Math.sqrt(n * m);
        if (total == 0) {
            return 0.0;
        }
        //统计同时喜欢a和b的人数和
        for (Map.Entry entry : p1) {
            String key = (String) entry.getKey();
            for (Map.Entry p : p2) {
                if (key.equals(p.getKey())) {
                    sum++;
                }
            }
        }
        return sum / total;

    }
}
