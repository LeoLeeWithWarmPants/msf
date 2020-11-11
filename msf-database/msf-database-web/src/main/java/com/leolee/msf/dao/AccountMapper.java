package com.leolee.msf.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.leolee.msf.entity.pay.Account;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface AccountMapper extends BaseMapper<Account> {
}
