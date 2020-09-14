package com.ciwei.exception.notice.handler;

import com.ciwei.exception.notice.content.ExceptionInfo;
import com.ciwei.exception.notice.process.INoticeProcessor;
import com.ciwei.exception.notice.properties.ExceptionNoticeProperties;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.method.HandlerMethod;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.*;
import java.util.concurrent.*;

/**
 * 异常信息通知前处理
 *
 * @author FuHang
 */
@Slf4j
public class ExceptionNoticeHandler {

    private final String SEPARATOR = System.getProperty("line.separator");

    private final ScheduledExecutorService executor = Executors.newScheduledThreadPool(1);

    private final BlockingQueue<ExceptionInfo> exceptionInfoBlockingDeque = new ArrayBlockingQueue<>(1024);

    private final ExceptionNoticeProperties exceptionProperties;

    private final List<INoticeProcessor> noticeProcessors;

    public ExceptionNoticeHandler(ExceptionNoticeProperties exceptionProperties,
                                  List<INoticeProcessor> noticeProcessors) {
        this.exceptionProperties = exceptionProperties;
        this.noticeProcessors = noticeProcessors;
    }

    /**
     * 将捕获到的异常信息封装好之后发送到阻塞队列
     */
    public Boolean createNotice(Exception ex, JoinPoint joinPoint) {
        if (containsException(ex)) {
            return null;
        }
        log.error("捕获到异常开始发送消息通知:{}method:{}--->", SEPARATOR, joinPoint.getSignature().getName());
        //获取请求参数
        Object parameter = getParameter(joinPoint);
        //获取当前请求对象
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        String address = Optional.ofNullable(attributes).map(attributesNotNull ->
                attributesNotNull.getRequest().getRequestURL().toString() + ((attributesNotNull.getRequest().getQueryString() != null && attributesNotNull.getRequest().getQueryString().length() > 0) ? "?" + attributesNotNull.getRequest().getQueryString() : "")
        ).orElse(null);
        String className = joinPoint.getTarget().getClass().getName();
        String methodName = joinPoint.getSignature().getName();
        ExceptionInfo exceptionInfo = new ExceptionInfo(ex, className, methodName, exceptionProperties.getIncludedTracePackage(), parameter, address);
        exceptionInfo.setProject(exceptionProperties.getProjectName());
        return exceptionInfoBlockingDeque.offer(exceptionInfo);
    }

    /**
     * 将捕获到的超时信息封装好之后发送到阻塞队列
     */
    public Boolean createTimeOutNotice(long time, JoinPoint joinPoint) {
        log.error("捕获到超时开始发送消息通知:{}method:{}--->", SEPARATOR, joinPoint.getSignature().getName());
        Object parameter = getParameter(joinPoint);
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        String address = Optional.ofNullable(attributes).map(attributesNotNull ->
                attributesNotNull.getRequest().getRequestURL().toString() + ((attributesNotNull.getRequest().getQueryString() != null && attributesNotNull.getRequest().getQueryString().length() > 0) ? "?" + attributesNotNull.getRequest().getQueryString() : "")
        ).orElse(null);
        String className = joinPoint.getTarget().getClass().getName();
        String methodName = joinPoint.getSignature().getName();
        ExceptionInfo exceptionInfo = new ExceptionInfo(time, className, methodName, parameter, address);
        exceptionInfo.setProject(exceptionProperties.getProjectName());
        return exceptionInfoBlockingDeque.offer(exceptionInfo);
    }

    /**
     * 启动定时任务发送异常通知
     */
    public void start() {
        executor.scheduleAtFixedRate(() -> {
            ExceptionInfo exceptionInfo = exceptionInfoBlockingDeque.poll();
            Optional.ofNullable(exceptionInfo).ifPresent(exceptionInfoNotNull ->
                    noticeProcessors.forEach(noticeProcessor -> noticeProcessor.sendNotice(exceptionInfoNotNull))
            );
        }, 6, exceptionProperties.getPeriod(), TimeUnit.SECONDS);
    }

    private boolean containsException(Exception exception) {
        Class<? extends Exception> exceptionClass = exception.getClass();
        List<Class<? extends Exception>> list = exceptionProperties.getExcludeExceptions();
        for (Class<? extends Exception> clazz : list) {
            if (clazz.isAssignableFrom(exceptionClass)) {
                return true;
            }
        }
        return false;
    }


    /**
     * 根据方法和传入的参数获取请求参数
     */
    private Object getParameter(JoinPoint joinPoint) {
        Signature signature = joinPoint.getSignature();
        MethodSignature methodSignature = (MethodSignature) signature;
        String[] parameterNames = methodSignature.getParameterNames();
        Object[] args = joinPoint.getArgs();

        List<Object> argList = new ArrayList<>(parameterNames.length);
        for (int i = 0; i < args.length; i++) {
            Map<String, Object> map = new HashMap<>(1);
            String key = parameterNames[i];
            map.put(key, args[i]);
            argList.add(map);
        }
        if (argList.size() == 0) {
            return null;
        } else if (argList.size() == 1) {
            return argList.get(0);
        } else {
            return argList;
        }
    }
}
