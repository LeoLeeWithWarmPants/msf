package com.leolee.msf;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@MapperScan("com.leolee.msf.dao")
@EnableDiscoveryClient
@SpringBootApplication
public class MsfProducerWebApplication {

    public static void main(String[] args) {
        SpringApplication.run(MsfProducerWebApplication.class, args);
    }

}
