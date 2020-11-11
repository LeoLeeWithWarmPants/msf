package com.leolee.msf.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.leolee.msf.entity.order.Order;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface OrderMapper extends BaseMapper<Order> {

}
