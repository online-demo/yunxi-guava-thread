package com.yunxi.yunxiguavathread.test.listenablefuture;

import com.google.common.util.concurrent.FutureCallback;

/**
 * ListenableFuture回调任务
 */
public class FutureCallBackTask implements FutureCallback<Boolean> {

    /**
     * 成功的回调
     * @param result
     */
    @Override
    public void onSuccess(Boolean result) {
        //执行回调函数
        System.out.println("进入回调函数");
        //得到任务执行的结果
        System.out.println("任务执行成功了，执行结果=" + result);
    }

    /**
     * 失败的回调
     * @param t
     */
    @Override
    public void onFailure(Throwable t) {
        System.out.println("出错了");
    }
}
