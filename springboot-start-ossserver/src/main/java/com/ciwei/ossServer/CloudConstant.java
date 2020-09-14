package com.ciwei.ossServer;

/**
 * @author fuhang
 * @description: 云服务商
 * @date 2020/8/29 16:37
 */
public enum CloudConstant {
    /**
     * 七牛云
     */
    QINIU(1),
    /**
     * 阿里云
     */
    ALIYUN(2),
    /**
     * 腾讯云
     */
    QCLOUD(3),
    /**
     * 服务器存储
     */
    DISCK(4);

    private int value;

    private CloudConstant(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }
}
