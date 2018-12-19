package com.yunxi.yunxiguavathread.test.guava.seniorcharacter;

import com.google.common.util.concurrent.*;
import com.yunxi.yunxiguavathread.test.guava.common.FutureCallBackTask;
import com.yunxi.yunxiguavathread.test.service.OuterService;
import org.checkerframework.checker.nullness.qual.Nullable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;

/**
 * @Author: 无双老师
 * @Date: 2018/10/27 09:45
 * @Description: 高级特性介绍
 * <p>
 * transform：对于ListenableFuture的返回值进行转换。
 * <p>
 * allAsList：对多个ListenableFuture的合并，返回一个当所有Future成功时返回多个Future返回值组成的List对象。
 * 注：当其中一个Future失败或者取消的时候，将会进入失败或者取消。
 * <p>
 * successfulAsList：和allAsList相似，唯一差别是对于失败或取消的Future返回值用null代替。不会进入失败或者取消流程。
 * <p>
 * immediateFuture/immediateCancelledFuture： 立即返回一个待返回值的ListenableFuture。
 * <p>
 * makeChecked: 将ListenableFuture 转换成CheckedFuture。CheckedFuture 是一个ListenableFuture 。
 * 其中包含了多个版本的get 方法，方法声明抛出检查异常.这样使得创建一个在执行逻辑中可以抛出异常的Future更加容易
 * <p>
 * JdkFutureAdapters.listenInPoolThread(jdkthread): guava同时提供了将JDK Future转换为ListenableFuture的接口函数。
 */
@RestController
public class SeniorListenableFutureController {

    @Autowired
    private ListeningExecutorService listeningExecutorService;
    @Autowired
    private OuterService outerService;

    @RequestMapping("/test/senior")
    public Map<String, Object> execute() throws ExecutionException, InterruptedException {

        long start = System.currentTimeMillis();

        // 异步调用用户服务
        ListenableFuture<Long> userServiceFuture = listeningExecutorService.submit(new Callable<Long>() {
            @Override
            public Long call() throws Exception {
                return outerService.userService();
            }
        });

        // 异步调用订单服务
        ListenableFuture<Long> orderServiceFuture = listeningExecutorService.submit(new Callable<Long>() {
            @Override
            public Long call() throws Exception {
                return outerService.orderService();
            }
        });

        // 异步调用商品服务
        ListenableFuture<Long> itemServiceFuture = listeningExecutorService.submit(new Callable<Long>() {
            @Override
            public Long call() throws Exception {
                return outerService.itemService();
            }
        });
        //注册商品服务回调
        addCallBack(itemServiceFuture);

        // .......
        // 对多个ListenableFuture的合并，返回一个当所有Future成功时返回多个Future返回值组成的List对象。
        // 注：当其中一个Future失败或者取消的时候，会怎样？
        final ListenableFuture<List<Long>> threeServicesFutures = Futures.successfulAsList(userServiceFuture, orderServiceFuture, itemServiceFuture);

        //对于多个ListenableFuture的进行转换，返回一个新的ListenableFuture
        final ListenableFuture<Long> transform = Futures.transformAsync(threeServicesFutures, new AsyncFunction<List<Long>, Long>() {
            /**
             * 用给定的输入封装一个特定的ListenableFuture作为输出
             *
             * @param input 输入的ListenableFuture对象
             */
            @Override
            public ListenableFuture<Long> apply(@Nullable List<Long> input) {

                if (input == null || input.isEmpty()) {
                    return null;
                }
                // 这里可以对input进行复杂的处理，返回最终的一个结果
                // 比如：对用户服务，订单服务，商品服务的远程调用结果进行封装
                long result = 0;
                for (Long serviceResult : input) {
                    if (serviceResult != null) {
                        result += serviceResult;
                    }
                }
                // 立即返回一个带返回值的ListenableFuture
                return Futures.immediateFuture(result);
            }
        }, listeningExecutorService);

        // 异步调用地址服务
        ListenableFuture<Long> addressServiceFuture = listeningExecutorService.submit(new Callable<Long>() {
            @Override
            public Long call() throws Exception {
                return outerService.addressService();
            }
        });

        final ListenableFuture<List<Long>> finalFutures = Futures.successfulAsList(transform, addressServiceFuture);

        ListenableFuture<Long> finalResult = Futures.transformAsync(finalFutures, new AsyncFunction<List<Long>, Long>() {
            @Override
            public ListenableFuture<Long> apply(@Nullable List<Long> input) throws Exception {
                if (CollectionUtils.isEmpty(input)) {
                    return null;
                }
                // 这里可以对input进行复杂的处理，返回最终的一个结果
                long result = 0;
                // 比如：对上面的用户服务，订单服务，商品服务的总结果和地址服务再封装
                for (Long serviceResult : input) {
                    if (serviceResult != null) {
                        result += serviceResult;
                    }
                }
                // 立即返回一个带返回值的ListenableFuture
                return Futures.immediateFuture(result);
            }
        }, listeningExecutorService);

        // 注册回调事件
        addCallBack(finalResult);
        long end = System.currentTimeMillis();
        String time = "总调用时间是：" + (end - start) + "毫秒";
        //为什么要初始化4个容量的Map
        Map<String, Object> resultMap = new HashMap<>(3);
        resultMap.put("time", time);
        //结果
        return resultMap;
    }

    /**
     * 添加回调
     * @param listenableFuture
     */
    private void addCallBack(ListenableFuture<Long> listenableFuture) {
        Futures.addCallback(listenableFuture, new FutureCallback<Long>() {
            @Override
            public void onSuccess(@Nullable Long result) {
                System.out.printf("服务调用成功,执行结果是%s%n",result);
            }

            @Override
            public void onFailure(Throwable t) {
                System.out.printf("服务调用异常%s%n", t);
            }
        }, listeningExecutorService);
    }
}
