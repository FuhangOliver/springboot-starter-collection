package com.ciwei.exception.notice.aop;

import com.ciwei.exception.notice.annotation.ExceptionListener;
import com.ciwei.exception.notice.annotation.TimeOutListener;
import com.ciwei.exception.notice.handler.ExceptionNoticeHandler;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.*;
import org.aspectj.lang.reflect.MethodSignature;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 异常捕获切面
 *
 * @author FuHang
 */
@Aspect
@Slf4j
@RequiredArgsConstructor
public class ExceptionNoticeAop {

    private final ExceptionNoticeHandler handler;

    @AfterThrowing(value = "@within(listener)", throwing = "e")
    public void exceptionNotice(JoinPoint joinPoint, ExceptionListener listener, Exception e) {
        handleException(e, joinPoint);
    }

    @AfterThrowing(value = "@annotation(listener)", throwing = "e")
    public void exceptionNoticeWithMethod(JoinPoint joinPoint, ExceptionListener listener, Exception e) {
        handleException(e, joinPoint);
    }

    private void handleException(Exception exception, JoinPoint joinPoint) {
        log.debug("出现异常：" + joinPoint.getSignature().getName()
                + String.join(",", Arrays.stream(joinPoint.getArgs()).map(x -> x.toString()).toArray(String[]::new)));
        handler.createNotice(exception, joinPoint);
    }

    private Map<Long, Map<String, List<Long>>> threadMap = new ConcurrentHashMap<>(200);

    @Before(value = "@annotation(listener) || @within(listener)")
    public void serviceLogBefore(JoinPoint point, TimeOutListener listener) {
        Map<String, List<Long>> methodTimeMap = threadMap.get(Thread.currentThread().getId());
        int result = Optional.ofNullable(methodTimeMap).map(methodTimeMapNotNull -> {
            List<Long> list = Optional.ofNullable(methodTimeMapNotNull.get(point.toShortString()))
                    .orElse(new LinkedList<>());
            list.add(System.currentTimeMillis());
            methodTimeMapNotNull.put(point.toShortString(), list);
            return 1;
        }).orElseGet(() -> {
            Map<String, List<Long>> methodTimeMapTemp = new HashMap<>();
            List<Long> list = new LinkedList<>();
            list.add(System.currentTimeMillis());
            methodTimeMapTemp.put(point.toShortString(), list);
            threadMap.put(Thread.currentThread().getId(), methodTimeMapTemp);
            return 0;
        });
        log.info("method serviceLogBefore add method start time result {}", result == 1 ? "success" : "fail");
    }

    @After(value = "@annotation(listener) || @within(listener)")
    public void serviceLogAfter(JoinPoint joinPoint, TimeOutListener listener) {
        Map<String, List<Long>> methodTimeMap = threadMap.get(Thread.currentThread().getId());
        List<Long> list = methodTimeMap.get(joinPoint.toShortString());
        long intervalTime = System.currentTimeMillis() - list.get(list.size() - 1);
        log.info("\n方法：{}\n耗时：{}\n", joinPoint.toShortString(),
                (intervalTime));
        list.remove(list.size() - 1);
        if (intervalTime > listener.timeOutMs()) {
            this.handler.createTimeOutNotice(intervalTime, joinPoint);
        }
    }
}
