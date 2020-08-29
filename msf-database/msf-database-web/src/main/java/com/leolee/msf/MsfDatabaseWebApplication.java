package com.leolee.msf;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

/**
 * @ClassName MsfDatabaseWebApplication
 * @Description: TODO
 * @Author LeoLee
 * @Date 2020/8/29
 * @Version V1.0
 **/
@MapperScan("com.leolee.msf.dao")
@EnableDiscoveryClient
@SpringBootApplication
public class MsfDatabaseWebApplication {

    public static void main(String[] args) {
        SpringApplication.run(MsfDatabaseWebApplication.class, args);
    }
}
