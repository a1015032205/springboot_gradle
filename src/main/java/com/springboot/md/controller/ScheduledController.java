package com.springboot.md.controller;

import org.springframework.stereotype.Component;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.TimeUnit;

/**
 * @Author: 秒度
 * @Email: fangxin.md@Gmail.com
 * @Date: 2020-12-09 23:27
 * @Description:
 */
@Component
public class ScheduledController {
    //每10秒执行一次
   // @Scheduled(cron = "0/10 * *  * * ? ")
    //  @Async
    public void aTask() {
        DateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        System.out.println(Thread.currentThread().getName() + ":" + sdf.format(new Date()) + " --> A任务每10秒执行一次");
        try {
            TimeUnit.SECONDS.sleep(20);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
    //单线程下  A==>执行的间隔确是30s。
    //@Async  B==>执行的间隔确是10s  此时A和B任务互相不影响，而且A的sleep也不影响A的任务了。
    //ThreadPoolTaskScheduler===>   A和B的任务在不同的线程中执行，所以互相不影响，A的sleep会影响到下一次执行。


    //每5秒执行一次
   // @Scheduled(cron = "0/5 * *  * * ? ")
    // @Async
    public void bTask() {
        DateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        System.out.println(Thread.currentThread().getName() + ":" + sdf.format(new Date()) + " --> B任务每5秒执行一次");
    }
    //单线程下  B受A影响
    //@Async B==>执行的间隔确是5s  此时A和B任务互相不影响，而且A的sleep也不影响A的任务了。
    //ThreadPoolTaskScheduler===>   A和B的任务在不同的线程中执行，所以互相不影响，A的sleep会影响到下一次执行。


  //（1）单线程情况下A任务的延迟会影响到B任务的执行。
    //（2）引起定时任务延迟原因：Spring/Spring Boot的定时任务默认是单线程 (敲重点)。
    //（3）解决定时任务延迟问题：异步任务和多线程(敲重点)。
    //（4）异步任务定时任务执行情况：A/B任务互相不影响，A的定时任务执行过长不会影响A的下次执行。
    //（5）多线程定时任务执行情况：A/B任务互相不影响，A的定时任务执行过长会影响到A的下次执行。
    //（6）定时任务重复执行的方案：①锁表行记录（不能锁业务表行记录，因为可能会影响正常的业务执行）；②缓存ID（可以是任务ID，也可以是业务表ID
}
