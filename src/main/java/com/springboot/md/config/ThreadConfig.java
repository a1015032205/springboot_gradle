package com.springboot.md.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.aop.interceptor.AsyncUncaughtExceptionHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.scheduling.annotation.AsyncConfigurer;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.lang.reflect.Method;

/**
 * @Author 秒度
 * @Email fangxin.md@Gmail.com
 * @Date 2020/9/30 上午10:22
 * @Description 线程池
 */
@EnableAsync
@Configuration
@Slf4j
public class ThreadConfig implements AsyncConfigurer {

    @Bean
    @Override
    @Primary
    public ThreadPoolTaskExecutor getAsyncExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();

        int core = Runtime.getRuntime().availableProcessors();

        //核心数量
        executor.setCorePoolSize(core);
        //最大线程数
        executor.setMaxPoolSize((core << 1) + core);
        //等待队列
        executor.setQueueCapacity(100);
        //前缀名称
        executor.setThreadNamePrefix("cfx-thread-");
        executor.initialize();
        return executor;
    }

    @Override
    public AsyncUncaughtExceptionHandler getAsyncUncaughtExceptionHandler() {
        return new SpringAsyncExceptionHandler();
    }

    private class SpringAsyncExceptionHandler implements AsyncUncaughtExceptionHandler {
        @Override
        public void handleUncaughtException(Throwable throwable, Method method, Object... objects) {
            log.info("-------------multi thread exception-----------------");
        }
    }
}
