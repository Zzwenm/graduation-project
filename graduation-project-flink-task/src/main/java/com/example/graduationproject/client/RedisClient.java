package com.example.graduationproject.client;

import com.example.graduationproject.util.Property;
import redis.clients.jedis.Jedis;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Zzwen
 * @date 2020/4/15 16:24
 */
public class RedisClient {
    private static Jedis jedis;

    static {
        jedis = new Jedis(Property.getStrValue("redis.host"), Property.getIntValue("redis.port"));
        jedis.select(Property.getIntValue("redis.db"));
    }

    private static RedisClient redisClient;


    private String getData(String key){
        return jedis.get(key);
    }

    public List<String> getTopList(int topRange){
        List<String> res = new ArrayList<>();
        for (int i = 0; i < topRange; i++) {
            res.add(getData(String.valueOf(i)));
        }
        return res;
    }


    public static void main(String[] args) {
        RedisClient client = new RedisClient();

        String data = client.getData("3");
        System.out.println(data);
    }
}
