package com.yunxi.yunxiguavathread.test.guava.firstguava;

import com.google.common.util.concurrent.*;
import com.yunxi.yunxiguavathread.test.guava.common.FutureCallBackTask;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.Callable;

/**
 * @Author: 无双老师
 * @Date: 2018/10/27 09:45
 * @Description: Guava 线程简单测试
 */
@RestController
public class GuavaThreadController {

    @Autowired
    private ListeningExecutorService listeningExecutorService;

    @RequestMapping("/test/guava")
    public void execute() {
        // 记录开始时间
        Long start = System.currentTimeMillis();
        // 一个耗时的任务
        ListenableFuture<Boolean> listenableFuture = listeningExecutorService.submit(() -> {
            Thread.sleep(5000);
            return true;
        });
        // 注册回调事件
        Futures.addCallback(listenableFuture, new FutureCallBackTask(), listeningExecutorService);
        // 记录结束时间
        Long end = System.currentTimeMillis();
        // 执行时间
        System.out.println("execute()方法执行结束了，耗时=" + (end - start) + "毫秒");
        System.out.println("-----------------------华丽的分割线-----------------------");
    }
}

