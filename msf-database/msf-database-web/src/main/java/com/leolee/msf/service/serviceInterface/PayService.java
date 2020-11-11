package com.leolee.msf.service.serviceInterface;

public interface PayService {


    /*
     * 功能描述: <br>
     * 〈扣除账户金额〉
     * @Param: [userId, price]
     * @Return: boolean
     * @Author: LeoLee
     * @Date: 2020/11/10 22:38
     */
    boolean reduceBalance(Long userId, Integer price) throws Exception;
}
