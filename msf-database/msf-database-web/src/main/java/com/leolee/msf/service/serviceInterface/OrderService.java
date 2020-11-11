package com.leolee.msf.service.serviceInterface;

import com.leolee.msf.entity.OperationResponse;
import com.leolee.msf.entity.order.PlaceOrderRequestVO;

public interface OrderService {


    /*
     * 功能描述: <br>
     * 〈下单订购〉
     * @Param: [placeOrderRequestVO {
     *  userId 订购人
     *  productId 产品id
     *  price 产品价格
     * }]
     * @Return: com.leolee.msf.entity.OperationResponse
     * @Author: LeoLee
     * @Date: 2020/11/10 21:29
     */
    OperationResponse placeOrder(PlaceOrderRequestVO placeOrderRequestVO) throws Exception;
}
