package com.example.graduationproject.top;

import com.example.graduationproject.domain.ItemViewCount;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.apache.flink.api.common.state.ListState;
import org.apache.flink.api.common.state.ListStateDescriptor;
import org.apache.flink.configuration.Configuration;
import org.apache.flink.streaming.api.functions.KeyedProcessFunction;
import org.apache.flink.util.Collector;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

/**
 * 自定义的处理函数,用于统计topN商品
 *
 * @author Zzwen
 * @date 2020/4/14 14:51
 */
@Slf4j
public class TopNHotItems extends KeyedProcessFunction<Long, ItemViewCount, ItemViewCount> {

    private final int topSize;

    private ListState<ItemViewCount> itemState;

    public TopNHotItems(int topSize) {
        this.topSize = topSize;
    }

    @Override
    public void processElement(ItemViewCount itemViewCount, Context context, Collector<ItemViewCount> collector) throws Exception {
        //把每条数据存入状态列表
        itemState.add(itemViewCount);
        //注册一个定时器
        context.timerService().registerEventTimeTimer(itemViewCount.getWindowEnd() + 1);
    }

    @Override
    public void open(Configuration parameters) throws Exception {
        super.open(parameters);
        // 状态的注册
        ListStateDescriptor<ItemViewCount> itemsStateDesc = new ListStateDescriptor<>("itemState-state", ItemViewCount.class);
        itemState = getRuntimeContext().getListState(itemsStateDesc);
    }

    @Override
    public void onTimer(long timestamp, OnTimerContext ctx, Collector<ItemViewCount> out) throws Exception {
        List<ItemViewCount> allItems = new ArrayList<>();
        for (ItemViewCount item : itemState.get()) {
            allItems.add(item);
        }
        // 按照点击量从大到小排序
        allItems.sort((o1, o2) -> (int) (o2.getCount() - o1.getCount()));
        //提取topN
        List<ItemViewCount> topItems;
        if (allItems.size() > topSize) {
            topItems = new ArrayList<>(topSize);
            topItems = allItems.subList(0, topSize);
        }
        topItems = allItems;
        // 提前清除状态中的数据，释放空间
        itemState.clear();

        StringBuilder result = new StringBuilder();

        result.append("时间：").append(new Timestamp(timestamp - 1)).append("\n");

        //对每个产品结果 的排名 进行赋值 并打印校对
        for (int i = 0; i < topItems.size(); i++) {
            ///排名
            topItems.get(i).setRank(String.valueOf(i));
            result.append("No").append(i + 1).append(":")
                    .append(" 商品id = ").append(topItems.get(i).getItemId())
                    .append(" 浏览量 = ").append(topItems.get(i).getCount())
                    .append("\n");

            out.collect(topItems.get(i));
        }
        result.append("========================");

        log.info(result.toString());
        log.info(String.valueOf(out));

    }
}
