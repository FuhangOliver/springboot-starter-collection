package com.ciwei.lock.redisson.annotation;

import com.ciwei.lock.redisson.RedissonLock;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @author FuHang
 * @date 2019/7/10
 * @desc Redisson分布式锁注解解析器
 */
@Aspect
@Component
public class DistributedLockHandler {

    private static final Logger LOGGER = LoggerFactory.getLogger(DistributedLockHandler.class);

    @Pointcut("@annotation(com.ciwei.lock.redisson.annotation.DistributedLock)")
    public void distributedLock() {}

    @Autowired
    RedissonLock redissonLock;

    @Around("@annotation(distributedLock)")
    public void around(ProceedingJoinPoint joinPoint, DistributedLock distributedLock) {
        LOGGER.debug("[开始]执行RedisLock环绕通知,获取Redis分布式锁开始");
        /**获取锁名称*/
        String lockName = distributedLock.value();
        /**获取超时时间，默认十秒*/
        int expireSeconds = distributedLock.expireSeconds();
        int sleepTime = distributedLock.sleepTime();
        if (redissonLock.lock(sleepTime,lockName, expireSeconds)) {
            try {
                LOGGER.debug("获取Redis分布式锁[成功]，加锁完成，开始执行业务逻辑...");
                joinPoint.proceed();
            } catch (Throwable throwable) {
                LOGGER.error("获取Redis分布式锁[异常]，加锁失败", throwable);
                throwable.printStackTrace();
            } finally {
                redissonLock.release(lockName);
            }
            LOGGER.debug("释放Redis分布式锁[成功]，解锁完成，结束业务逻辑...");
        } else {
            LOGGER.debug("获取Redis分布式锁[失败]");
        }
        LOGGER.debug("[结束]执行RedisLock环绕通知");
    }
}
