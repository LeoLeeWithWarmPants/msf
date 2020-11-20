package com.leolee.msf.config;

import com.google.gson.Gson;
import com.leolee.msf.utils.RedisUtils;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

/**
 * 功能描述: <br> 注入bean对象
 * 〈〉
 * @Author: LeoLee
 * @Date: 2020/8/17 17:10
 */
@Component
public class BeanConfig {

    @Bean
    public Gson gson(){
        return new Gson();
    }


    @Bean
    public RedisUtils redisUtils() {
        return new RedisUtils();
    }
}
