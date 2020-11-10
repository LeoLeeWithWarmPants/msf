package com.leolee.msf.service;

import com.baomidou.dynamic.datasource.annotation.DS;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.leolee.msf.dao.TestMapper;
import com.leolee.msf.entity.TestEntity;
import com.leolee.msf.service.serviceInterface.TestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @ClassName TestServiceImpl
 * @Description: TODO
 * @Author LeoLee
 * @Date 2020/8/27
 * @Version V1.0
 **/
@Service("testService")
@DS("master")//使用 @DS 切换数据源 @DS 可以注解在方法上和类上，同时存在方法注解优先于类上注解。
public class TestServiceImpl extends ServiceImpl<TestMapper, TestEntity> implements TestService {

    @Autowired
    private TestMapper testMapper;

    @Override
    public List<TestEntity> getTestList() {
        return testMapper.selectList(null);
    }


    @Override
    public TestEntity insertTest(TestEntity test) {
        int i = testMapper.insert(test);
        if (i > 0) {
            return test;
        } else {
            return test;
        }
    }


    @Override
    public List<TestEntity> selectAll() {
        return testMapper.selectAll();
    }

    @DS("db2")
    @Override
    public List<TestEntity> selectByName(String name) {
        return testMapper.selectByName(name);
    }
}
