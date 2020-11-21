package com.leolee.msf.config;

import org.redisson.Redisson;
import org.redisson.api.RedissonClient;
import org.redisson.config.Config;
import org.redisson.config.TransportMode;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @ClassName RedissonConfig
 * @Description: TODO
 * @Author LeoLee
 * @Date 2020/11/21
 * @Version V1.0
 **/
@Configuration
public class RedissonConfig {

    @Bean
    public RedissonClient config() {
        Config config = new Config();
        config.setTransportMode(TransportMode.NIO);
        config.useSingleServer().setAddress("redis://127.0.0.1:6379").setPassword("liyalong"); //可以用"rediss://"来启用SSL连接
        config.setLockWatchdogTimeout(10*1000);
        RedissonClient redisson = Redisson.create(config);
        return redisson;
    }

}
