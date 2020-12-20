package com.springboot.md.aop;

import cn.hutool.core.map.MapUtil;
import com.springboot.md.utils.RedisLockUtil;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.*;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

/**
 * @Author: 秒度
 * @Email: fangxin.md@Gmail.com
 * @Date: 2020-12-12 23:48
 * @Description:
 */
@Component
@Aspect
@Slf4j
public class RedisLockAspect {

    @Pointcut("@annotation(com.springboot.md.aop.RedisLock)")
    public void redisLock() {
    }

    @Around("redisLock()")
    public Object around(ProceedingJoinPoint joinPoint) throws Throwable {
        String name = Thread.currentThread().getName();
        Map<String, Object> map = getMap(joinPoint);
        String key = MapUtil.getStr(map, "key");
        long waitTime = MapUtil.getLong(map, "waitTime");
        long leaseTime = MapUtil.getLong(map, "leaseTime");
        if (RedisLockUtil.tryLock(key, waitTime, leaseTime)) {
            log.error("{}，抢到了！！！ 准备执行==========", name);
            Object proceed = joinPoint.proceed();
            unLock(joinPoint);
            return proceed;
        } else {
            log.info("{}，没有抢到", name);
            return null;
        }

    }


    @AfterThrowing("redisLock()")
    public void afterThrowing(JoinPoint joinPoint) {
        unLock(joinPoint);
    }

    private Map<String, Object> getMap(JoinPoint joinPoint) {
        MethodSignature ms = (MethodSignature) joinPoint.getSignature();
        Method method = ms.getMethod();
        RedisLock myAnnotation = method.getAnnotation(RedisLock.class);
        String key = myAnnotation.key();
        long waitTime = myAnnotation.waitTime();
        long leaseTime = myAnnotation.leaseTime();
        return MapUtil.builder(new HashMap<String, Object>())
                .put("key", key)
                .put("waitTime", waitTime)
                .put("leaseTime", leaseTime)
                .build();
    }


    public void unLock(JoinPoint joinPoint) {
        Map<String, Object> map = getMap(joinPoint);
        String key = MapUtil.getStr(map, "key");
        boolean heldByCurrentThread = RedisLockUtil.isHeldByCurrentThread(key);
        if (heldByCurrentThread) {

            RedisLockUtil.unlock(key);
            log.error(Thread.currentThread().getName() + "私放锁");
        }
    }


    @Before("redisLock()")
    public void before(JoinPoint joinPoint) throws InterruptedException {
    }

    @AfterReturning("redisLock()")
    public void afterReturn(JoinPoint joinPoint) {

    }


    @After("redisLock()")
    public void after(JoinPoint joinPoint) {
    }
}
