package com.leolee.msf.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
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

    //雪花算法生成全局唯一id，适用于分布式系统的多数据源
    @TableId(value = "id", type = IdType.ASSIGN_ID)
    private Long id;

    private String name;
}
