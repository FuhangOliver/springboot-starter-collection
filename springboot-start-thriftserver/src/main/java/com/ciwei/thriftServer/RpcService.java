package com.ciwei.thriftServer;

import org.springframework.stereotype.Service;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

/**
 * @author liuqi
 * @date 2019/7/10 16:02
 * @desc RPC注解（在service类上注入就代表可以提供RPC服务）
 */

@Retention(value = java.lang.annotation.RetentionPolicy.RUNTIME)
@Target(value = { java.lang.annotation.ElementType.TYPE })
@Service
public @interface RpcService {
}
