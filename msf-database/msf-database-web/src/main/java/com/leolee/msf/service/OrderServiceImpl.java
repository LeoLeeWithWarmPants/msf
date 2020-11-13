package com.leolee.msf.service;

import com.baomidou.dynamic.datasource.annotation.DS;
import com.leolee.msf.config.DataSourceKey;
import com.leolee.msf.config.DynamicDataSourceContextHolder;
import com.leolee.msf.dao.OrderMapper;
import com.leolee.msf.entity.OperationResponse;
import com.leolee.msf.entity.order.Order;
import com.leolee.msf.entity.order.OrderStatus;
import com.leolee.msf.entity.order.PlaceOrderRequestVO;
import com.leolee.msf.service.serviceInterface.OrderService;
import com.leolee.msf.service.serviceInterface.PayService;
import com.leolee.msf.service.serviceInterface.StorageService;
import io.seata.core.context.RootContext;
import io.seata.spring.annotation.GlobalTransactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @ClassName OrderServiceImpl
 * @Description: TODO
 * @Author LeoLee
 * @Date 2020/11/10
 * @Version V1.0
 **/
@Service("orderService")
public class OrderServiceImpl implements OrderService {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());


    @Autowired
    private OrderMapper orderMapper;

    @Autowired
    private StorageService storageService;

    @Autowired
    private PayService payService;

    @GlobalTransactional
//    @DS("order")
    @Override
    public OperationResponse placeOrder(PlaceOrderRequestVO placeOrderRequestVO) throws Exception {

        logger.info("=====================Order start===================");
        DynamicDataSourceContextHolder.setDataSourceKey(DataSourceKey.ORDER);
        logger.info("当前 XID: {}", RootContext.getXID());

        //每人限购一件嗷
        final Integer amount = 1;
        final Integer price = placeOrderRequestVO.getPrice();

        //创建订单
        Order order = Order.builder()
                .productId(placeOrderRequestVO.getProductId())
                .userId(placeOrderRequestVO.getUserId())
                .payAmount(placeOrderRequestVO.getPrice())
                .status(OrderStatus.INIT)
                .build();

        int insertOrderResult = orderMapper.insert(order);
        logger.info("保存订单{}", insertOrderResult > 0 ? "succeed" : "failed");

        //扣减库存
        boolean operationStorageResult = storageService.reduceStock(placeOrderRequestVO.getProductId(), amount);

        //扣减用户余额
        boolean operationBalanceResult = payService.reduceBalance(placeOrderRequestVO.getUserId(), price);

        logger.info("=====================Order end====================");

        //更新订单\
        DynamicDataSourceContextHolder.setDataSourceKey(DataSourceKey.ORDER);
        order.setStatus(OrderStatus.SUCCESS);
        Integer updateOrderRecord = orderMapper.updateById(order);
        logger.info("更新订单:{} {}", order.getId(), updateOrderRecord > 0 ? "成功" : "失败");

        return OperationResponse.builder()
                .success(operationStorageResult && operationBalanceResult)
                .build();
    }
}
