package com.leolee.msf.controller;

import com.leolee.msf.entity.OperationResponse;
import com.leolee.msf.entity.order.PlaceOrderRequestVO;
import com.leolee.msf.service.serviceInterface.OrderService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @ClassName OrderController
 * @Description: 订单
 * @Author LeoLee
 * @Date 2020/11/10
 * @Version V1.0
 **/
@RestController
@RequestMapping("/order")
public class OrderController {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());


    @Autowired
    private OrderService orderService;


    /*
     * 功能描述: <br>
     * 〈下单〉
     * @Param: [placeOrderRequestVO]
     * @Return: com.leolee.msf.entity.OperationResponse
     * @Author: LeoLee
     * @Date: 2020/11/10 21:27
     */
    @RequestMapping(value = "/placeOrder")
    public OperationResponse placeOrder(@RequestBody PlaceOrderRequestVO placeOrderRequestVO) throws Exception {

        logger.info("=======================================================");
        logger.info("下单订购请求:userId:[" + placeOrderRequestVO.getUserId() + "],productId[" + placeOrderRequestVO.getProductId() + "],price[" + placeOrderRequestVO.getPrice() + "]");

        return orderService.placeOrder(placeOrderRequestVO);
    }
}
