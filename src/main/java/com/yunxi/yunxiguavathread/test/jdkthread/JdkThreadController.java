package com.yunxi.yunxiguavathread.test.jdkthread;

import com.yunxi.yunxiguavathread.test.service.OuterService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.*;

/**
 * @Author: 无双老师
 * @Date: 2018/10/27 09:45
 * @Description: JDK多线程操作演示
 */
@RestController
public class JdkThreadController {

    @Resource
    private OuterService outerService;

    @RequestMapping("/test/jdk")
    public Map<String, Object> execute() throws ExecutionException, InterruptedException {
        // 固定大小的线程池 核心线程数和最大线程数=10
        ExecutorService executorService = Executors.newFixedThreadPool(10);
        // 记录开始时间
        Long start = System.currentTimeMillis();
        // 异步调用用户服务
        Future<Long> userServiceFuture = executorService.submit(new Callable<Long>() {
            @Override
            public Long call() throws Exception {
                return outerService.userService();
            }
        });
        // 异步调用订单服务
        Future<Long> orderServiceFuture = executorService.submit(new Callable<Long>() {
            @Override
            public Long call() throws Exception {
                return outerService.orderService();
            }
        });
        // 异步调用商品服务
        Future<Long> itemServiceFuture = executorService.submit(new Callable<Long>() {
            @Override
            public Long call() throws Exception {
                return outerService.itemService();
            }
        });
        // 阻塞 等待执行结果
        long userServiceResult = userServiceFuture.get();
        long orderServiceResult = orderServiceFuture.get();
        long itemServiceResult = itemServiceFuture.get();

        //结束调用的时间
        long end = System.currentTimeMillis();
        //计算结果
        long result = userServiceResult + orderServiceResult + itemServiceResult;
        String time = "总调用时间是：" + (end - start) + "毫秒";
        //为什么要初始化4个容量的Map
        Map<String, Object> resultMap = new HashMap<>(4);
        resultMap.put("time", time);
        resultMap.put("result", result);
        //结果
        return resultMap;
    }
}

