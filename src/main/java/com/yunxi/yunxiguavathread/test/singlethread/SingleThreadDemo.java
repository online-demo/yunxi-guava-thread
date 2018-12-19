package com.yunxi.yunxiguavathread.test.singlethread;

import com.yunxi.yunxiguavathread.test.service.OuterService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.Map;

/**
 * @Author: 无双老师
 * @Date: 2018/10/27 09:45
 * @Description: 单线程操作演示
 */
@RestController
public class SingleThreadDemo {
    @Resource
    private OuterService outerService;

    /**
     * 测试代码
     */
    @RequestMapping("/test/single")
    public Map<String, Object> test () {
        //开始调用的时间
        long start = System.currentTimeMillis();
        //同步调用用户服务
        long userServiceResult = outerService.userService();
        //同步调用订单服务
        long orderServiceResult = outerService.orderService();
        //同步调用商品服务
        long itemServiceResult = outerService.itemService();
        //结束调用的时间
        long end = System.currentTimeMillis();
        String time = "总调用时间是：" + (end - start) + "毫秒";
        //计算结果
        long result = userServiceResult + orderServiceResult + itemServiceResult;
        //为什么要初始化4个容量的Map
        Map<String, Object> resultMap = new HashMap<>(4);
        resultMap.put("time", time);
        resultMap.put("result", result);
        //结果
        return resultMap;
    }
}
