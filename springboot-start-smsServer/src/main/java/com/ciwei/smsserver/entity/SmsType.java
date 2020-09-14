package com.ciwei.smsserver.entity;

/**
 *功能描述
 * @author FuHang
 * @date 2019/8/5
 */
public enum SmsType {

    /**
     * 阿里短信服务
     */
    ALI("ali","阿里短信服务"),

    /**
     * 腾讯短信服务
     */
    TENCENT("tencent","腾讯短信服务"),

    /**
     * 云之讯短信平台
     */
    UCPASS("ucpass", "云之讯短信服务");

    private String type;

    private String desc;

    SmsType(String type, String desc) {
        this.type = type;
        this.desc = desc;
    }

    public String getType() {
        return type;
    }

    public String getDesc() {
        return desc;
    }

    public SmsType getSmsTypeByType(String type){
        if (type == null || type.trim().length()<1){
            return null;
        }
        for (SmsType smsType:values()) {
            if (smsType.getType().equalsIgnoreCase(type)){
                return smsType;
            }
        }
        return null;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("{");
        sb.append("\"type\":\"")
                .append(type).append('\"');
        sb.append(",\"desc\":\"")
                .append(desc).append('\"');
        sb.append('}');
        return sb.toString();
    }
}
