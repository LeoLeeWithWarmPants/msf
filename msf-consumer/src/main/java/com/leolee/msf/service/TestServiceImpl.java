package com.leolee.msf.service;


import com.leolee.msf.feignInterface.TestClinet;
import com.leolee.msf.service.serviceInterface.TestService;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixProperty;
import com.netflix.hystrix.contrib.javanica.conf.HystrixPropertiesManager;
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

    @HystrixCommand(fallbackMethod = "fallback",
            commandProperties = {
//                    @HystrixProperty(name = HystrixPropertiesManager.EXECUTION_ISOLATION_THREAD_TIMEOUT_IN_MILLISECONDS, value = "6000")
            })
    @Override
    public String getFeignValue() {
        return testClinet.feignValue();
    }

    public String fallback() {
        return "Hystrix";
    }
}
