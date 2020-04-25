package com.example.graduationproject.scheduler;

import com.example.graduationproject.client.HbaseClient;

import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * 定时任务，分析用户行为，将用户感兴趣的产品存入hbase
 *
 * @author Zzwen
 * @date 2020/4/18 14:42
 */
public class SchedulerJob {

    static ExecutorService executorService = Executors.newFixedThreadPool(10);

    /**
     * 每12小时定时调度一次 基于两个推荐策略的 产品评分计算
     * 策略1 ：协同过滤
     * <p>
     * 数据写入Hbase表  px
     *
     * @param args
     */
    public static void main(String[] args) {
        //		ScheduledExecutorService pool = new ScheduledThreadPoolExecutor(5);
        Timer qTimer = new Timer();
        qTimer.scheduleAtFixedRate(new RefreshTask(), 0, 15 * 1000);// 定时每15分钟


    }

    private static class RefreshTask extends TimerTask {

        @Override
        public void run() {
            System.out.println(new Date() + " 开始执行任务！");
            //取出被用户点击过的产品id
            List<String> allProId;
            try {
                allProId = HbaseClient.getAllKey("item_history");
            } catch (IOException e) {
                System.err.println("获取历史产品id异常: " + e.getMessage());
                e.printStackTrace();
                return;
            }
            //可以考虑任务执行前是否需要把历史记录删掉
            for (String id : allProId) {
                // 每12小时调度一次
                executorService.execute(new Task(id, allProId));
            }
        }
    }

    private static class Task implements Runnable {

        private String id;
        private List<String> others;

        Task(String id, List<String> others) {
            this.id = id;
            this.others = others;
        }

        ItemCfCalculate item = new ItemCfCalculate();

        @Override
        public void run() {
            try {
                item.getItemCoefficient(id, others);
            } catch (Exception e) {
                e.printStackTrace();
            }

        }
    }
}
