package com.example.graduationproject.web.controller;

import com.example.graduationproject.web.dto.ResultDto;
import com.example.graduationproject.web.service.BookService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;

/**
 * 书籍控制器
 *
 * @author Zzwen
 * @date 2020/4/21 14:08
 */
@Controller
@ResponseBody
@RequestMapping("/book")
public class BookController {

    @Resource
    private BookService bookService;

    /**
     * 获取主页中要展示的书籍信息和榜单信息
     *
     * @return 结果
     */
    @GetMapping("/doFetchData")
    public ResultDto fetchData(@RequestParam String page,@RequestParam String queryName) {
        return bookService.fetchData(page,queryName);
    }

    /**
     * 获取书籍的详细信息以及推荐书籍
     *
     * @param bookId 书籍id
     * @param userId 书籍id
     * @return 结果
     */
    @GetMapping("/doGetDetail")
    public ResultDto getBookDetail(@RequestParam String bookId, @RequestParam String userId) {
        return bookService.getBookDetail(bookId, userId);
    }

}
