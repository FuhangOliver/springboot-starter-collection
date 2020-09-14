package com.ciwei.smsserver.entity;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.NestedConfigurationProperty;

/**
 *功能描述
 * @author FuHang
 * @date 2019/8/5
 */
@ConfigurationProperties(prefix = "spring.ciwei.sms")
public class SmsProperties {

    @NestedConfigurationProperty
    private SmsType smsType = SmsType.ALI;

    /**
     * 短信应用商服务地址
     */
    private String appid;
    private String collegeAppid;
    private String experienceAppid;
    private String pricticeAppid;

    /**
     * 短信服务商应用公钥
     */
    private String accessKey;

    /**
     *  短信服务商应用私钥
     */
    private String securityKey;
    private String collegeSecurityKey;
    private String experienceSecurityKey;
    private String pricticeSecurityKey;

    /**
     * 阿里云短信特有-区域id
     */
    private String regionId;

    /**
     * 短信服务商短信签名
     */
    private String sign;
    private String collegeSign;
    private String experienceSign;
    private String pricticeSign;

    /**
     *  云之讯服务地址
     */
    private String restSserver;

    public SmsType getSmsType() {
        return smsType;
    }

    public void setSmsType(SmsType smsType) {
        this.smsType = smsType;
    }

    public String getAccessKey() {
        return accessKey;
    }

    public void setAccessKey(String accessKey) {
        this.accessKey = accessKey;
    }

    public String getRegionId() {
        return regionId;
    }

    public void setRegionId(String regionId) {
        this.regionId = regionId;
    }

    public String getRestSserver() {
        return restSserver;
    }

    public void setRestSserver(String restSserver) {
        this.restSserver = restSserver;
    }

    public String getCollegeAppid() {
        return collegeAppid;
    }

    public void setCollegeAppid(String collegeAppid) {
        this.collegeAppid = collegeAppid;
    }

    public String getExperienceAppid() {
        return experienceAppid;
    }

    public void setExperienceAppid(String experienceAppid) {
        this.experienceAppid = experienceAppid;
    }

    public String getPricticeAppid() {
        return pricticeAppid;
    }

    public void setPricticeAppid(String pricticeAppid) {
        this.pricticeAppid = pricticeAppid;
    }

    public String getCollegeSecurityKey() {
        return collegeSecurityKey;
    }

    public void setCollegeSecurityKey(String collegeSecurityKey) {
        this.collegeSecurityKey = collegeSecurityKey;
    }

    public String getExperienceSecurityKey() {
        return experienceSecurityKey;
    }

    public void setExperienceSecurityKey(String experienceSecurityKey) {
        this.experienceSecurityKey = experienceSecurityKey;
    }

    public String getPricticeSecurityKey() {
        return pricticeSecurityKey;
    }

    public void setPricticeSecurityKey(String pricticeSecurityKey) {
        this.pricticeSecurityKey = pricticeSecurityKey;
    }

    public String getCollegeSign() {
        return collegeSign;
    }

    public void setCollegeSign(String collegeSign) {
        this.collegeSign = collegeSign;
    }

    public String getExperienceSign() {
        return experienceSign;
    }

    public void setExperienceSign(String experienceSign) {
        this.experienceSign = experienceSign;
    }

    public String getPricticeSign() {
        return pricticeSign;
    }

    public void setPricticeSign(String pricticeSign) {
        this.pricticeSign = pricticeSign;
    }

    public String getAppid() {
        return appid;
    }

    public void setAppid(String appid) {
        this.appid = appid;
    }

    public String getSecurityKey() {
        return securityKey;
    }

    public void setSecurityKey(String securityKey) {
        this.securityKey = securityKey;
    }

    public String getSign() {
        return sign;
    }

    public void setSign(String sign) {
        this.sign = sign;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("{");
        sb.append("\"smsType\":").append(smsType);
        sb.append(",\"appid\":\"").append(pricticeAppid+collegeAppid+experienceAppid).append('\"');
        sb.append(",\"accessKey\":\"").append(accessKey).append('\"');
        sb.append(",\"securityKey\":\"").append(pricticeSecurityKey+experienceSecurityKey+collegeSecurityKey).append('\"');
        sb.append(",\"regionId\":\"").append(regionId).append('\"');
        sb.append(",\"sign\":\"").append(pricticeSign+experienceSign+collegeSign).append('\"');
        sb.append(",\"restSserver\":\"").append(restSserver).append('\"');
        sb.append("}");
        return sb.toString();
    }
}
