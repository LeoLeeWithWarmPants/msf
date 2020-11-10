package com.leolee.msf.controller;

import com.google.gson.Gson;
import com.leolee.msf.annotation.SysLog;
import com.leolee.msf.entity.TestEntity;
import com.leolee.msf.service.serviceInterface.TestService;
import com.leolee.msf.sysEnum.SysLogEnum;
import com.leolee.msf.utils.RedisUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/test")
public class TestController {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Value("${hello}")
    private String hello;

    @Autowired
    private Gson gson;

    @Autowired
    private RedisUtils redisUtils;


    @Autowired
    private TestService testService;


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


    @SysLog(value = "这是一个日志记录", type = SysLogEnum.BUSSINESS)
    @RequestMapping(value = "/list", method = RequestMethod.GET)
    public String getTestList() {
        return gson.toJson(testService.getTestList());
    }


    @SysLog(value = "测试mybatis-plus雪花算法生成id", type = SysLogEnum.BUSSINESS)
    @RequestMapping(value = "/user", method = RequestMethod.PUT)
    public String insertTest(@RequestBody TestEntity test) {
        return gson.toJson(testService.insertTest(test));
    }


    @SysLog(value = "测试自定义sql", type = SysLogEnum.BUSSINESS)
    @RequestMapping(value = "/allList", method = RequestMethod.GET)
    public String getAllList() {
        List<TestEntity> list = testService.selectAll();
        list.add(testService.selectByName("LeoLee").get(0));
        return gson.toJson(list);
    }

}
