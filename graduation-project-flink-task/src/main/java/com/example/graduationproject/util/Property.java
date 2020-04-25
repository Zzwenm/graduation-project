package com.example.graduationproject.util;

import org.apache.kafka.clients.consumer.ConsumerConfig;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Properties;

/**
 * 加载配置文件
 *
 * @author Zzwen
 * @date 2020/4/14 16:35
 */
public class Property {
    private final static String CONF_NAME = "config.properties";

    private static Properties contextProperties;

    static {
        InputStream in = Thread.currentThread().getContextClassLoader().getResourceAsStream(CONF_NAME);
        contextProperties = new Properties();
        try {
            InputStreamReader inputStreamReader = new InputStreamReader(in, "UTF-8");
            contextProperties.load(inputStreamReader);
        } catch (IOException e) {
            System.err.println("===============================================");
            System.err.println("Graduation project Flink-task ->资源文件加载失败!");
            System.err.println("===============================================");
            e.printStackTrace();
        }
        System.out.println("===============================================");
        System.out.println("Graduation project Flink-task ->资源文件加载成功!");
        System.out.println("===============================================");
    }

    public static String getStrValue(String key) {
        return contextProperties.getProperty(key);
    }

    public static int getIntValue(String key) {
        String strValue = getStrValue(key);
        // 注意，此处没有做校验，暂且认为不会出错
        return Integer.parseInt(strValue);
    }

    public static Properties getKafkaProperties(String groupId) {

        Properties properties = new Properties();
        //连接到kafka的集群
        properties.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, getStrValue("kafka.bootstrap.servers"));
        //消费者组
        properties.put(ConsumerConfig.GROUP_ID_CONFIG, groupId);

        return properties;
    }
}
