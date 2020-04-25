package com.example.graduationproject.web;

import com.example.graduationproject.web.client.RedisClient;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@SpringBootTest
class GraduationProjectWebApplicationTests {

    @Resource
    private RedisClient redisClient;

    @Test
    void redisTest(){
        String data = redisClient.getData("0");
        System.out.println(data);
    }

    @Test
    void contextLoads() {
        List<String> stringList = new ArrayList<>();
        for(int i = 0;i<4;i++){
            stringList.add(String.valueOf(i));
        }
        List<String> moreList = new ArrayList<>(10);
        for (int i = 0; i < 10; i++) {
            moreList.add(String.valueOf(i));
        }
        if(stringList.size()<5){
            stringList.addAll(moreList);
            stringList = stringList.stream().distinct().collect(Collectors.toList());
        }
        System.out.println(stringList);
    }

}
