package com.springboot.md.config;

import org.apache.logging.log4j.ThreadContext;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @Author: 秒度
 * @Email: fangxin.md@Gmail.com
 * @Date: 2020-11-01 23:11
 * @Description:
 */
public class LogInterceptor implements HandlerInterceptor {
    private final static String TRACE_ID = "traceId";

    //修改日志配置文件 在原来的日志格式中 添加traceId的占位符
    //<property name="pattern">[TRACEID:%X{traceId}] %d{HH:mm:ss.SSS} %-5level %class{-1}.%M()/%L - %msg%xEx%n</property>
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String traceId = java.util.UUID.randomUUID().toString().replaceAll("-", "").toUpperCase();
        ThreadContext.put("traceId", traceId);
        return true;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView)
            throws Exception {
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex)
            throws Exception {
        ThreadContext.remove(TRACE_ID);
    }
}