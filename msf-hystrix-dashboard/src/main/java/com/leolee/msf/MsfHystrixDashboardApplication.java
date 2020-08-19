package com.leolee.msf;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;import org.springframework.cloud.netflix.hystrix.dashboard.EnableHystrixDashboard;

@EnableHystrixDashboard
@SpringBootApplication
public class MsfHystrixDashboardApplication {

    public static void main(String[] args) {
        SpringApplication.run(MsfHystrixDashboardApplication.class, args);
    }

}
