package com.example.graduationproject.task;

import com.example.graduationproject.agg.CountAgg;
import com.example.graduationproject.domain.ItemViewCount;
import com.example.graduationproject.domain.UserBehavior;
import com.example.graduationproject.map.TopItemMapFunction;
import com.example.graduationproject.sink.TopNRedisSink;
import com.example.graduationproject.top.TopNHotItems;
import com.example.graduationproject.util.Property;
import com.example.graduationproject.window.WindowResult;
import org.apache.flink.api.common.serialization.SimpleStringSchema;
import org.apache.flink.streaming.api.TimeCharacteristic;
import org.apache.flink.streaming.api.datastream.DataStream;
import org.apache.flink.streaming.api.datastream.DataStreamSource;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.api.functions.timestamps.AscendingTimestampExtractor;
import org.apache.flink.streaming.api.windowing.time.Time;
import org.apache.flink.streaming.connectors.kafka.FlinkKafkaConsumer;
import org.apache.flink.streaming.connectors.redis.RedisSink;
import org.apache.flink.streaming.connectors.redis.common.config.FlinkJedisPoolConfig;

import java.util.Properties;

/**
 * topN -> redis
 * 实时热门商品统计，通过统计将前n数据存入redis
 * 每五分钟统计一小时内最热门的商品
 *
 * @author Zzwen
 * @date 2020/4/14 17:04
 */
public class TopNItemTask {
    private static final int TOP_SIZE = 3;

    public static void main(String[] args) throws Exception {

        StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();
        env.setParallelism(1);
        // 开启EventTime
        env.setStreamTimeCharacteristic(TimeCharacteristic.EventTime);

        //配置redis
        FlinkJedisPoolConfig conf = new FlinkJedisPoolConfig.Builder()
                .setHost(Property.getStrValue("redis.host"))
                .setPort(Property.getIntValue("redis.port"))
                .setDatabase(Property.getIntValue("redis.db"))
                .setPassword(Property.getStrValue("redis.password"))
                .build();

        //配置kafka
        Properties properties = Property.getKafkaProperties("topItem");
        //从kafka获取数据流
        DataStreamSource<String> dataStream = env.addSource(new FlinkKafkaConsumer<>("book", new SimpleStringSchema(), properties));

        //静态文件测试
//        DataStreamSource<String> dataStream = env.readTextFile("F:\\毕业设计\\graduation-project\\data.csv");

        DataStream<ItemViewCount> topProduct =
                //封装成UserBehavior
                dataStream.map(new TopItemMapFunction()).
                        // 抽取时间戳做watermark 以 秒 为单位
                                assignTimestampsAndWatermarks(new AscendingTimestampExtractor<UserBehavior>() {
                            @Override
                            public long extractAscendingTimestamp(UserBehavior userBehavior) {
                                return userBehavior.getTimeTemp() * 1000;
                            }
                        })
                        // 按照itemId划分，设置滑动窗口
                        .keyBy(UserBehavior::getItemId).timeWindow(Time.hours(1), Time.minutes(5))
                        .aggregate(new CountAgg(), new WindowResult())
                        // 按照窗口结束时间进行再次划分
                        .keyBy(ItemViewCount::getWindowEnd)
                        .process(new TopNHotItems(TOP_SIZE));
        topProduct.print();
        //存入redis
        topProduct.addSink(new RedisSink<>(conf, new TopNRedisSink()));

        env.execute("TopN Job");
    }
}
