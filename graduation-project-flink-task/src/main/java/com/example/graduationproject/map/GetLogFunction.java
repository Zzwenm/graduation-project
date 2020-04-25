package com.example.graduationproject.map;

import com.example.graduationproject.domain.UserBehavior;
import com.example.graduationproject.util.LogToEntity;
import org.apache.flink.api.common.functions.MapFunction;

/**
 * 将kafka 的数据 转为 UserBehavior
 * @author Zzwen
 * @date 2020/4/14 17:31
 */
public class GetLogFunction implements MapFunction<String, UserBehavior> {
    @Override
    public UserBehavior map(String s) throws Exception {
        return LogToEntity.getEntity(s);
    }
}
