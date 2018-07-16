package com.yunxi.yunxiguavathread.test.listenablefuture;

import com.google.common.util.concurrent.*;
import com.yunxi.yunxiguavathread.test.common.FutureCallBackTask;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Guava 线程测试
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
            //模拟耗时5s
            Thread.sleep(5000);
            return true;
        });
        // 注册回调事件
        Futures.addCallback(listenableFuture, new FutureCallBackTask(), listeningExecutorService);
        // 记录结束时间
        Long end = System.currentTimeMillis();
        // 执行时间
        System.out.println("线程执行结束了，耗时=" + (end - start) + "毫秒");
        System.out.println("-----------------------华丽的分割线-----------------------");
    }
}

