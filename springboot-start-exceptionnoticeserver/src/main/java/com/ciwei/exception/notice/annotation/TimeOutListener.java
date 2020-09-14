package com.ciwei.exception.notice.annotation;

import java.lang.annotation.*;

/**
 * 超时提示注解,超时时间默认1000ms
 *
 * @author Fuhang
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ ElementType.METHOD, ElementType.TYPE })
public @interface TimeOutListener {
    long timeOutMs() default 1000L;
}
