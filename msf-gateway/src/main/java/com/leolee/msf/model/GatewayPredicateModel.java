package com.leolee.msf.model;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * @ClassName GatewayPredicateModel
 * @Description: 路由断言定义模型
 * @Author LeoLee
 * @Date 2020/8/20
 * @Version V1.0
 **/
public class GatewayPredicateModel {

    /**
     * 断言对应的Name
     */
    private String name;

    /**
     * 配置的断言规则
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
