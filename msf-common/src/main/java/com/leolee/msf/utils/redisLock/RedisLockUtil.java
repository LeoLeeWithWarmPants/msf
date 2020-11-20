package com.leolee.msf.utils.redisLock;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 * @ClassName RedisLockUtil
 * @Description: 分布式锁（还是存在超卖的情况，该实例仅供理解学习redis分布式锁）
 * @Author LeoLee
 * @Date 2020/11/20
 * @Version V1.0
 **/
public class RedisLockUtil {

    @Autowired
    private RedisTemplate<String,Object> redisTemplate;


    //========================方案1============================

    /*
     * 功能描述: <br>
     * 〈分布式锁——加锁〉
     * @Param: [key, timeMillis 设置要大于当前时间戳]
     * @Return: boolean
     * @Author: LeoLee
     * @Date: 2020/11/20 12:24
     */
    @Deprecated
    public boolean redisLock(String key, String timeMillis) {

        //加锁成功直接返回true，证明目前还没有该key
        if (redisTemplate.opsForValue().setIfAbsent(key, timeMillis)) {
            return true;
        }

        //解决死锁
        String current = (String) redisTemplate.opsForValue().get(key);
        if (current != null && Long.valueOf(current) < System.currentTimeMillis()) {//之前的锁过期了，应该允许新的请求获取锁
            //Set value of key and return its old value.设置新值返回旧值，
            // 考虑多线程并发的情况，只有一个线程的设置值和当前值相同，它才有权利加锁
            String old = (String) redisTemplate.opsForValue().getAndSet(key, timeMillis);//getAndSet线程安全
            if (old != null && old.equals(current)) {
                return true;
            }
        }
        return false;
    }

    @Deprecated
    public boolean deleteLock(String key, String timeMillis) {

        if (String.valueOf(redisTemplate.opsForValue().get(key)).equals(timeMillis)) {
            return redisTemplate.delete(key);
        }
        return false;
    }


    //========================方案2============================
    ExtensionExpirationTime extensionExpirationTime = null;
    /*
     * 功能描述: <br>
     * 〈分布式加锁，延长过期时间版〉
     * @Param: [key, value, time]
     * @Return: boolean
     * @Author: LeoLee
     * @Date: 2020/11/20 17:02
     */
    public boolean redisLock(String key, String value, long time) {


        if (redisTemplate.opsForValue().setIfAbsent(key, value, time, TimeUnit.MILLISECONDS)) {

            extensionExpirationTime = new ExtensionExpirationTime(key, value, time, redisTemplate);
            extensionExpirationTime.start();
            return true;
        }
        return false;
    }

    public boolean newDeleteLock(String key, String value) {

        if (String.valueOf(redisTemplate.opsForValue().get(key)).equals(value)) {
            extensionExpirationTime.interrupt();//终止续期线程
            if (extensionExpirationTime.isInterrupted()) {
                try {
                    extensionExpirationTime.stop();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            return redisTemplate.delete(key);
        }
        return false;
    }

}
