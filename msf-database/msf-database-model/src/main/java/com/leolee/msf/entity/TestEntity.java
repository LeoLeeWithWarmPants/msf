package com.leolee.msf.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

/**
 * @ClassName TestEntity
 * @Description: 测试实体类
 * @Author LeoLee
 * @Date 2020/8/27
 * @Version V1.0
 **/
@Data
@TableName("test")
public class TestEntity {

    private Long id;

    private String name;
}
