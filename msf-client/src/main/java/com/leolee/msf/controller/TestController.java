package com.leolee.msf.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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

    @RequestMapping(value = "/hello", method = RequestMethod.GET)
    public String getConfigHello() {
        logger.info("client log");
        return hello;
    }

    @RequestMapping(value = "/value", method = RequestMethod.GET)
    public String getValue() {
        try {
            Thread.sleep(5000L);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        logger.info("Hystrix熔断测试");
        return "111";
    }

}
