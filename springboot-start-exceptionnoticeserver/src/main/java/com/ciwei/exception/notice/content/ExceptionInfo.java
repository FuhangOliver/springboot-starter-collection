package com.ciwei.exception.notice.content;

import lombok.Data;
import org.springframework.util.DigestUtils;
import org.springframework.util.StringUtils;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static java.util.stream.Collectors.toList;

/**
 * 异常信息数据model
 *
 * @author FuHang
 */
@Data
public class ExceptionInfo {

    /**
     * 工程名
     */
    private String project;

    /**
     * 异常的标识码
     */
    private String uid;

    /**
     * 请求地址
     */
    private String reqAddress;

    /**
     * 方法名
     */
    private String methodName;

    /**
     * 方法参数信息
     */
    private Object params;

    /**
     * 类路径
     */
    private String classPath;

    /**
     * 异常信息
     */
    private String exceptionMessage;

    /**
     * 异常追踪信息
     */
    private List<String> traceInfo = new ArrayList<>();

    /**
     * 最后一次出现的时间
     */
    private LocalDateTime latestShowTime = LocalDateTime.now();

    public ExceptionInfo(Throwable ex,String className, String methodName, String filterTrace, Object args, String reqAddress) {
        this.exceptionMessage = gainExceptionMessage(ex);
        this.reqAddress = reqAddress;
        this.params = args;
        this.classPath = className;
        this.methodName = methodName;
        List<StackTraceElement> list = Arrays.stream(ex.getStackTrace())
                .filter(x -> filterTrace == null || x.getClassName().startsWith(filterTrace))
                .filter(x -> !"<generated>".equals(x.getFileName())).collect(toList());
        if (list.size() > 0) {
            this.traceInfo = list.stream().map(StackTraceElement::toString).collect(toList());
            this.methodName = Optional.ofNullable(methodName).orElse(list.get(0).getMethodName());
            this.classPath = Optional.ofNullable(className).orElse(list.get(0).getClassName());
        }
        this.uid = calUid();
    }

    public ExceptionInfo(long time,String className, String methodName, Object args, String reqAddress) {
        this.exceptionMessage = String.format("method time out with time : %s ms", time);
        this.reqAddress = reqAddress;
        this.params = args;
        this.classPath = className;
        this.methodName = methodName;
        this.uid = calUid();
    }

    private String gainExceptionMessage(Throwable exception) {
        String em = exception.toString();
        if (exception.getCause() != null) {
            em = String.format("%s\r\n\tcaused by : %s", em, gainExceptionMessage(exception.getCause()));
        }
        return em;
    }

    private String calUid() {
        return DigestUtils.md5DigestAsHex(
                String.format("%s-%s", exceptionMessage, traceInfo.size() > 0 ? traceInfo.get(0) : "").getBytes());
    }

    public String createText() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("项目名称：").append(project).append("\n");
        stringBuilder.append("类路径：").append(classPath).append("\n");
        Optional.ofNullable(reqAddress).ifPresent(reqAddressNotNull ->
                stringBuilder.append("请求地址：").append(reqAddressNotNull).append("\n"));
        stringBuilder.append("方法名：").append(methodName).append("\n");
        Optional.ofNullable(params).ifPresent(paramsNotNull ->
                stringBuilder.append("方法参数：").append(paramsNotNull).append("\n"));
        stringBuilder.append("异常信息：").append("\n").append(exceptionMessage).append("\n");
        if(traceInfo!=null&&!traceInfo.isEmpty()) {
            stringBuilder.append("异常追踪：").append("\n").append(String.join("\n", traceInfo)).append("\n");
        }
        stringBuilder.append("最后一次出现时间：")
                .append(latestShowTime.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
        return stringBuilder.toString();
    }

    public String createWeChatMarkDown() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(">项目名称：<font color=\"info\">").append(project).append("</font>").append("\n");
        stringBuilder.append(">类路径：<font color=\"info\">").append(classPath).append("</font>").append("\n");
        Optional.ofNullable(reqAddress).ifPresent(reqAddressNotNull ->
                stringBuilder.append(">请求地址：<font color=\"info\">").append(reqAddressNotNull).append("</font>").append("\n"));
        stringBuilder.append(">方法名：<font color=\"info\">").append(methodName).append("</font>").append("\n");
        Optional.ofNullable(params).ifPresent(paramsNotNull ->
                stringBuilder.append(">方法参数：<font color=\"info\">").append(paramsNotNull).append("</font>").append("\n"));
        stringBuilder.append(">异常信息：").append("\n").append("<font color=\"warning\">").append(exceptionMessage).append("</font>").append("\n");
        if(traceInfo!=null&&!traceInfo.isEmpty()){
            stringBuilder.append(">异常追踪：").append("\n").append("<font color=\"info\">").append(String.join("\n", traceInfo)).append("</font>").append("\n");
        }
        stringBuilder.append(">最后一次出现时间：<font color=\"info\">")
                .append(latestShowTime.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"))).append("</font>");
        return stringBuilder.toString();
    }

    public String createDingTalkMarkDown() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("#### 项目名称：").append("\n").append("> ").append(project).append("\n");
        stringBuilder.append("#### 类路径：").append("\n").append("> ").append(classPath).append("\n");
        Optional.ofNullable(reqAddress).ifPresent(reqAddressNotNull ->
                stringBuilder.append("#### 请求地址：").append("\n").append("> ").append(reqAddressNotNull).append("\n"));
        stringBuilder.append("#### 方法名：").append("\n").append("> ").append(methodName).append("\n");
        Optional.ofNullable(params).ifPresent(paramsNotNull ->
                stringBuilder.append("#### 方法参数：").append(paramsNotNull).append("\n"));
        stringBuilder.append("#### 异常信息：").append("\n").append("> ").append(exceptionMessage).append("\n");
        if(traceInfo!=null&&!traceInfo.isEmpty()) {
            stringBuilder.append("#### 异常追踪：").append("\n").append("> ").append(String.join("\n", traceInfo)).append("\n");
        }
        stringBuilder.append("#### 最后一次出现时间：")
                .append(latestShowTime.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
        return stringBuilder.toString();
    }
}
