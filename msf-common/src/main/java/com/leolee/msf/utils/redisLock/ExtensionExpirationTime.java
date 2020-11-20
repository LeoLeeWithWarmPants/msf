package com.leolee.msf.utils.redisLock;

import org.springframework.data.redis.core.RedisTemplate;

import java.util.concurrent.TimeUnit;

/**
 * @ClassName ExtensionExpirationTime
 * @Description: TODO
 * @Author LeoLee
 * @Date 2020/11/20
 * @Version V1.0
 **/
public class ExtensionExpirationTime extends Thread {


    private String productId;

    private String value;

    private long checkTime;

    private RedisTemplate<String,Object> redisTemplate;

    private int i;

    public ExtensionExpirationTime(String productId, String value, long time, RedisTemplate<String,Object> redisTemplate) {
        this.productId = productId;
        this.value = value;
        this.checkTime = time/3 > 0 ? time/3 : 5000;
        this.redisTemplate = redisTemplate;
    }


    @Override
    public void run() {

        while (!Thread.currentThread().isInterrupted()) {
            try {
                Thread.sleep(checkTime);
                //延长过期时间
                System.out.println("prudctId:" + productId + ",第" + ++i + "次续期");
                checkExpiretion();
            } catch (InterruptedException e) {
                e.printStackTrace();
                //中断状态在抛出异常前，被清除掉，因此在此处重置中断状态
                Thread.currentThread().interrupt();
            }
        }
    }

    private void checkExpiretion() {
        long currentExpire = redisTemplate.opsForValue().getOperations().getExpire(productId);
        if (currentExpire < checkTime/1000) {
            redisTemplate.expire(productId, checkTime + currentExpire * 2000, TimeUnit.MILLISECONDS);
        }
    }
}
