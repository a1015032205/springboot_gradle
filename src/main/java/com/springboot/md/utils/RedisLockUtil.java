package com.springboot.md.utils;

/**
 * @Author: 秒度
 * @Email: fangxin.md@Gmail.com
 * @Date: 2020-12-06 22:59
 * @Description:
 */

import com.springboot.md.redisson.DistributedLocker;
import org.redisson.api.RLock;

import java.util.concurrent.TimeUnit;

/**
 * redis分布式锁帮助类
 *
 * @author yangzhilong
 */
public class RedisLockUtil {

    private static DistributedLocker redisLock;

    public static void setLocker(DistributedLocker locker) {
        RedisLockUtil.redisLock = locker;
    }

    /**
     * 加锁
     *
     * @param lockKey
     * @return
     */
    public static RLock lock(String lockKey) {
        return redisLock.lock(lockKey);
    }

    /**
     * 释放锁
     *
     * @param lockKey
     */
    public static void unlock(String lockKey) {
        redisLock.unlock(lockKey);
    }

    /**
     * 释放锁
     *
     * @param lock
     */
    public static void unlock(RLock lock) {
        redisLock.unlock(lock);
    }

    /**
     * 带超时的锁
     *
     * @param lockKey
     * @param timeout 超时时间   单位：秒
     */
    public static RLock lock(String lockKey, long timeout) {
        return redisLock.lock(lockKey, timeout);
    }

    /**
     * 带超时的锁
     *
     * @param lockKey
     * @param unit    时间单位
     * @param timeout 超时时间
     */
    public static RLock lock(String lockKey, TimeUnit unit, long timeout) {
        return redisLock.lock(lockKey, unit, timeout);
    }

    /**
     * 尝试获取锁
     *
     * @param lockKey
     * @param waitTime  最多等待时间
     * @param leaseTime 上锁后自动释放锁时间
     * @return
     */
    public static boolean tryLock(String lockKey, long waitTime, long leaseTime) {
        return redisLock.tryLock(lockKey, TimeUnit.SECONDS, waitTime, leaseTime);
    }

    /**
     * 尝试获取锁
     *
     * @param lockKey
     * @param unit      时间单位
     * @param waitTime  最多等待时间
     * @param leaseTime 上锁后自动释放锁时间
     * @return
     */
    public static boolean tryLock(String lockKey, TimeUnit unit, long waitTime, long leaseTime) {
        return redisLock.tryLock(lockKey, unit, waitTime, leaseTime);
    }

    public static boolean isHeldByCurrentThread(String lockKey) {
        return redisLock.isHeldByCurrentThread(lockKey);
    }

    public static boolean isLocked(String lockKey) {
        return redisLock.isLocked(lockKey);
    }
}