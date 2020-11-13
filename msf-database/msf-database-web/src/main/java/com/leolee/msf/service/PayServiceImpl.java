package com.leolee.msf.service;

import com.baomidou.dynamic.datasource.annotation.DS;
import com.leolee.msf.config.DataSourceKey;
import com.leolee.msf.config.DynamicDataSourceContextHolder;
import com.leolee.msf.entity.pay.Account;
import com.leolee.msf.dao.AccountMapper;
import com.leolee.msf.service.serviceInterface.PayService;
import io.seata.core.context.RootContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

/**
 * @ClassName PayServiceImpl
 * @Description: TODO
 * @Author LeoLee
 * @Date 2020/11/10
 * @Version V1.0
 **/
@Service("payService")
public class PayServiceImpl implements PayService {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());


    @Autowired
    private AccountMapper accountMapper;


//    @DS("pay")
    @Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRES_NEW)
    @Override
    public boolean reduceBalance(Long userId, Integer price) throws Exception {

        logger.info("======================Pay start=======================");
        DynamicDataSourceContextHolder.setDataSourceKey(DataSourceKey.PAY);
        logger.info("当前 XID: {}", RootContext.getXID());

        //检查余额
        checkBalance(userId, price);

        logger.info("开始扣减用户 {} 余额", userId);
        Account account = accountMapper.selectById(userId);
        account.setBalance(account.getBalance() - price);
        Integer record = accountMapper.updateById(account);
        logger.info("扣减用户 {} 余额结果:{}", userId, record > 0 ? "操作成功" : "扣减余额失败");

        logger.info("======================Pay end=========================");
        return record > 0;
    }


    /*
     * 功能描述: <br>
     * 〈检查账户余额是否足够支付〉
     * @Param: []
     * @Return: void
     * @Author: LeoLee
     * @Date: 2020/11/10 22:40
     */
    private void checkBalance(Long userId, Integer price) throws Exception {

        logger.info("检查用户{}余额", userId);

        Account account = accountMapper.selectById(userId);
        Integer balance = account.getBalance();

        if (balance < price) {
            logger.info("用户 {} 余额不足，当前余额:{}", userId, balance);
            throw new Exception("用户{" + userId + "}余额不足");
        }
    }
}
