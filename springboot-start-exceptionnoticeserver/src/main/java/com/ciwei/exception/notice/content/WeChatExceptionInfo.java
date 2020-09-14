package com.ciwei.exception.notice.content;

import com.ciwei.exception.notice.enums.WeChatMsgTypeEnum;
import com.ciwei.exception.notice.properties.WeChatProperties;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import static com.ciwei.exception.notice.enums.WeChatMsgTypeEnum.MARKDOWN;
import static com.ciwei.exception.notice.enums.WeChatMsgTypeEnum.TEXT;

/**
 * 企业微信异常通知消息请求体
 *
 * @author FuHang
 */
@Slf4j
@Data
public class WeChatExceptionInfo {

    private WeChatText text;
    private WeChatMarkDown markdown;
    private String msgtype;

    public WeChatExceptionInfo(ExceptionInfo exceptionInfo, WeChatProperties weChatProperties) {
        WeChatMsgTypeEnum msgType = weChatProperties.getMsgType();
        if (msgType.equals(TEXT)) {
            this.text = new WeChatText(exceptionInfo.createText(), weChatProperties.getAtUserIds(), weChatProperties.getAtPhones());
        } else if (msgType.equals(MARKDOWN)) {
            log.info(exceptionInfo.createWeChatMarkDown());
            this.markdown = new WeChatMarkDown(exceptionInfo.createWeChatMarkDown());
        }
        this.msgtype = msgType.getMsgType();
    }

    @NoArgsConstructor
    @AllArgsConstructor
    @Data
    static class WeChatText {

        private String content;

        private String[] mentioned_list;

        private String[] mentioned_mobile_list;

    }

    @NoArgsConstructor
    @AllArgsConstructor
    @Data
    static class WeChatMarkDown {

        private String content;

    }


}
