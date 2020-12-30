package com.springboot.md.controller;

import com.springboot.md.aop.RedisLock;
import com.springboot.md.dao.InfoMapper;
import com.springboot.md.utils.RedisLockUtil;
import com.springboot.md.utils.RedisUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.concurrent.TimeUnit;

/**
 * @Author: 秒度
 * @Email: fangxin.md@Gmail.com
 * @Date: 2020-12-06 20:46
 * @Description: redis 分布式锁
 */

@Slf4j
@RequestMapping("/redis")
@RestController
public class RedisSonController implements InitializingBean {

    @Resource
    private RedisUtil redisUtil;

    @Autowired
    private ThreadPoolTaskExecutor executor;

    @Resource
    private InfoMapper infoMapper;

    @Autowired
    private Environment environment;

    @Override
    public void afterPropertiesSet() throws Exception {
        redisUtil.set("Info:Size", "10");
    }

    @RequestMapping("/lock")
    @RedisLock(key = "redisKey", waitTime = 0L, leaseTime = 5L, isUnlock = true)
    @Async
    public void redisLock() throws InterruptedException {
        String str = redisUtil.get("Info:Size");
        int size = str == null ? 0 : Integer.parseInt(str);
        String port = environment.getProperty("local.server.port");
        String name = Thread.currentThread().getName();
        if (size > 0) {
            TimeUnit.SECONDS.sleep(3L);
            String value = String.valueOf(size - 1);
            redisUtil.set("Info:Size", value);
            infoMapper.del();
            log.error("当前进入客户端端口：{}，当前买到的线程名称：{},剩余数量：{}", port, name, value);
        } else {
            System.out.println("==========卖完了=======================");
            System.exit(0);
        }


//        String info = redisUtil.get("info");
//        int i = Integer.parseInt(info);
//
//        if (i > 9) {
//            TimeUnit.SECONDS.sleep(2L);
//            redisUtil.set("info", "9");
//            log.error("当前进入客户端端口：{}，当前线程名称：{},抢到了！！！！", port, name);
//        } else {
//            log.info("当前进入客户端端口：{}，当前线程名称：{},没有抢到==============", port, name);
//        }
    }

//    @RedisLock(key = "redisKey")
//    @Scheduled(cron = "*/1 * * * * ?")
//    public void task01() throws InterruptedException {
//        Thread.sleep(5000L);
//        String name = Thread.currentThread().getName();
//        log.info(name + "===task01" + RedisSonController.name);
//    }
//
//    @RedisLock(key = "redisKey")
//    @Scheduled(cron = "*/1 * * * * ?")
//    public void task02() throws InterruptedException {
//        Thread.sleep(5000L);
//        String name = Thread.currentThread().getName();
//        log.info(name + "==-task02" + name);
//    }

//    @RedisLock(key = "redisKey")
//    @Scheduled(cron = "*/1 * * * * ?")
//    public void task03() throws InterruptedException {
//        Thread.sleep(5000L);
//        String name = Thread.currentThread().getName();
//        log.info(name + "===task03" + name);
//    }


    @RequestMapping("/test")
    public void redissonTest() {
        String key = "redisson_key";
        for (int i = 0; i < 100; i++) {
            Thread t = new Thread(() -> {
                try {
                    System.err.println("=============线程开启============" + Thread.currentThread().getName());
                    RedisLockUtil.lock(key, 10L); //直接加锁，获取不到锁则一直等待获取锁
                    Thread.sleep(100); //获得锁之后可以进行相应的处理
                    System.err.println("======获得锁后进行相应的操作======" + Thread.
                            currentThread().getName());
                    RedisLockUtil.unlock(key); //解锁
                    System.err.println("=============================" +
                            Thread.currentThread().getName());

                    boolean isGetLock = RedisLockUtil.tryLock(key, TimeUnit.SECONDS, 5L, 10L); // 尝试获取锁，等待5秒，自己获得锁后一直不解锁则10秒后自动解锁
                    if (isGetLock) {
                        System.out.println("线程:" + Thread.currentThread().getName() + ",获取到了锁");
                        Thread.sleep(100); // 获得锁之后可以进行相应的处理
                        System.err.println("======获得锁后进行相应的操作======" + Thread.currentThread().getName());
                        //distributedLocker.unlock(key);
                        System.err.println("=============================" + Thread.currentThread().getName());
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            });
            t.start();
        }
    }
}
