package com.example.graduationproject.web.client;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

/**
 * redis 封装工具
 *
 * @author Zzwen
 * @date 2020/4/17 11:33
 */
@Component
public class RedisClient {

    @Resource
    private RedisTemplate<String, String> redisTemplate;

    /**
     * 获取redis数据
     *
     * @param key key
     * @return 对应的value
     */
    public String getData(String key) {
        return redisTemplate.opsForValue().get(key);
    }

    public void setData(String key, String value) {
        redisTemplate.opsForValue().set(key, value);
    }

    /**
     * 获取 top 榜单
     *
     * @param topRange 榜单前n个
     * @return 榜单列表，id列表
     */
    public List<String> getTopList(int topRange) {
        List<String> result = new ArrayList<>();

        for (int i = 0; i < topRange; i++) {
            result.add(getData(String.valueOf(i)));
        }
        return result;
    }
}
