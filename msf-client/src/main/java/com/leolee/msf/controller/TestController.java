package com.leolee.msf.controller;

import com.leolee.msf.utils.RedisUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/test")
public class TestController {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Value("${hello}")
    private String hello;

    @Autowired
    private RedisUtils redisUtils;

    @RequestMapping(value = "/hello", method = RequestMethod.GET)
    public String getConfigHello() {
        logger.info("client log");
        return hello;
    }

    @RequestMapping(value = "/value", method = RequestMethod.GET)
    public String getValue() {
        try {
            Thread.sleep(4000L);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        logger.info("Hystrix熔断测试");
        return "111";
    }


    @RequestMapping(value = "/redis/value", method = RequestMethod.GET)
    public String getRedisValue(String key) {
        return redisUtils.get(key).toString();
    }


    @RequestMapping(value = "/redis/value", method = RequestMethod.POST)
    public boolean setRedisValue(String key, String value) {
        return redisUtils.set(key, value);
    }

}
