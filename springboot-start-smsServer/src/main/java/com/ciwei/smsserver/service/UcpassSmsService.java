package com.ciwei.smsserver.service;

import com.ciwei.smsserver.entity.BaseRequest;
import com.ciwei.smsserver.entity.SmsProperties;
import com.ciwei.smsserver.entity.UcpassSmsRequest;
import com.ciwei.smsserver.exception.SmsException;
import com.ciwei.smsserver.ucpass.client.JsonReqClient;

/**
 * @author FuHang
 * @date 2019/8/5
 * @Description: 云之讯短信服务
 **/
public class UcpassSmsService implements SmsService{
    private static final String SYS_TYPE_CONFIG_ERROR_MSG = "短信服务商信息配置错误";

    private JsonReqClient jsonReqClient;

    private SmsProperties smsProperties;

    public UcpassSmsService(JsonReqClient jsonReqClient, SmsProperties smsProperties){
        this.jsonReqClient = jsonReqClient;
        this.smsProperties = smsProperties;
    }

    @Deprecated
    @Override
    public Object sendSms(Integer type, BaseRequest params) throws SmsException {
       return sendTemplateSms(String.valueOf(type), params);
    }

    @Override
    public Object sendTemplateSms(String templateId, BaseRequest params) throws SmsException {
        if (params instanceof UcpassSmsRequest){
            UcpassSmsRequest ucPassSmsRequest = (UcpassSmsRequest) params;
            // 发送的手机号
            String mobile = ucPassSmsRequest.getMobile().split(",")[0];
            // 参数
            String param= ucPassSmsRequest.getParam();
            String sid = smsProperties.getSign();
            String token = smsProperties.getSecurityKey();
            String appid = smsProperties.getAppid();
            return jsonReqClient.sendSms(sid,token,appid,templateId,param,mobile,null);
        }
        throw new IllegalStateException(SYS_TYPE_CONFIG_ERROR_MSG);
    }

    @Deprecated
    @Override
    public Object sendBatchSms(int type, BaseRequest params) throws SmsException {
        return sendBatchTemplateSms(String.valueOf(type), params);
    }

    @Override
    public Object sendBatchTemplateSms(String templateId, BaseRequest params) throws SmsException {
        if (params instanceof UcpassSmsRequest){
            UcpassSmsRequest ucPassSmsRequest = (UcpassSmsRequest) params;
            // 发送的手机号
            String mobile = ucPassSmsRequest.getMobile();
            // 参数
            String param= ucPassSmsRequest.getParam();
            String sid = smsProperties.getSign();
            String token = smsProperties.getSecurityKey();
            String appid = smsProperties.getAppid();
            return jsonReqClient.sendSmsBatch(sid,token,appid,templateId,param,mobile,null);
        }
        throw new IllegalStateException(SYS_TYPE_CONFIG_ERROR_MSG);
    }

    @Override
    public Object sendVoiceTemplateSms(String templateId, BaseRequest params) throws SmsException{
        return null;
    }
}
