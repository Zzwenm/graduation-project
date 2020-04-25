package com.example.graduationproject.map;

import com.example.graduationproject.domain.UserBehavior;
import com.example.graduationproject.util.LogToEntity;
import lombok.extern.slf4j.Slf4j;
import org.apache.flink.api.common.functions.MapFunction;

/**
 * @author Zzwen
 * @date 2020/4/14 17:24
 */
@Slf4j
public class TopItemMapFunction implements MapFunction<String, UserBehavior> {
    @Override
    public UserBehavior map(String s) throws Exception {
        log.info("flink input stream -> " + s);
        return LogToEntity.getEntity(s);
    }
}
