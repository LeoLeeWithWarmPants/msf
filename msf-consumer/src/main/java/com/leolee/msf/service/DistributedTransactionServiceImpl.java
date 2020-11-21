package com.leolee.msf.service;

import com.leolee.msf.service.serviceInterface.DistributedTransactionService;
import com.leolee.msf.utils.redisLock.RedisLockUtil;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

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

    @Autowired
    RedissonClient redissonClient;



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


    //===========================================方案1========================================================
    /*该方案存在问题
      1.当前锁过期之后，高并发情况下多个客户端同时执行getAndSet方法，那么虽然最终只有一个客户端可以加锁，虽然其他没有获得锁的请求没有成功执行业务操作，但是覆盖了锁的value时间戳
      2.虽然这样为了处理死锁问题，由于存在一个客户端请求在锁失效前还是没有执行完毕，甚至计算库存是否>0都没有完成，下一个客户端请求的时候，判断前一个锁已经失效，覆盖了前一个锁，所以两个线程间还是会出现超卖的问题。
    */
    @Override
    public boolean orderByProductId(String productId) {

        //加分布式锁
        //value设置为10秒后
        String cuurentTimeMills = String.valueOf(System.currentTimeMillis() + 10000);
        if (!redisLockUtil.redisLock(productId, cuurentTimeMills)) {
            return false;
        }

        boolean result = false;
        try {
            //=======================执行业务逻辑=========================

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

        return result;
    }

    //===========================================方案2（另起线程续期）========================================================
    public boolean orderByProductId2(String productId) {

        //加分布式锁
        String uuid = UUID.randomUUID().toString();
        redisLockUtil.redisLock(productId, uuid, 5000);

        boolean result = false;
        try {
            //=======================执行业务逻辑=========================
            //判断是否存在该商品
            if (checkExist(productId)) {
                try {
                    //模拟数据库操作
                    Thread.sleep(4000);
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
            redisLockUtil.newDeleteLock(productId, uuid);
        }


        return result;
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

    //===========================================方案3（redisson）========================================================
    private static final String PRODUCT_LOCK_TITLE = "product_";
    public boolean orderByProductId3(String productId) {

        //redisson加锁
        RLock lock = redissonClient.getLock(PRODUCT_LOCK_TITLE + productId);
        //获取锁，直到该锁释放，其他待获取锁的线程，一直处于阻塞状态
        //缺点：当前服务意外宕机，redis锁将无法得到释放
//        lock.lock();
        // 加锁以后10秒钟自动解锁
        // 无需调用unlock方法手动解锁
//        lock.lock(10, TimeUnit.SECONDS);
        // 尝试加锁，最多等待100秒，上锁以后10秒自动解锁
        boolean res = false;
        try {
            res = lock.tryLock(100, 10, TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        boolean result = false;
        try {
            //=======================执行业务逻辑=========================
            //判断是否存在该商品
            if (checkExist(productId) && res) {
                try {
                    //模拟数据库操作
                    Thread.sleep(1000*2);
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
            lock.unlock();
        }
        return result;
    }

    public boolean orderByProductId4(String productId) {

        //redisson加锁
        RLock lock = redissonClient.getLock(PRODUCT_LOCK_TITLE + productId);
        Future<Boolean> res = lock.tryLockAsync(30, 10, TimeUnit.SECONDS);

        //此时获取锁的行为并不会阻塞代码，可以执行其他业务逻辑
        //do something......

        boolean result = false;
        try {
            //=======================执行业务逻辑=========================
            //判断是否存在该商品
            if (checkExist(productId) && res.get()) {
                try {
                    //模拟数据库操作
                    Thread.sleep(1000*2);
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
            lock.unlock();
        }
        return result;
    }


}
