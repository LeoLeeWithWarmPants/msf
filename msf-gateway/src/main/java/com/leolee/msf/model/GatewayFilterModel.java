package com.leolee.msf.model;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * @ClassName GatewayFilterModel
 * @Description: 过滤器定义模型
 * @Author LeoLee
 * @Date 2020/8/20
 * @Version V1.0
 **/
public class GatewayFilterModel {

    /**
     * Filter Name
     */
    private String name;

    /**
     * 对应的路由规则
     */
    private Map<String, String> args = new LinkedHashMap<>();


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Map<String, String> getArgs() {
        return args;
    }

    public void setArgs(Map<String, String> args) {
        this.args = args;
    }
}
