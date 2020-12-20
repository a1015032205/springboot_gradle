package com.springboot.md.application;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.event.ApplicationEnvironmentPreparedEvent;
import org.springframework.boot.context.event.ApplicationPreparedEvent;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextClosedEvent;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.ContextStartedEvent;
import org.springframework.context.event.ContextStoppedEvent;
import org.springframework.stereotype.Component;

/**
 * @Author 秒度
 * @Email fangxin.md@Gmail.com
 * @Date 2020/8/31 上午10:44
 * @Description Spring 事件监听总线
 */
@Slf4j
@SuppressWarnings("rawtypes")
@Component
public class ApplicationReadyListener implements ApplicationListener {
    @Override
    public void onApplicationEvent(ApplicationEvent applicationEvent) {
        if (applicationEvent instanceof ApplicationEnvironmentPreparedEvent) {
            log.info("================  初始化环境变量 =================");
        } else if (applicationEvent instanceof ApplicationPreparedEvent) {
            log.info("================  初始化完成 =================");
        } else if (applicationEvent instanceof ContextRefreshedEvent) {
            log.info("================  容器正在刷新，等待启动 =================");
        } else if (applicationEvent instanceof ApplicationReadyEvent) {
            String server = ((ApplicationReadyEvent) applicationEvent).getSpringApplication().getAllSources().iterator().next().toString();
            log.info("================  {},启动完成 =================", server.substring(server.lastIndexOf(".") + 1));
        } else if (applicationEvent instanceof ContextStartedEvent) {
            log.info("================  应用启动 =================");
        } else if (applicationEvent instanceof ContextStoppedEvent) {
            log.info("================  应用停止 =================");
        } else if (applicationEvent instanceof ContextClosedEvent) {
            log.info("================  应用关闭 =================");
        }
    }
}
