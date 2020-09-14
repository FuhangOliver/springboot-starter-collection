package com.ciwei.exception.notice.annotation;

import java.lang.annotation.*;

/**
 * 异常提示注解
 *
 * @author Fuhang
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ ElementType.METHOD, ElementType.TYPE })
public @interface ExceptionListener {
}
