package com.leolee.msf.service.serviceInterface;

public interface StorageService {

    /*
     * 功能描述: <br>
     * 〈扣减库存〉
     * @Param: [productId 产品id, amount数量]
     * @Return: boolean
     * @Author: LeoLee
     * @Date: 2020/11/10 22:02
     */
    public boolean reduceStock(Long productId, Integer amount) throws Exception;
}
