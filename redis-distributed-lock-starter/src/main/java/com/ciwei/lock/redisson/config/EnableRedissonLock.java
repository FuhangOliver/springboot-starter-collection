package com.ciwei.lock.redisson.config;

import org.springframework.context.annotation.Import;

import java.lang.annotation.*;

/**
 * @author FuHang
 * @date 2019/7/10
 * @desc 开启Redisson注解支持
 */
@Inherited
@Documented
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Import(RedissonAutoConfiguration.class)
public @interface EnableRedissonLock {
}
