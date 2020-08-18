package com.leolee.msf;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;import org.springframework.cloud.config.server.EnableConfigServer;

@EnableConfigServer
@SpringBootApplication
public class MsfConfigcenterApplication {

    public static void main(String[] args) {
        SpringApplication.run(MsfConfigcenterApplication.class, args);
    }

}
