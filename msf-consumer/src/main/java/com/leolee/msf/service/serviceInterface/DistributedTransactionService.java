package com.leolee.msf.service.serviceInterface;

import java.util.Map;

public interface DistributedTransactionService {


    /*
     * 功能描述: <br>
     * 〈获取抢购信息〉
     * @Param: [productId]
     * @Return: java.util.Map<java.lang.String,java.lang.Object>
     * @Author: LeoLee
     * @Date: 2020/11/20 10:50
     */
    public Map<String, Object> getProductQuantity(String productId);

    /*
     * 功能描述: <br>
     * 〈下单抢购〉
     * @Param: [productId]
     * @Return: boolean
     * @Author: LeoLee
     * @Date: 2020/11/20 10:50
     */
    public boolean orderByProductId(String productId);

}
