package com.leolee.msf.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.event.RefreshRoutesEvent;
import org.springframework.cloud.gateway.route.RouteDefinition;
import org.springframework.cloud.gateway.route.RouteDefinitionWriter;
import org.springframework.cloud.gateway.support.NotFoundException;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.ApplicationEventPublisherAware;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

/**
 * @ClassName DynamicRouteServiceImpl
 * @Description: TODO
 * @Author LeoLee
 * @Date 2020/8/29
 * @Version V1.0
 **/
@Service
public class DynamicRouteServiceImpl implements ApplicationEventPublisherAware {


    @Autowired
    private RouteDefinitionWriter routeDefinitionWriter;

    private ApplicationEventPublisher applicationEventPublisher;


    @Override
    public void setApplicationEventPublisher(ApplicationEventPublisher applicationEventPublisher) {
        this.applicationEventPublisher = applicationEventPublisher;
    }

    /**
     * 功能描述: <br> 增加路由
     * 〈〉
     * @Param: []
     * @Return: java.lang.String
     * @Author: LeoLee
     * @Date: 2020/8/29 21:21
     */
    public String add(RouteDefinition definition) {

        routeDefinitionWriter.save(Mono.just(definition)).subscribe();
        this.applicationEventPublisher.publishEvent(new RefreshRoutesEvent(this));
        return "success";
    }


    /**
     * 功能描述: <br> 删除路由
     * 〈〉
     * @Param: [id]
     * @Return: reactor.core.publisher.Mono<org.springframework.http.ResponseEntity<java.lang.Object>>
     * @Author: LeoLee
     * @Date: 2020/8/29 21:32
     */
    public Mono<ResponseEntity<Object>> delete(String id) {

        return this.routeDefinitionWriter.delete(Mono.just(id)).then(Mono.defer(() -> {
            return Mono.just(ResponseEntity.ok().build());
        })).onErrorResume((t) -> {
            return t instanceof NotFoundException;
        }, (t) -> {
            return Mono.just(ResponseEntity.notFound().build());
        });
    }


    /**
     * 功能描述: <br> 更新路由
     * 〈〉
     * @Param: [definition]
     * @Return: java.lang.String
     * @Author: LeoLee
     * @Date: 2020/8/29 21:25
     */
    public String update(RouteDefinition definition) {
        try {
            delete(definition.getId());
        } catch (Exception e) {
            return "update fail,not find route,routeId:" + definition.getId();
        }

        try {
            return this.add(definition);
        } catch (Exception e) {
            e.printStackTrace();
            return "update route fail";
        }
    }
}
