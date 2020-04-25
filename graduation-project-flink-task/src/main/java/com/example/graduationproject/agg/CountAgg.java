package com.example.graduationproject.agg;

import com.example.graduationproject.domain.UserBehavior;
import org.apache.flink.api.common.functions.AggregateFunction;

/**
 * 预聚合函数
 * @author Zzwen
 * @date 2020/4/14 14:39
 */
public class CountAgg implements AggregateFunction<UserBehavior, Long, Long> {

    @Override
    public Long createAccumulator() {
        return 0L;
    }

    @Override
    public Long add(UserBehavior userBehavior, Long aLong) {
        return aLong + 1;
    }

    @Override
    public Long getResult(Long aLong) {
        return aLong;
    }

    @Override
    public Long merge(Long aLong, Long acc1) {
        return aLong + acc1;
    }
}
