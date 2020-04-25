package com.example.graduationproject.sink;

import com.example.graduationproject.domain.ItemViewCount;
import org.apache.flink.streaming.connectors.redis.common.mapper.RedisCommand;
import org.apache.flink.streaming.connectors.redis.common.mapper.RedisCommandDescription;
import org.apache.flink.streaming.connectors.redis.common.mapper.RedisMapper;

/**
 * @author Zzwen
 * @date 2020/4/15 9:28
 */
public class TopNRedisSink implements RedisMapper<ItemViewCount> {


    @Override
    public RedisCommandDescription getCommandDescription() {
        return new RedisCommandDescription(RedisCommand.SET, null);
    }

    @Override
    public String getKeyFromData(ItemViewCount item) {
        //将产品的排名次序作为key
        return String.valueOf(item.getRank());
    }

    @Override
    public String getValueFromData(ItemViewCount item) {
        //产品id作为value，便于后台获取产品
        return String.valueOf(item.getItemId());
    }
}
