package com.ciwei.ossServer;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.NestedConfigurationProperty;

import java.io.Serializable;

/**
 * @author fuhang
 * @description: 云存储配置信息
 * @date 2020/8/29 16:37
 */
@ConfigurationProperties("oss")
public class CloudStorageConfig implements Serializable {
    private static final long serialVersionUID = 1L;

    //类型 1：七牛  2：阿里云  3：腾讯云  4：服务器存储
    @NestedConfigurationProperty
    private Integer type = CloudConstant.QINIU.getValue();
    //上传文件的大小限制
    @NestedConfigurationProperty
    private Integer maxFileSize = 1024;
    //七牛绑定的域名
    @NestedConfigurationProperty
    private String qiniuDomain = "https://img.shixijob.net";
    //七牛路径前缀
    private String qiniuPrefix;
    //七牛ACCESS_KEY
    private String qiniuAccessKey;
    //七牛SECRET_KEY
    private String qiniuSecretKey;
    //七牛存储空间名
    private String qiniuBucketName;

    //阿里云绑定的域名
    private String aliyunDomain;
    //阿里云路径前缀
    private String aliyunPrefix;
    //阿里云EndPoint
    private String aliyunEndPoint;
    //阿里云AccessKeyId
    private String aliyunAccessKeyId;
    //阿里云AccessKeySecret
    private String aliyunAccessKeySecret;
    //阿里云BucketName
    private String aliyunBucketName;

    //腾讯云绑定的域名
    private String qcloudDomain;
    //腾讯云路径前缀
    private String qcloudPrefix;
    //腾讯云AppId
    private Integer qcloudAppId;
    //腾讯云SecretId
    private String qcloudSecretId;
    //腾讯云SecretKey
    private String qcloudSecretKey;
    //腾讯云BucketName
    private String qcloudBucketName;
    //腾讯云COS所属地区
    private String qcloudRegion;

    //服务器存储
    private String diskPath;
    //本地存储代理服务器不能为空
    private String proxyServer;

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public Integer getMaxFileSize() {
        return maxFileSize;
    }

    public void setMaxFileSize(Integer maxFileSize) {
        this.maxFileSize = maxFileSize;
    }

    public String getQiniuDomain() {
        return qiniuDomain;
    }

    public void setQiniuDomain(String qiniuDomain) {
        this.qiniuDomain = qiniuDomain;
    }

    public String getQiniuPrefix() {
        return qiniuPrefix;
    }

    public void setQiniuPrefix(String qiniuPrefix) {
        this.qiniuPrefix = qiniuPrefix;
    }

    public String getQiniuAccessKey() {
        return qiniuAccessKey;
    }

    public void setQiniuAccessKey(String qiniuAccessKey) {
        this.qiniuAccessKey = qiniuAccessKey;
    }

    public String getQiniuSecretKey() {
        return qiniuSecretKey;
    }

    public void setQiniuSecretKey(String qiniuSecretKey) {
        this.qiniuSecretKey = qiniuSecretKey;
    }

    public String getQiniuBucketName() {
        return qiniuBucketName;
    }

    public void setQiniuBucketName(String qiniuBucketName) {
        this.qiniuBucketName = qiniuBucketName;
    }

    public String getAliyunDomain() {
        return aliyunDomain;
    }

    public void setAliyunDomain(String aliyunDomain) {
        this.aliyunDomain = aliyunDomain;
    }

    public String getAliyunPrefix() {
        return aliyunPrefix;
    }

    public void setAliyunPrefix(String aliyunPrefix) {
        this.aliyunPrefix = aliyunPrefix;
    }

    public String getAliyunEndPoint() {
        return aliyunEndPoint;
    }

    public void setAliyunEndPoint(String aliyunEndPoint) {
        this.aliyunEndPoint = aliyunEndPoint;
    }

    public String getAliyunAccessKeyId() {
        return aliyunAccessKeyId;
    }

