package com.yunxi.yunxiguavathread.test.common;

import com.google.common.util.concurrent.ListeningExecutorService;
import com.google.common.util.concurrent.MoreExecutors;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import java.util.concurrent.Executors;

/**
 * 公用的线程池
 */
@Component
public class ListeningExecutors {

    @Bean
    public ListeningExecutorService createListeningExecutorService() {
        // 创建线程池
        ListeningExecutorService listeningExecutorService = MoreExecutors.
                listeningDecorator(Executors.newFixedThreadPool(10));

        return listeningExecutorService;
    }
}