package com.springboot.md.aop;

import cn.hutool.extra.servlet.ServletUtil;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Component;
import org.springframework.util.StopWatch;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;


/**
 * @Author 秒度
 * @Email fangxin.md@Gmail.com
 * @Date 2020/9/30 下午1:54
 * @Description
 */
@Component
@Aspect
@Slf4j
public class WebLogParsing {


//	[Aspect1] around advise 1  进环绕
//[Aspect1] before advise 执行前置
//test OK  执行方法
//[Aspect1] around advise2  出环绕
//[Aspect1] after advise 执行后置
//[Aspect1] afterReturning advise  执行后置同志通知

    @Autowired
    private ThreadPoolTaskExecutor threadPoolExecutor;

    // 第一个*代表返回类型不限
    // 第二个*代表所有类
    // 第三个*代表所有方法show memory indicator
    // (..) 代表参数不限
    //@Pointcut("execution(public * com.lmx.blog.controller.*.*(..))")

    @Pointcut("@within(com.springboot.md.aop.WebLog)")
    public void webLog() {
    }

    @Before("webLog()")
    public void before(JoinPoint joinPoint) {
        HttpServletRequest httpServletRequest = getHttpServletRequest();
        String requestUrl = httpServletRequest.getRequestURL().toString();
        String method = httpServletRequest.getMethod();
        log.info("========================   START  =========================");
        log.info("URL         :[{}]", requestUrl);
        log.info("http method         :[{}]", method);
        String declaringTypeName = joinPoint.getSignature().getDeclaringTypeName();
        String name = joinPoint.getSignature().getName();
        log.info("class method         :[{}.{}]", declaringTypeName, name);
        log.info("requestIp         :[{}]", ServletUtil.getClientIP(httpServletRequest));
    }

    @AfterReturning("webLog()")
    public void afterReturn(JoinPoint joinPoint) {
        System.out.println("方法执行完执行...afterRunning");
    }

    @After("webLog()")
    public void after(JoinPoint joinPoint) {

    }

    @Around("webLog()")
    public Object around(ProceedingJoinPoint joinPoint) throws Throwable {
        StopWatch stopwatch = new StopWatch();
        stopwatch.start("Record-Api-Log");
        Object proceed = joinPoint.proceed();
        HttpServletRequest httpServletRequest = getHttpServletRequest();

        stopwatch.stop();
        long latency = stopwatch.getTotalTimeMillis();
        log.info("耗时：[{}]毫秒", latency);
        recordAprCalled(httpServletRequest, latency);
        log.info("========================   END  =========================");
        return proceed;
    }


//


    @AfterThrowing("webLog()")
    public void afterThrowing() {
        System.out.println("异常出现之后...afterThrowing");
    }


    private void recordAprCalled(HttpServletRequest request, Long latency) {
        threadPoolExecutor.execute(() -> {
            String method = request.getMethod();
            String url = request.getRequestURL().toString();
            String clientIP = ServletUtil.getClientIP(request);
            //todo 插入db  表设计 api method 最长耗时  最长耗时ip 访问次数   latency记得更新
//			log.info("方式：{}", method);
//			log.info("api：{}", url);
//			log.info("ip：{}", clientIP);
//			log.info("时长：{}", latency);
        });
    }

    private HttpServletRequest getHttpServletRequest() {
        ServletRequestAttributes requestAttributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        return requestAttributes.getRequest();
    }

}
