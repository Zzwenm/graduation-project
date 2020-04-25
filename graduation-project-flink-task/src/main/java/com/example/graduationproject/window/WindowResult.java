package com.example.graduationproject.window;

import com.example.graduationproject.domain.ItemViewCount;
import org.apache.flink.api.java.tuple.Tuple;
import org.apache.flink.streaming.api.functions.windowing.WindowFunction;
import org.apache.flink.streaming.api.windowing.windows.TimeWindow;
import org.apache.flink.util.Collector;

/**
 * 自定义窗口函数，计算输出itemViewCount
 *
 * @author Zzwen
 * @date 2020/4/14 14:44
 */
public class WindowResult implements WindowFunction<Long, ItemViewCount, Long, TimeWindow> {
    @Override
    public void apply(Long key, TimeWindow timeWindow, Iterable<Long> input, Collector<ItemViewCount> out) throws Exception {
        Long itemId = key;
        Long endTime = timeWindow.getEnd();
        Long count = input.iterator().next();
        out.collect(new ItemViewCount(itemId, endTime, count,null));
    }

}
