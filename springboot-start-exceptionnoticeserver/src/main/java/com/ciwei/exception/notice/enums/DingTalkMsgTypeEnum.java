package com.ciwei.exception.notice.enums;

/**
 * 钉钉文本类型枚举
 *
 * @author FuHang
 */
public enum DingTalkMsgTypeEnum {

    TEXT("text"), MARKDOWN("markdown");

    private final String msgType;

    public String getMsgType() {
        return msgType;
    }

    DingTalkMsgTypeEnum(String msgType) {
        this.msgType = msgType;
    }
}
