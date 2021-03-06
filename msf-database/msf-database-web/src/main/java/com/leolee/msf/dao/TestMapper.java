package com.leolee.msf.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.leolee.msf.entity.TestEntity;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface TestMapper extends BaseMapper<TestEntity> {

    public List<TestEntity> selectAll();

    public List<TestEntity> selectByName(@Param("name") String name);

}
