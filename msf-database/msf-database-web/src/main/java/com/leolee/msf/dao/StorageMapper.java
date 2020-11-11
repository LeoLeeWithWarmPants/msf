package com.leolee.msf.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.leolee.msf.entity.storage.Product;
import org.apache.ibatis.annotations.Mapper;

/**
 * @ClassName StorageMapper
 * @Description: TODO
 * @Author LeoLee
 * @Date 2020/11/10
 * @Version V1.0
 **/
@Mapper
public interface StorageMapper extends BaseMapper<Product> {

}
