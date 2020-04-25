package com.example.graduationproject.web.controller;

import com.example.graduationproject.web.dto.ItemDto;
import com.example.graduationproject.web.dto.ResultDto;
import com.example.graduationproject.web.service.ItemCfCoefficientService;
import com.example.graduationproject.web.service.KafkaService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import java.io.IOException;
import java.util.List;

/**
 * 推荐控制层
 *
 * @author Zzwen
 * @date 2020/4/17 14:53
 */
@Controller
public class RecommendController {

    @Resource
    private ItemCfCoefficientService itemCfCoefficientService;

    @Resource
    private KafkaService kafkaService;

    /**
     * 返回推荐页面
     *
     * @param userId
     * @return
     * @throws IOException
     */
    @GetMapping("/recommend")
    public String recommendByUserId(@RequestParam("userId") String userId,
                                    Model model) throws IOException {

        // 获取topN榜单
        List<ItemDto> hotList = itemCfCoefficientService.getTopNHotItemList();
        // 获取用户可能感兴趣的产品
        List<ItemDto> itemCfList = itemCfCoefficientService.getCfItemList();

        // 将结果返回给前端
        model.addAttribute("userId", userId);
        model.addAttribute("hotList", hotList);
        model.addAttribute("itemCfList", itemCfList);

        return "user";
    }

    @GetMapping("/log")
    @ResponseBody
    public ResultDto logToKafka(@RequestParam("id") String userId,
                                @RequestParam("prod") String productId,
                                @RequestParam("action") String action) {

        String log = kafkaService.makeLog(userId, productId, action);
        kafkaService.send(null, log);
        return new ResultDto<>(200, "成功！", null);
    }
}
