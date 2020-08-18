package com.leolee.msf;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@EnableDiscoveryClient
@SpringBootApplication
public class MsfClientApplication {

    public static void main(String[] args) {
        SpringApplication.run(MsfClientApplication.class, args);
    }

}
