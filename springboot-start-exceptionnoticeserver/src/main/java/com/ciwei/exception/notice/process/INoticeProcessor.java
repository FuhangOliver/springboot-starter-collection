package com.ciwei.exception.notice.process;

import com.ciwei.exception.notice.content.ExceptionInfo;

/**
 * 异常信息通知处理接口
 *
 * @author FuHang
 */
public interface INoticeProcessor {

    /**
     * 异常信息通知
     *
     * @param exceptionInfo 异常信息
     */
    void sendNotice(ExceptionInfo exceptionInfo);

}
