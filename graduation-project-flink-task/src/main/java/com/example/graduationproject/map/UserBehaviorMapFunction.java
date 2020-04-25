package com.example.graduationproject.map;

import com.example.graduationproject.client.HbaseClient;
import com.example.graduationproject.domain.UserBehavior;
import org.apache.flink.api.common.functions.RichMapFunction;
import org.apache.flink.api.common.state.StateTtlConfig;
import org.apache.flink.api.common.state.ValueState;
import org.apache.flink.api.common.state.ValueStateDescriptor;
import org.apache.flink.api.common.time.Time;
import org.apache.flink.configuration.Configuration;

/**
 * 用户行为分析，存入hbase
 *
 * @author Zzwen
 * @date 2020/4/15 17:01
 */
public class UserBehaviorMapFunction extends RichMapFunction<UserBehavior, String> {

    private ValueState<UserBehavior> state;

    /**
     * 获取上次的值
     *
     * @param parameters 参数值
     * @throws Exception 异常
     */
    @Override
    public void open(Configuration parameters) throws Exception {

        // 设置 state 的过期时间为100s
        StateTtlConfig ttlConfig = StateTtlConfig
                .newBuilder(Time.seconds(100L))
                .setUpdateType(StateTtlConfig.UpdateType.OnCreateAndWrite)
                .setStateVisibility(StateTtlConfig.StateVisibility.NeverReturnExpired)
                .build();

        ValueStateDescriptor<UserBehavior> desc = new ValueStateDescriptor<>("UserBehavior time", UserBehavior.class);
        desc.enableTimeToLive(ttlConfig);
        state = getRuntimeContext().getState(desc);
    }

    /**
     * 对两次的行为操作进行分析，存入hbase
     *
     * @param thisTime 用户行为
     * @throws Exception 异常
     */
    @Override
    public String map(UserBehavior thisTime) throws Exception {
        UserBehavior lastTime = state.value();
        int times = 1;
        // 如果是第一次行为操作，将数值设为1保存
        if(lastTime != null){
            times = getTimesByRule(lastTime, thisTime);
        }
        saveToHbase(thisTime, times);
        // 如果用户收藏了，记为对当前内容十分感兴趣
        if ("3".equals(thisTime.getBehavior())) {
            state.clear();
        }
        return null;
    }

    /**
     * 获取次数
     *
     * @param lastTime 上次行为
     * @param thisTime 当前行为
     * @return 次数
     */
    private int getTimesByRule(UserBehavior lastTime, UserBehavior thisTime) {
        // 动作主要有3种类型
        // 1 -> 浏览  2 -> 分享  3 -> 收藏
        int a1 = Integer.parseInt(lastTime.getBehavior());
        int a2 = Integer.parseInt(thisTime.getBehavior());
        Long t1 = lastTime.getTimeTemp();
        Long t2 = thisTime.getTimeTemp();
        int pluse = 1;
        // 如果用户在短时间内进行分享或者收藏，记为用户对当前内容感兴趣
        if (a2 > a1 && (t2 - t1) < 100_000L) {
            pluse *= a2 - a1;
        }
        return pluse;
    }

    /**
     * 保存到Hbase
     *
     * @param userBehavior 用户行为
     * @param times        次数
     * @throws Exception 异常
     */
    private void saveToHbase(UserBehavior userBehavior, int times) throws Exception {
        if (userBehavior != null) {
            for (int i = 0; i < times; i++) {
                HbaseClient.increamColumn("u_interest", String.valueOf(userBehavior.getUserId()), "p", String.valueOf(userBehavior.getItemId()));
            }
        }

    }
}
