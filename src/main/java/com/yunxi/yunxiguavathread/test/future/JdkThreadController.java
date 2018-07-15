package com.yunxi.yunxiguavathread.test.future;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.*;

/**
 * JDK 线程测试
 */
@RestController
public class JdkThreadController {

    @RequestMapping("/test/jdk")
    public void execute() throws ExecutionException, InterruptedException {
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

        // 阻塞 等待执行结果
        Boolean result = future.get();
        //打印结果
        System.out.println("任务执行成功了，执行结果=" + result);
        // 记录结束时间
        Long end = System.currentTimeMillis();
        // 执行时间
        System.out.println("线程执行结束了，耗时=" + (end - start) + "毫秒");
        System.out.println("-----------------------华丽的分割线-----------------------");
    }
}

