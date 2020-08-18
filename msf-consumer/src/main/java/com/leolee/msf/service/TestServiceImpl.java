package com.leolee.msf.service;


import com.leolee.msf.feignInterface.TestClinet;
import com.leolee.msf.service.serviceInterface.TestService;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @ClassName TestServiceImpl
 * @Description: TODO
 * @Author LeoLee
 * @Date 2020/8/18
 * @Version V1.0
 **/
@Service("loginService")
public class TestServiceImpl implements TestService {

    @Autowired
    private TestClinet testClinet;

    @HystrixCommand(fallbackMethod = "fallback")
    @Override
    public String getFeignValue() {
        return testClinet.feignValue();
    }

    public String fallback() {
        return "Hystrix";
    }
}
