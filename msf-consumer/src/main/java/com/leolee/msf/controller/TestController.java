package com.leolee.msf.controller;

import com.leolee.msf.annotation.SysLog;import com.leolee.msf.feignInterface.TestClinet;
import com.leolee.msf.service.serviceInterface.TestService;
import com.leolee.msf.sysEnum.SysLogEnum;import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.loadbalancer.LoadBalancerClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

@RestController
@RequestMapping("/test")
public class TestController {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    LoadBalancerClient loadBalancerClient;

    @Autowired
    RestTemplate restTemplate;

    @Value("${hello}")
    private String hello;

    @Autowired
    private TestClinet testClinet;

    @Autowired
    private TestService testService;


    /**
     * 功能描述: <br> 远程消费eureka-client
     * 〈〉使用了ribbon的负载之后，该方法无法使用
     * @Param: []
     * @Return: java.lang.String
     * @Author: LeoLee
     * @Date: 2020/8/14 16:28
     */
    @RequestMapping(value = "/hello", method = RequestMethod.GET)
    public String getrRestTemplateHello() {
        ServiceInstance serviceInstance = loadBalancerClient.choose("eureka-client");
        String url = "http://" + serviceInstance.getHost() + ":" + serviceInstance.getPort() + "/test/hello";
        System.out.println(url);
        return restTemplate.getForObject(url, String.class);
    }

    /**
     * 功能描述: <br> ribbon负载的方式远程消费eureka-client
     * 〈〉
     * @Param: []
     * @Return: java.lang.String
     * @Author: LeoLee
     * @Date: 2020/8/14 16:30
     */
    @RequestMapping(value = "/hello2", method = RequestMethod.GET)
    public String getRibbonHello() {
        return restTemplate.getForObject("http://eureka-client/test/hello", String.class);
    }

    /**
     * 功能描述: <br> feign远程调用
     * 〈〉
     * @Param: []
     * @Return: java.lang.String
     * @Author: LeoLee
     * @Date: 2020/8/14 17:42
     */
    @SysLog(value = "这是一个日志记录", type = SysLogEnum.BUSSINESS)
    @RequestMapping(value = "/hello3", method = RequestMethod.GET)
    public String getFeignHello() {
        logger.info("日志数据拉");
        return testClinet.feignHello();
    }

    /**
     * 功能描述: <br> Hystrix验证
     * 〈〉
     * @Param: []
     * @Return: java.lang.String
     * @Author: LeoLee
     * @Date: 2020/8/18 23:38
     */
    @RequestMapping(value = "/value", method = RequestMethod.GET)
    public String getFeignValue() {
        return testService.getFeignValue();
    }

}
