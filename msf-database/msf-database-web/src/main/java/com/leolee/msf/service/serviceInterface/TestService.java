package com.leolee.msf.service.serviceInterface;



import com.leolee.msf.entity.TestEntity;

import java.util.List;

public interface TestService {

    public List<TestEntity> getTestList();

    public TestEntity insertTest(TestEntity test);

    public List<TestEntity> selectAll();

    public List<TestEntity> selectByName(String name);
}
