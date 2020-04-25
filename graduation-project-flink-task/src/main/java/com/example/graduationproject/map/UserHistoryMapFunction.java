package com.example.graduationproject.map;

import com.example.graduationproject.client.HbaseClient;
import com.example.graduationproject.domain.UserBehavior;
import com.example.graduationproject.util.LogToEntity;
import lombok.extern.slf4j.Slf4j;
import org.apache.flink.api.common.functions.MapFunction;

/**
 * 存入hbase的item_history表中
 * 记录哪些用户对该产品进行浏览
 * 便于后续协同过滤推荐
 *
 * @author Zzwen
 * @date 2020/4/19 10:08
 */
@Slf4j
public class UserHistoryMapFunction implements MapFunction<String, String> {
    @Override
    public String map(String s) throws Exception {
        log.info("flink input stream -> " + s);
        UserBehavior userBehavior = LogToEntity.getEntity(s);
        if (null != userBehavior) {
            HbaseClient.increamColumn("item_history", String.valueOf(userBehavior.getItemId()), "userId", String.valueOf(userBehavior.getUserId()));
        }
        return "";
    }
}
