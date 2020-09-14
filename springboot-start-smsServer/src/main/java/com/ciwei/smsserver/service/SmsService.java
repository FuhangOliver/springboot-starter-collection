package com.ciwei.smsserver.service;

import com.ciwei.smsserver.entity.BaseRequest;
import com.ciwei.smsserver.exception.SmsException;
import org.springframework.stereotype.Component;

/**
 *功能描述
 * @author FuHang
 * @date 2019/8/8
 */
@Component
public interface SmsService {

    /**
     *  单个发送短信
     * @param params 根据对应的短信服务商所需信息填写
     */
    public Object sendSms(Integer type, BaseRequest params) throws SmsException;

    /**
     * 单个发送模板短信
     * @param templateId 短信模板id
     * @param params 根据对应的短信服务商所需信息填写
     */
    public Object sendTemplateSms(String templateId, BaseRequest params) throws SmsException;

    /**
     * 单个发送语音模板短信
     * @param templateId 短信模板id
     * @param params 根据对应的短信服务商所需信息填写
     */
    public Object sendVoiceTemplateSms(String templateId, BaseRequest params) throws SmsException;

    /**
     *  批量发送短信
     * @param params 根据对应的短信服务商所需信息填写
     */
    public Object sendBatchSms(int type, BaseRequest params) throws SmsException;

    /**
     * 批量发送模板短信
     * @param templateId 短信模板id
     * @param params 根据对应的短信服务商所需信息填写
     */
    public Object sendBatchTemplateSms(String templateId, BaseRequest params) throws SmsException;
}
