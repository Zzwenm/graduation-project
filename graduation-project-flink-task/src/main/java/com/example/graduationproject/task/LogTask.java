package com.example.graduationproject.task;

import com.example.graduationproject.map.LogMapFunction;
import com.example.graduationproject.util.Property;
import org.apache.flink.streaming.api.datastream.DataStreamSource;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.connectors.kafka.FlinkKafkaConsumer;
import org.apache.flink.streaming.util.serialization.SimpleStringSchema;

import java.util.Properties;

/**
 * 日志 -> Hbase
 * 从日志读取数据保存到HBase
 *
 * @author Zzwen
 * @date 2020/4/14 17:10
 */
public class LogTask {
    public static void main(String[] args) throws Exception {

        StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();

        Properties properties = Property.getKafkaProperties("log");
        DataStreamSource<String> dataStream = env.addSource(new FlinkKafkaConsumer<>("book", new SimpleStringSchema(), properties));
        dataStream.map(new LogMapFunction());

        env.execute("Log message receive");
    }
}
