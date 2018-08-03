package com.yunxi.yunxiguavathread.test.seniorcharacter;

import com.google.common.util.concurrent.*;
import com.yunxi.yunxiguavathread.test.common.FutureCallBackTask;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

/**
 * JdkFutureAdapters.listenInPoolThread(jdkthread): guava同时提供了将JDK Future转换为ListenableFuture的接口函数。
 */
@RestController
public class JdkFutureAdaptersController {

    @Autowired
    private ListeningExecutorService listeningExecutorService;

    @RequestMapping("/test/adapter")
    public void execute() {
        // 固定大小的线程池 核心线程数和最大线程数=10
        ExecutorService executorService = Executors.newFixedThreadPool(10);
        // 记录开始时间
        Long start = System.currentTimeMillis();
        // 一个耗时的任务
        Future<Boolean> future = executorService.submit(new Callable<Boolean>() {
            /**
             * Computes a result, or throws an exception if unable to do so.
             *
             * @return computed result
             * @throws Exception if unable to compute a result
             */
            @Override
            public Boolean call() throws Exception {
                //模拟耗时5s
                Thread.sleep(5000);
                return true;
            }
        });
        ListenableFuture listenableFuture = JdkFutureAdapters.listenInPoolThread(future);
        Futures.addCallback(listenableFuture, new FutureCallBackTask(), listeningExecutorService);
        // 记录结束时间
        Long end = System.currentTimeMillis();
        // 执行时间
        System.out.println("线程执行结束了，耗时=" + (end - start) + "毫秒");
        System.out.println("-----------------------华丽的分割线-----------------------");
    }
}
