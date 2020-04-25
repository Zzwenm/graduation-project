package com.example.graduationproject.web.service.impl;

import com.example.graduationproject.client.HbaseClient;
import com.example.graduationproject.web.client.RedisClient;
import com.example.graduationproject.web.dto.ResultDto;
import com.example.graduationproject.web.entity.Book;
import com.example.graduationproject.web.mapper.BookMapper;
import com.example.graduationproject.web.service.BookService;
import com.example.graduationproject.web.service.KafkaService;
import com.example.graduationproject.web.vo.BookVo;
import com.example.graduationproject.web.vo.RecommendVo;
import com.github.pagehelper.PageHelper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * @author Zzwen
 * @date 2020/4/21 14:14
 */
@Slf4j
@Service
public class BookServiceImpl implements BookService {

    /**
     * 榜单个数
     */
    private static final int TOP_SIZE = 10;

    /**
     * 相似产品推荐个数
     */
    private static final int RECOMMEND_SIZE = 3;

    @Resource
    private BookMapper bookMapper;

    @Resource
    private RedisClient redisClient;

    @Resource
    private KafkaService kafkaService;

    /**
     * 获取书籍信息
     * 1、所有书籍
     * 2、榜单书籍
     *
     * @param page 查询页数
     * @param queryName 查询内容
     * @return 结果
     */
    @Override
    public ResultDto fetchData(String page,String queryName) {
        log.info("============================");
        log.info("获取书籍信息：");
        //1、数据库中的所有书
        PageHelper.startPage(Integer.parseInt(page),10);
        List<Book> books = bookMapper.queryAll(queryName);
        List<BookVo> bookVos = new ArrayList<>(books.size());
        for(Book book : books){
            BookVo bookVo = toBookVo(book);
            bookVos.add(bookVo);
        }
        //2、redis中的榜单书籍
        List<String> topBookIdList = getTopBookIdList();
        List<Book> topBooks = bookMapper.selectByIdList(topBookIdList);
        List<BookVo> topBookVos = new ArrayList<>(topBooks.size());
        for(Book book : topBooks){
            BookVo bookVo = toBookVo(book);
            topBookVos.add(bookVo);
        }
        log.info("榜单：\n{}",topBooks);
        RecommendVo recommendVo = new RecommendVo();
        recommendVo.setBooks(bookVos);
        recommendVo.setRecommendBooks(topBookVos);
        log.info("============================");
        return new ResultDto<>(20000, "查询成功！", recommendVo);
    }

    /**
     * 获取书籍的详细信息以及推荐书籍并记录日志
     *
     * @param bookId 书籍id
     * @param userId 用户id
     * @return 结果
     */
    @Override
    public ResultDto getBookDetail(String bookId,String userId) {
        //1、记录每个书籍被点击的日志
        String log = kafkaService.makeLog(userId, bookId, "pv");
        kafkaService.send(null,log);
        //书的详情信息
        Book book = bookMapper.findBookById(bookId);
        //根据此书进行推荐的书籍列表
        List<Book> cfBookList = getCfBookList(bookId);
        List<BookVo> cfBookVoList = null;
        if(cfBookList!=null){
            cfBookVoList = new ArrayList<>(cfBookList.size());
            for(Book cfBook : cfBookList){
                BookVo bookVo = toBookVo(cfBook);
                cfBookVoList.add(bookVo);
            }
        }
        RecommendVo recommendVo = new RecommendVo();
        recommendVo.setBooks(Collections.singletonList(toBookVo(book)));
        recommendVo.setRecommendBooks(cfBookVoList);
        return new ResultDto<>(20000,"",recommendVo);
    }

    /**
     * 根据当前书，进行推荐
     * @param bookId 当前书的id
     * @return 推荐列表
     */
    private List<Book> getCfBookList(String bookId) {
        List<Book> result = null;
        List<Map.Entry> cfBookList = null;
        try {
            //从hbase中取出相似的书籍列表id
            cfBookList = HbaseClient.getRow("item_similar", bookId);
            //根据相似度进行排序
            cfBookList.sort(((o1, o2) -> -(new BigDecimal(o1.getValue().toString()).compareTo(new BigDecimal(o2.getValue().toString())))));
        } catch (Exception e) {
            log.warn("Hbase中没有产品【{}】推荐记录！", bookId);
            return null;
        }
        List<String> cfBookIdList = new ArrayList<>(cfBookList.size());
        for (Map.Entry entry : cfBookList) {
            Object cfBookId = entry.getKey();
            cfBookIdList.add((String) cfBookId);
        }
        //去空
        cfBookIdList = cfBookIdList.stream().filter(Objects::nonNull).collect(Collectors.toList());
        //取前RECOMMEND_SIZE
        int min = Math.min(cfBookIdList.size(), RECOMMEND_SIZE);
        result = bookMapper.selectByIdList(cfBookIdList.subList(0, min));
        return result;
    }

    /**
     * 获取redis中榜单数据
     *
     * @return 榜单书籍id列表
     */
    private List<String> getTopBookIdList() {
        List<String> topList = redisClient.getTopList(TOP_SIZE);
        //去空
        topList = topList.stream().filter(Objects::nonNull).collect(Collectors.toList());

        return topList;
    }

    private BookVo toBookVo(Book book) {
        BookVo bookVo = new BookVo();
        BeanUtils.copyProperties(book,bookVo);
        bookVo.setId(String.valueOf(book.getId()));
        return bookVo;
    }
}
