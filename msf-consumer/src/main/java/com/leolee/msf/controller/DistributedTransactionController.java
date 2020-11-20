package com.leolee.msf.controller;

import com.google.gson.Gson;
import com.leolee.msf.service.serviceInterface.DistributedTransactionService;
import com.leolee.msf.utils.RedisUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

/**
 * @ClassName DistributedTransactionController
 * @Description: 分布式事务测试
 * 产品下单为例
 * @Author LeoLee
 * @Date 2020/11/20
 * @Version V1.0
 **/
@RestController
@RequestMapping("/product")
public class DistributedTransactionController {

    //限购一件
    private final int num = 1;

    @Autowired
    Gson gson;

    @Autowired
    DistributedTransactionService distributedTransactionService;

    @Autowired
    RedisUtils redisUtils;



    @GetMapping("/testRedis")
    public void testRedis() {
        redisUtils.set("aaa", "123");
        redisUtils.get("aaa");
    }


    @GetMapping("/{id}")
    public String productQuantity(@PathVariable(name = "id")String productId) {

        return gson.toJson(distributedTransactionService.getProductQuantity(productId));
    }


    @GetMapping("/order/{id}")
    public String order(@PathVariable(name = "id")String productId) {

        boolean b = distributedTransactionService.orderByProductId(productId);
        Map<String, Object> resultMap = distributedTransactionService.getProductQuantity(productId);
        if (b) {
            resultMap.put("msg", "抢购成功");
            resultMap.put("code", true);
        } else {
            resultMap.put("msg", "抢购成功");
            resultMap.put("code", false);
        }
        return gson.toJson(resultMap);
    }

}
