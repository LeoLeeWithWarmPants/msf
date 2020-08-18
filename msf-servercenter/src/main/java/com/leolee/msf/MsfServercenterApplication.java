package com.leolee.msf;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;import org.springframework.cloud.netflix.eureka.server.EnableEurekaServer;

@EnableEurekaServer
@SpringBootApplication
public class MsfServercenterApplication {

    public static void main(String[] args) {
        SpringApplication.run(MsfServercenterApplication.class, args);
    }

}
