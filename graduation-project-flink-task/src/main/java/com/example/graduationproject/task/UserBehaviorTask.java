package com.example.graduationproject.task;

import com.example.graduationproject.map.GetLogFunction;
import com.example.graduationproject.map.UserBehaviorMapFunction;
import com.example.graduationproject.util.Property;
import org.apache.flink.api.common.serialization.SimpleStringSchema;
import org.apache.flink.streaming.api.datastream.DataStreamSource;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.connectors.kafka.FlinkKafkaConsumer;

import java.util.Properties;

/**
 * 根据用户的行为，分析用户可能感兴趣的item，存入HBase
 * 先根据用户进行划分，再通过计算，将可靠数据存入HBase
 *
 * @author Zzwen
 * @date 2020/4/15 16:57
 */
public class UserBehaviorTask {

    public static void main(String[] args) throws Exception {

        StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();

        Properties properties = Property.getKafkaProperties("interest");
        DataStreamSource<String> dataStream = env.addSource(new FlinkKafkaConsumer<>("book", new SimpleStringSchema(), properties));
        dataStream.map(new GetLogFunction()).keyBy("userId").map(new UserBehaviorMapFunction());


        env.execute("User Product History");
    }

}
