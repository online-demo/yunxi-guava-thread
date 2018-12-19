package com.yunxi.yunxiguavathread.test.service;

import org.springframework.stereotype.Service;

/**
 * @Author: 无双老师
 * @Date: 2018/10/27 09:33
 * @Description: 模拟一个耗时的服务 如：RPC或者HTTP请求
 */
@Service
public class OuterService {
    /**
     * 用户服务
     */
    public long userService() {
        System.out.println("用户服务开始执行");
        try {
            //模拟耗时1000ms的用户服务
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println("用户服务执行结束");
        //假设用户服务返回值=1
        return 1;
    }

    /**
     * 订单服务
     */
    public long orderService() {
        System.out.println("订单服务开始执行");
        try {
            //模拟耗时2000ms的订单服务
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println("订单服务执行结束");
        //假设订单服务返回值=3
        return 3;
    }

    /**
     * 商品服务
     */
    public long itemService() {
        System.out.println("商品服务开始执行");
        try {
            //模拟耗时3000ms的商品服务
            Thread.sleep(3000);
            //模拟调用超时或者服务异常
            // throw new RuntimeException("Oh My God, OrderService Exception!!!");
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println("商品服务执行结束");
        //假设商品服务返回值=5
        return 5;
    }

    /**
     * 地址服务
     */
    public long addressService() {
        System.out.println("地址服务开始执行");
        try {
            //模拟耗时5000ms的地址服务
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println("地址服务执行结束");
        //假设地址服务返回值=7
        return 7;
    }
}
