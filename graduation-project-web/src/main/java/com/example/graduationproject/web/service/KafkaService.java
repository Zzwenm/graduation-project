package com.example.graduationproject.web.service;

import org.springframework.stereotype.Service;

/**
 * @author Zzwen
 * @date 2020/4/17 14:54
 */
public interface KafkaService {
    /**
     * 发送数据
     *
     * @param key   key
     * @param value val
     */
    void send(String key, String value);

    /**
     * 生成日志
     *
     * @param userId 用户id
     * @param itemId 产品id
     * @param action 用户行为
     * @return
     */
    String makeLog(String userId, String itemId, String action);
}
