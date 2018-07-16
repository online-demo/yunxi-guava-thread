package com.yunxi.yunxiguavathread.test.common;

import com.google.common.util.concurrent.FutureCallback;

/**
 * ListenableFuture回调任务
 */
public class FutureCallBackTask implements FutureCallback<Object> {

    /**
     * 成功的回调
     * @param result
     */
    @Override
    public void onSuccess(Object result) {
        //执行回调函数
        System.out.println("进入正确的回调函数");
        //得到任务执行的结果
        System.out.printf("任务执行的结果是：%s%n", result);
    }

    /**
     * 失败的回调
     * @param thrown
     */
    @Override
    public void onFailure(Throwable thrown) {
        System.out.println("进入错误的回调函数");
        System.out.printf("系统出错了，错误原因是：%s%n", thrown.getMessage());
    }
}
