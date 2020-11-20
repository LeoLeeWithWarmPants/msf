package com.leolee.msf.service;

import com.leolee.msf.service.serviceInterface.DistributedTransactionService;
import com.leolee.msf.utils.RedisLockUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * @ClassName DistributedTransactionServiceImpl
 * @Description: TODO
 * @Author LeoLee
 * @Date 2020/11/20
 * @Version V1.0
 **/
@Service("distributedTransactionService")
public class DistributedTransactionServiceImpl implements DistributedTransactionService {

    @Autowired
    RedisTemplate redisTemplate;

    @Autowired
    RedisLockUtil redisLockUtil;



    //库存 key-productId value-数量
    private HashMap<String, Long> productStockQuantity;

    //订单 key-uuid value-productId
    private HashMap<String, String> order;

    //总量 key-productId value-数量
    private HashMap<String, Long> total;

    //抢购商品id写死
    private final String productId = "123";


    public DistributedTransactionServiceImpl() {
        this.total = new HashMap<String, Long>();
        this.productStockQuantity = new HashMap<String, Long>();
        this.total.put(productId, 10000l);
        this.productStockQuantity.put(productId, 10000l);
        this.order = new HashMap<String, String>();
    }


    @Override
    public Map<String, Object> getProductQuantity(String productId) {
        Map<String, Object> info = new HashMap<>();
        info.put("productId", productId);
        info.put("soldOut", order.size());//已售
        info.put("total", total.get(productId));
        info.put("stock", productStockQuantity.get(productId));
        return info;
    }

    @Override
    public boolean orderByProductId(String productId) {

        //加分布式锁
        //value设置为10秒后
        String cuurentTimeMills = String.valueOf(System.currentTimeMillis() + 10000);
        if (!redisLockUtil.redisLock(productId, cuurentTimeMills)) {
            return false;
        }

        try {
            //=======================执行业务逻辑=========================
            boolean result = false;
            //判断是否存在该商品
            if (checkExist(productId)) {
                try {
                    //模拟数据库操作
                    Thread.sleep(1000);
                    //产生订单，扣减库存
                    order.put(UUID.randomUUID().toString(), productId);
                    productStockQuantity.put(productId, productStockQuantity.get(productId) - 1);
                    result = true;
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            //=======================业务逻辑结束=========================
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            //解锁
            redisLockUtil.deleteLock(productId, cuurentTimeMills);
        }


        return false;
    }

    /*
     * 功能描述: <br>
     * 〈检查商品是否存在，是否有库存〉
     * @Param: [productId]
     * @Return: boolean
     * @Author: LeoLee
     * @Date: 2020/11/20 10:52
     */
    private boolean checkExist(String productId) {

        return total.containsKey(productId) && productStockQuantity.containsKey(productId) && productStockQuantity.get(productId) > 0 ? true : false;
    }
}
