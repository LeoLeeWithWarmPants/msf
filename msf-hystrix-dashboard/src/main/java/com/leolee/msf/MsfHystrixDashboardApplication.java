package com.leolee.msf;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;import org.springframework.cloud.client.discovery.EnableDiscoveryClient;import org.springframework.cloud.netflix.hystrix.dashboard.EnableHystrixDashboard;import org.springframework.cloud.netflix.turbine.EnableTurbine;

@EnableTurbine // 开启turbine监控数据聚合
@EnableHystrixDashboard // 开启hystrix仪表盘
@EnableDiscoveryClient
@SpringBootApplication
public class MsfHystrixDashboardApplication {

    public static void main(String[] args) {
        SpringApplication.run(MsfHystrixDashboardApplication.class, args);
    }

}
