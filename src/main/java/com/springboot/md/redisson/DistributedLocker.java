package com.springboot.md.redisson;

import org.redisson.api.RLock;

import java.util.concurrent.TimeUnit;

/**
 * @Author: 秒度
 * @Email: fangxin.md@Gmail.com
 * @Date: 2020-12-11 0:34
 * @Description:
 */

public interface DistributedLocker {

    RLock lock(String lockKey);

    RLock lock(String lockKey, long timeout);

    RLock lock(String lockKey, TimeUnit unit, long timeout);

    boolean tryLock(String lockKey, TimeUnit unit, long waitTime, long leaseTime);

    void unlock(String lockKey);

    void unlock(RLock lock);

    boolean isHeldByCurrentThread(String lockKey);
}