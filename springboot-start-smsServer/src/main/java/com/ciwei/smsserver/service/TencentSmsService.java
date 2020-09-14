package com.ciwei.smsserver.service;

import com.ciwei.smsserver.entity.BaseRequest;
import com.ciwei.smsserver.entity.SmsProperties;
import com.ciwei.smsserver.entity.TencentSmsRequest;
import com.ciwei.smsserver.exception.SmsException;
import com.github.qcloudsms.SmsMultiSender;
import com.github.qcloudsms.SmsSingleSender;
import com.github.qcloudsms.TtsVoiceSender;
import com.github.qcloudsms.httpclient.HTTPException;

import java.io.IOException;

/**
 *功能描述
 * @author FuHang
 * @date 2019/8/5
 */
public class TencentSmsService implements SmsService {

    private static final String SYS_TYPE_CONFIG_ERROR_MSG = "短信服务商信息配置错误";

    private SmsSingleSender smsSingleSender;

    private SmsMultiSender smsMultiSender;

    private TtsVoiceSender tvsender;

    private SmsProperties smsProperties;

    public TencentSmsService(SmsProperties smsProperties) {
        this.smsProperties = smsProperties;
    }

    @Override
    public Object sendSms(Integer type, BaseRequest params) throws SmsException {
        if (params instanceof TencentSmsRequest){
            TencentSmsRequest tencentSmsRequest = (TencentSmsRequest) params;
            String nationCode = tencentSmsRequest.getNationCode();
            String phoneNumber = tencentSmsRequest.getPhoneNumber()[0];
            String extend = tencentSmsRequest.getExtend();
            String ext = tencentSmsRequest.getExt();
            String msg = tencentSmsRequest.getMsg();
            String business = tencentSmsRequest.getBusiness();
            switch (business){
                case "实习":
                    smsSingleSender = new SmsSingleSender(Integer.parseInt(smsProperties.getExperienceAppid()),smsProperties.getExperienceSecurityKey());
                    break;
                case "体验":
                    smsSingleSender = new SmsSingleSender(Integer.parseInt(smsProperties.getPricticeAppid()),smsProperties.getPricticeSecurityKey());
                    break;
                case "大学":
                    smsSingleSender = new SmsSingleSender(Integer.parseInt(smsProperties.getCollegeAppid()),smsProperties.getCollegeSecurityKey());
                    break;
                default:
                    smsSingleSender = new SmsSingleSender(Integer.parseInt(smsProperties.getPricticeAppid()),smsProperties.getPricticeSecurityKey());
            }
            try {
                return smsSingleSender.send(type,nationCode, phoneNumber,
                        msg, extend, ext);
            } catch (HTTPException e) {
                throw new SmsException(e);
            } catch (IOException e) {
                throw new SmsException(e);
            }
        }
        throw new IllegalStateException(SYS_TYPE_CONFIG_ERROR_MSG);
    }

    @Override
    public Object sendTemplateSms(String tempalteId, BaseRequest params) throws SmsException{
        if (params instanceof TencentSmsRequest){
            TencentSmsRequest tencentSmsRequest = (TencentSmsRequest) params;
            String nationCode = tencentSmsRequest.getNationCode();
            String phoneNumber = tencentSmsRequest.getPhoneNumber()[0];
            String[] params1 = tencentSmsRequest.getParams();
            String business = tencentSmsRequest.getBusiness();
            String sign = "";
            switch (business){
                case "实习":
                    smsSingleSender = new SmsSingleSender(Integer.parseInt(smsProperties.getExperienceAppid()),smsProperties.getExperienceSecurityKey());
                    sign = smsProperties.getPricticeSign();
                    break;
                case "体验":
                    smsSingleSender = new SmsSingleSender(Integer.parseInt(smsProperties.getPricticeAppid()),smsProperties.getPricticeSecurityKey());
                    sign = smsProperties.getExperienceSign();
                    break;
                case "大学":
                    smsSingleSender = new SmsSingleSender(Integer.parseInt(smsProperties.getCollegeAppid()),smsProperties.getCollegeSecurityKey());
                    sign = smsProperties.getCollegeSign();
                    break;
                    default:
                        smsSingleSender = new SmsSingleSender(Integer.parseInt(smsProperties.getCollegeAppid()),smsProperties.getCollegeSecurityKey());
                        sign = smsProperties.getCollegeSign();
            }
            String extend = tencentSmsRequest.getExtend();
            String ext = tencentSmsRequest.getExt();
            try {
                return smsSingleSender.sendWithParam(nationCode, phoneNumber, Integer.parseInt(tempalteId),
                        params1, sign, extend, ext);
            } catch (HTTPException e) {
                throw new SmsException(e);
            } catch (IOException e) {
                throw new SmsException(e);
            }
        }
        throw new IllegalStateException(SYS_TYPE_CONFIG_ERROR_MSG);
    }