    public void setAliyunAccessKeyId(String aliyunAccessKeyId) {
        this.aliyunAccessKeyId = aliyunAccessKeyId;
    }

    public String getAliyunAccessKeySecret() {
        return aliyunAccessKeySecret;
    }

    public void setAliyunAccessKeySecret(String aliyunAccessKeySecret) {
        this.aliyunAccessKeySecret = aliyunAccessKeySecret;
    }

    public String getAliyunBucketName() {
        return aliyunBucketName;
    }

    public void setAliyunBucketName(String aliyunBucketName) {
        this.aliyunBucketName = aliyunBucketName;
    }

    public String getQcloudDomain() {
        return qcloudDomain;
    }

    public void setQcloudDomain(String qcloudDomain) {
        this.qcloudDomain = qcloudDomain;
    }

    public String getQcloudPrefix() {
        return qcloudPrefix;
    }

    public void setQcloudPrefix(String qcloudPrefix) {
        this.qcloudPrefix = qcloudPrefix;
    }

    public Integer getQcloudAppId() {
        return qcloudAppId;
    }

    public void setQcloudAppId(Integer qcloudAppId) {
        this.qcloudAppId = qcloudAppId;
    }

    public String getQcloudSecretId() {
        return qcloudSecretId;
    }

    public void setQcloudSecretId(String qcloudSecretId) {
        this.qcloudSecretId = qcloudSecretId;
    }

    public String getQcloudSecretKey() {
        return qcloudSecretKey;
    }

    public void setQcloudSecretKey(String qcloudSecretKey) {
        this.qcloudSecretKey = qcloudSecretKey;
    }

    public String getQcloudBucketName() {
        return qcloudBucketName;
    }

    public void setQcloudBucketName(String qcloudBucketName) {
        this.qcloudBucketName = qcloudBucketName;
    }

    public String getQcloudRegion() {
        return qcloudRegion;
    }

    public void setQcloudRegion(String qcloudRegion) {
        this.qcloudRegion = qcloudRegion;
    }

    public String getDiskPath() {
        return diskPath;
    }

    public void setDiskPath(String diskPath) {
        this.diskPath = diskPath;
    }

    public String getProxyServer() {
        return proxyServer;
    }

    public void setProxyServer(String proxyServer) {
        this.proxyServer = proxyServer;
    }

    @Override
    public String toString() {
        return "CloudStorageConfig{" +
                "type=" + type +
                ", maxFileSize=" + maxFileSize +
                ", qiniuDomain='" + qiniuDomain + '\'' +
                ", qiniuPrefix='" + qiniuPrefix + '\'' +
                ", qiniuAccessKey='" + qiniuAccessKey + '\'' +
                ", qiniuSecretKey='" + qiniuSecretKey + '\'' +
                ", qiniuBucketName='" + qiniuBucketName + '\'' +
                ", aliyunDomain='" + aliyunDomain + '\'' +
                ", aliyunPrefix='" + aliyunPrefix + '\'' +
                ", aliyunEndPoint='" + aliyunEndPoint + '\'' +
                ", aliyunAccessKeyId='" + aliyunAccessKeyId + '\'' +
                ", aliyunAccessKeySecret='" + aliyunAccessKeySecret + '\'' +
                ", aliyunBucketName='" + aliyunBucketName + '\'' +
                ", qcloudDomain='" + qcloudDomain + '\'' +
                ", qcloudPrefix='" + qcloudPrefix + '\'' +
                ", qcloudAppId=" + qcloudAppId +
                ", qcloudSecretId='" + qcloudSecretId + '\'' +
                ", qcloudSecretKey='" + qcloudSecretKey + '\'' +
                ", qcloudBucketName='" + qcloudBucketName + '\'' +
                ", qcloudRegion='" + qcloudRegion + '\'' +
                ", diskPath='" + diskPath + '\'' +
                ", proxyServer='" + proxyServer + '\'' +
                '}';
    }
}
