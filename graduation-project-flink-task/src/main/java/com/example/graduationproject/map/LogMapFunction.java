package com.example.graduationproject.map;

import com.example.graduationproject.client.HbaseClient;
import com.example.graduationproject.domain.UserBehavior;
import com.example.graduationproject.util.LogToEntity;
import lombok.extern.slf4j.Slf4j;
import org.apache.flink.api.common.functions.MapFunction;

/**
 * 对流数据进行封装并存入HBase
 * 记录日志
 *
 * @author Zzwen
 * @date 2020/4/14 15:56
 */
@Slf4j
public class LogMapFunction implements MapFunction<String, UserBehavior> {

    public final String TABLE_NAME = "con";

    @Override
    public UserBehavior map(String s) throws Exception {
        log.info("flink input stream -> " + s);

        UserBehavior userBehavior = LogToEntity.getEntity(s);

        //存入hbase
        if (null != userBehavior) {

            StringBuilder rowKey = new StringBuilder();
            rowKey.append(userBehavior.getUserId()).append("_")
                    .append(userBehavior.getItemId()).append("_")
                    .append(userBehavior.getTimeTemp());

            HbaseClient.putData(TABLE_NAME, rowKey.toString(), "log", "userId", String.valueOf(userBehavior.getUserId()));
            HbaseClient.putData(TABLE_NAME, rowKey.toString(), "log", "itemId", String.valueOf(userBehavior.getItemId()));
            HbaseClient.putData(TABLE_NAME, rowKey.toString(), "log", "timeTemp", String.valueOf(userBehavior.getTimeTemp()));
            HbaseClient.putData(TABLE_NAME, rowKey.toString(), "log", "behavior", userBehavior.getBehavior());
        }
        return userBehavior;
    }

}
