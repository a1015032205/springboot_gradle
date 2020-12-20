package com.springboot.md.aop;

import java.lang.annotation.*;

/**
 * @Author 秒度
 * @Email fangxin.md@Gmail.com
 * @Date 2020/9/30 下午1:38
 * @Description 接口日志
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE, ElementType.METHOD})
@Documented
@Inherited
public @interface RequestLog {
}
