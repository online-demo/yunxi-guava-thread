package com.yunxi.yunxiguavathread.test.seniorcharacter;

import com.google.common.util.concurrent.*;
import com.yunxi.yunxiguavathread.test.common.FutureCallBackTask;
import org.checkerframework.checker.nullness.qual.Nullable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 *  高级特性介绍
 *
 *  transform：对于ListenableFuture的返回值进行转换。
 *
 *  allAsList：对多个ListenableFuture的合并，返回一个当所有Future成功时返回多个Future返回值组成的List对象。
 *  注：当其中一个Future失败或者取消的时候，将会进入失败或者取消。
 *
 *  successfulAsList：和allAsList相似，唯一差别是对于失败或取消的Future返回值用null代替。不会进入失败或者取消流程。
 *
 *  immediateFuture/immediateCancelledFuture： 立即返回一个待返回值的ListenableFuture。
 *
 *  makeChecked: 将ListenableFuture 转换成CheckedFuture。CheckedFuture 是一个ListenableFuture 。
 *  其中包含了多个版本的get 方法，方法声明抛出检查异常.这样使得创建一个在执行逻辑中可以抛出异常的Future更加容易
 *
 *  JdkFutureAdapters.listenInPoolThread(jdkthread): guava同时提供了将JDK Future转换为ListenableFuture的接口函数。
 */
@RestController
public class SeniorListenableFutureController {

    @Autowired
    private ListeningExecutorService listeningExecutorService;

    @RequestMapping("/test/senior")
    public void execute() {

        long start = System.currentTimeMillis();

        // 任务1
        ListenableFuture future1 = listeningExecutorService.submit(() -> {
            Thread.sleep(5000);
            System.out.printf("调用第1个future，执行时间是%d%n", System.currentTimeMillis());
            return 1;
        });

        // 任务2
        ListenableFuture future2 = listeningExecutorService.submit(() -> {
            Thread.sleep(10000);
            System.out.printf("调用第2个future，执行时间是%d%n", System.currentTimeMillis());
//                   throw new RuntimeException("任务2出现了异常");
            return 2;
        });

        //对多个ListenableFuture的合并，返回一个当所有Future成功时返回多个Future返回值组成的List对象。
        // 注：当其中一个Future失败或者取消的时候，将会进入失败或者取消。
        final ListenableFuture allFutures = Futures.allAsList(future1, future2);

        //对于多个ListenableFuture的进行转换，返回一个新的ListenableFuture
        final ListenableFuture transform = Futures.transformAsync(allFutures, new AsyncFunction<List<Integer>, String>() {
            /**
             * 用给定的输入封装一个特定的ListenableFuture作为输出
             *
             * @param input
             */
            @Override
            public ListenableFuture<String> apply(@Nullable List<Integer> input) {
                // 立即返回一个带返回值的ListenableFuture
                // 这里可以对input进行复杂的处理，返回最终的一个结果  比如：对团单详情，团单优惠，团单使用范围进行组装
                return Futures.immediateFuture(String.format("执行成功的任务的数量是:%d", input.size()));
            }
        }, listeningExecutorService);

        // 注册回调事件
        Futures.addCallback(transform, new FutureCallBackTask(), listeningExecutorService);

        long end = System.currentTimeMillis();
        System.out.printf("接口总耗时%d毫秒%n", end - start);
        System.out.println("-----------------------华丽的分割线-----------------------");
    }
}