    @Override
    public Object sendVoiceTemplateSms(String tempalteId, BaseRequest params) throws SmsException{
        if (params instanceof TencentSmsRequest){
            TencentSmsRequest tencentSmsRequest = (TencentSmsRequest) params;
            String nationCode = tencentSmsRequest.getNationCode();
            String phoneNumber = tencentSmsRequest.getPhoneNumber()[0];
            String[] params1 = tencentSmsRequest.getParams();
            String business = tencentSmsRequest.getBusiness();
            switch (business){
                case "实习":
                    tvsender = new TtsVoiceSender(Integer.parseInt(smsProperties.getExperienceAppid()),smsProperties.getExperienceSecurityKey());
                    break;
                case "体验":
                    tvsender = new TtsVoiceSender(Integer.parseInt(smsProperties.getPricticeAppid()),smsProperties.getPricticeSecurityKey());
                    break;
                case "大学":
                    tvsender = new TtsVoiceSender(Integer.parseInt(smsProperties.getCollegeAppid()),smsProperties.getCollegeSecurityKey());
                    break;
                default:
                    tvsender = new TtsVoiceSender(Integer.parseInt(smsProperties.getCollegeAppid()),smsProperties.getCollegeSecurityKey());
            }
            String ext = tencentSmsRequest.getExt();
            try {
                return tvsender.send(nationCode, phoneNumber, Integer.parseInt(tempalteId),
                        params1,2, ext);
            } catch (HTTPException e) {
                throw new SmsException(e);
            } catch (IOException e) {
                throw new SmsException(e);
            }
        }
        throw new IllegalStateException(SYS_TYPE_CONFIG_ERROR_MSG);
    }

    @Override
    public Object sendBatchSms(int type, BaseRequest params) throws SmsException{
        if (params instanceof TencentSmsRequest){
            TencentSmsRequest tencentSmsRequest = (TencentSmsRequest) params;
            String nationCode = tencentSmsRequest.getNationCode();
            String[] phoneNumber = tencentSmsRequest.getPhoneNumber();
            String msg = tencentSmsRequest.getMsg();
            String extend = tencentSmsRequest.getExtend();
            String ext = tencentSmsRequest.getExt();
            String business = tencentSmsRequest.getBusiness();
            switch (business){
                case "实习":
                    smsMultiSender = new SmsMultiSender(Integer.parseInt(smsProperties.getExperienceAppid()),smsProperties.getExperienceSecurityKey());
                    break;
                case "体验":
                    smsMultiSender = new SmsMultiSender(Integer.parseInt(smsProperties.getPricticeAppid()),smsProperties.getPricticeSecurityKey());
                    break;
                case "大学":
                    smsMultiSender = new SmsMultiSender(Integer.parseInt(smsProperties.getCollegeAppid()),smsProperties.getCollegeSecurityKey());
                    break;
                default:
                    smsMultiSender = new SmsMultiSender(Integer.parseInt(smsProperties.getCollegeAppid()),smsProperties.getCollegeSecurityKey());
            }
            try {
                return smsMultiSender.send(type,nationCode, phoneNumber, msg, extend, ext);
            } catch (HTTPException e) {
                throw new SmsException(e);
            } catch (IOException e) {
                throw new SmsException(e);
            }
        }
        throw new IllegalStateException(SYS_TYPE_CONFIG_ERROR_MSG);
    }

    @Override
    public Object sendBatchTemplateSms(String tempalteId, BaseRequest params) throws SmsException {
        if (params instanceof TencentSmsRequest){
            TencentSmsRequest tencentSmsRequest = (TencentSmsRequest) params;
            String nationCode = tencentSmsRequest.getNationCode();
            String[] phoneNumber = tencentSmsRequest.getPhoneNumber();
            int templateId = Integer.parseInt(tempalteId);
            String[] params1 = tencentSmsRequest.getParams();
            String business = tencentSmsRequest.getBusiness();
            String sign = "";
            switch (business){
                case "实习":
                    smsMultiSender = new SmsMultiSender(Integer.parseInt(smsProperties.getExperienceAppid()),smsProperties.getExperienceSecurityKey());
                    sign = smsProperties.getPricticeSign();
                    break;
                case "体验":
                    smsMultiSender = new SmsMultiSender(Integer.parseInt(smsProperties.getPricticeAppid()),smsProperties.getPricticeSecurityKey());
                    sign = smsProperties.getExperienceSign();
                    break;
                case "大学":
                    smsMultiSender = new SmsMultiSender(Integer.parseInt(smsProperties.getCollegeAppid()),smsProperties.getCollegeSecurityKey());
                    sign = smsProperties.getCollegeSign();
                    break;
                default:
                    smsMultiSender = new SmsMultiSender(Integer.parseInt(smsProperties.getCollegeAppid()),smsProperties.getCollegeSecurityKey());
                    sign = smsProperties.getCollegeSign();
            }
            String extend = tencentSmsRequest.getExtend();
            String ext = tencentSmsRequest.getExt();
            try {
                return smsMultiSender.sendWithParam(nationCode, phoneNumber, templateId,
                        params1, sign, extend, ext);
            } catch (HTTPException e) {
                throw new SmsException(e);
            } catch (IOException e) {
                throw new SmsException(e);
            }
        }
        throw new IllegalStateException(SYS_TYPE_CONFIG_ERROR_MSG);
    }
}
