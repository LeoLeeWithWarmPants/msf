package com.leolee.msf.feignInterface;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@FeignClient("eureka-client")
public interface TestClinet {

    @RequestMapping(value = "/test/hello", method = RequestMethod.GET)
    public String feignHello();
}
