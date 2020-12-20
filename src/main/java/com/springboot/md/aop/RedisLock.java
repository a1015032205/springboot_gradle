package com.springboot.md.aop;

import java.lang.annotation.*;

/**
 * @Author: 秒度
 * @Email: fangxin.md@Gmail.com
 * @Date: 2020-12-12 23:40
 * @Description:
 */

//@Documented –注解是否将包含在JavaDoc中
//@Retention –什么时候使用该注解
//@Target –注解用于什么地方
//@Inherited – 是否允许子类继承该注解
@Documented
@Inherited
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD})
public @interface RedisLock {

    String key();

    long waitTime() default 1L;

    long leaseTime() default  5L;

}
