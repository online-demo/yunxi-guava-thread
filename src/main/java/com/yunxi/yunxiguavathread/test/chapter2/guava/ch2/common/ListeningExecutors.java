package com.yunxi.yunxiguavathread.test.chapter2.guava.ch2.common;

import com.google.common.util.concurrent.ListeningExecutorService;
import com.google.common.util.concurrent.MoreExecutors;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import java.util.concurrent.Executors;

/**
 * @Author: 无双老师
 * @Date: 2020年02月26日19:53:49
 * @Description: 公用的线程池
 */
@Component
public class ListeningExecutors {

    @Bean
    public ListeningExecutorService createListeningExecutorService() {
        // 创建线程池
        return MoreExecutors.listeningDecorator(Executors.newFixedThreadPool(10));
    }
}