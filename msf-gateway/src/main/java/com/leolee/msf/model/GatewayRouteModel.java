package com.leolee.msf.model;

import java.util.ArrayList;
import java.util.List;

/**
 * @ClassName GatewayRouteModel
 * @Description: Gateway的路由定义模型
 * @Author LeoLee
 * @Date 2020/8/20
 * @Version V1.0
 **/
public class GatewayRouteModel {

    /**
     * 路由的Id
     */
    private String id;

    /**
     * 路由断言集合配置
     */
    private List<GatewayPredicateModel> predicates = new ArrayList<>();

    /**
     * 路由过滤器集合配置
     */
    private List<GatewayPredicateModel> filters = new ArrayList<>();

    /**
     * 路由规则转发的目标uri
     */
    private String uri;

    /**
     * 路由执行的顺序
     */
    private int order = 0;

    /**
     * 断言集合json字符串
     */
    private String predicatesJson;

    /**
     * 路由过滤器json字符串
     */
    private String filtersJson;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public List<GatewayPredicateModel> getPredicates() {
        return predicates;
    }

    public void setPredicates(List<GatewayPredicateModel> predicates) {
        this.predicates = predicates;
    }

    public List<GatewayPredicateModel> getFilters() {
        return filters;
    }

    public void setFilters(List<GatewayPredicateModel> filters) {
        this.filters = filters;
    }

    public String getUri() {
        return uri;
    }

    public void setUri(String uri) {
        this.uri = uri;
    }

    public int getOrder() {
        return order;
    }

    public void setOrder(int order) {
        this.order = order;
    }

    public String getPredicatesJson() {
        return predicatesJson;
    }

    public void setPredicatesJson(String predicatesJson) {
        this.predicatesJson = predicatesJson;
    }

    public String getFiltersJson() {
        return filtersJson;
    }

    public void setFiltersJson(String filtersJson) {
        this.filtersJson = filtersJson;
    }
}
