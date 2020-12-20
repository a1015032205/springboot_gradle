package com.springboot.md.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.SchedulingConfigurer;
import org.springframework.scheduling.config.ScheduledTaskRegistrar;

import java.util.concurrent.Executors;

/**
 * @Author: 秒度
 * @Email: fangxin.md@Gmail.com
 * @Date: 2020-12-09 23:56
 * @Description:
 */

@Configuration
public class ScheduleConfig implements SchedulingConfigurer {
    @Override
    public void configureTasks(ScheduledTaskRegistrar taskRegistrar) {
        //设定一个长度10的定时任务线程池
        taskRegistrar.setScheduler(Executors.newScheduledThreadPool(10));
    }

    // 这种方式主要点是使用ThreadPoolTaskScheduler进行管理定时任务：
//    @Bean
//    public TaskScheduler taskScheduler() {
//        ThreadPoolTaskScheduler scheduler = new ThreadPoolTaskScheduler();
//        scheduler.setPoolSize(10);
//        return scheduler;
//    }
}