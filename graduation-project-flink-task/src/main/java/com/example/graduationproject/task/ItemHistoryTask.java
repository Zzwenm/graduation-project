package com.example.graduationproject.task;

import com.example.graduationproject.map.UserHistoryMapFunction;
import com.example.graduationproject.util.Property;
import org.apache.flink.api.common.serialization.SimpleStringSchema;
import org.apache.flink.streaming.api.datastream.DataStreamSource;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.connectors.kafka.FlinkKafkaConsumer;

import java.util.Properties;

/**
 * 统计产品的点击用户都有哪些，存入hbase，便于后续的协同过滤推荐
 *
 * @author Zzwen
 * @date 2020/4/19 10:06
 */
public class ItemHistoryTask {
    public static void main(String[] args) throws Exception {

        StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();

        Properties properties = Property.getKafkaProperties("history");
        DataStreamSource<String> dataStream = env.addSource(new FlinkKafkaConsumer<>("book", new SimpleStringSchema(), properties));
        dataStream.map(new UserHistoryMapFunction());

        env.execute("Item History Task");
    }
}
