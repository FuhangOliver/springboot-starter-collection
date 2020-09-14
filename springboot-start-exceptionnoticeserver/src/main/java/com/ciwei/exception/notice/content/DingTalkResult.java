package com.ciwei.exception.notice.content;

import lombok.Data;

/**
 * 钉钉异常通知响应结果
 *
 * @author FuHang
 */
@Data
public class DingTalkResult {

    private int errcode;

    private String errmsg;

    @Override
    public String toString() {
        return "DingDingResult [errcode=" + errcode + ", errmsg=" + errmsg + "]";
    }

}
