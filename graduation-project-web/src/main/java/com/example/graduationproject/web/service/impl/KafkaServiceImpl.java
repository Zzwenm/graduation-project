package com.example.graduationproject.web.service.impl;

import com.example.graduationproject.web.service.KafkaService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Service;
import org.springframework.util.concurrent.ListenableFuture;
import org.springframework.util.concurrent.ListenableFutureCallback;

import javax.annotation.Resource;
import java.util.Date;

/**
 * kafka逻辑层，主要对kafka进行操作
 *
 * @author Zzwen
 * @date 2020/4/17 14:56
 */
@Slf4j
@Service
public class KafkaServiceImpl implements KafkaService {

    /**
     * 话题
     */
    private final String TOPIC = "book";

    @Resource
    private KafkaTemplate<String, String> kafkaTemplate;

    /**
     * 发送数据
     *
     * @param key   key
     * @param value val
     */
    @Override
    public void send(String key, String value) {
        ListenableFuture<SendResult<String, String>> send = kafkaTemplate.send(TOPIC, key, value);

        send.addCallback(new ListenableFutureCallback<SendResult<String, String>>() {

            @Override
            public void onFailure(Throwable throwable) {
                log.error("kafka send msg err, ex = {}, topic = {}, data = {}", throwable, TOPIC, value);
            }

            @Override
            public void onSuccess(SendResult<String, String> integerStringSendResult) {
                log.info("kafka send msg success, topic = {}, data = {}", TOPIC, value);
            }
        });
    }

    /**
     * @param userId 用户id
     * @param itemId 产品id
     * @param action 用户行为
     * @return 日志信息
     */
    @Override
    public String makeLog(String userId, String itemId, String action) {
        StringBuilder logInfo = new StringBuilder();
        logInfo.append(userId).append(",")
                .append(itemId).append(",")
                .append(action).append(",")
                .append(getSecondTimestamp(new Date()));
        return logInfo.toString();
    }

    /**
     * 获取时间戳
     *
     * @param date 时间
     * @return 时间戳
     */
    private static String getSecondTimestamp(Date date) {
        if (null == date) {
            return "";
        }
        String timestamp = String.valueOf(date.getTime());
        int length = timestamp.length();
        if (length > 3) {
            return timestamp.substring(0, length - 3);
        } else {
            return "";
        }
    }

}
