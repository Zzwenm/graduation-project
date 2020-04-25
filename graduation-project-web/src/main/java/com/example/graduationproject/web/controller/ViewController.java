package com.example.graduationproject.web.controller;

import com.example.graduationproject.web.client.RedisClient;
import com.example.graduationproject.web.dto.ResultDto;
import com.example.graduationproject.web.entity.ItemDetail;
import com.example.graduationproject.web.service.ItemDetailService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author Zzwen
 * @date 2020/4/17 15:24
 */
@Controller
public class ViewController {

    /**
     * 榜单推荐个数
     */
    private final int TOP_SIZE = 10;

    @Resource
    private RedisClient redisClient;

    @Resource
    ItemDetailService itemDetailService;

    /**
     * 获取后台数据
     *
     * @return json
     */
    @GetMapping("/index")
    public String getBackStage(Model model) {
        // 获取 top 榜单数据,是产品id列表
        List<String> topList = redisClient.getTopList(TOP_SIZE);
        List<ItemDetail> topProduct = itemDetailService.getItemDetailListByIds(topList);
        model.addAttribute("topProduct", topProduct);
        return "index";
    }

    /**
     * 获取1小时内日志接入量
     *
     * @return 结果
     */
    @ResponseBody
    @GetMapping("/meter")
    public ResultDto getMeter() {
        // 获取 1小时内接入量
//        String meter = redisClient.getMeter();
        String meter = "69";
        return new ResultDto<>(200, "成功！", null);
    }
}
