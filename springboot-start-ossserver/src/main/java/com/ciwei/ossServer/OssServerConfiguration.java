package com.ciwei.ossServer;

import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configurable
@EnableConfigurationProperties({CloudStorageConfig.class})
public class OssServerConfiguration {

    @Configuration
    public static class QiNiuCloudStorageServiceConfiguration{
        @Bean
        public QiniuCloudStorageService qiniuCloudStorageService(CloudStorageConfig  cloudStorageConfig){
            return new QiniuCloudStorageService(cloudStorageConfig);
        }
    }

    @Configuration
    public static class AliYunCloudStorageServiceConfiguration{
        @Bean
        public AliyunCloudStorageService aliyunCloudStorageService(CloudStorageConfig  cloudStorageConfig){
            if (CloudConstant.ALIYUN.getValue() == cloudStorageConfig.getType()){
                return new AliyunCloudStorageService(cloudStorageConfig);
            }
            return null;
        }
    }

    @Configuration
    public static class QCloudCloudStorageServiceConfiguration{
        @Bean
        public QcloudCloudStorageService qcloudCloudStorageService(CloudStorageConfig  cloudStorageConfig){
            if (CloudConstant.QCLOUD.getValue() == cloudStorageConfig.getType()){
                return new QcloudCloudStorageService(cloudStorageConfig);
            }
            return null;
        }
    }

    @Configuration
    public static class DiskCloudStorageServiceConfiguration{
        @Bean
        public DiskCloudStorageService diskCloudStorageService(CloudStorageConfig  cloudStorageConfig){
            if (CloudConstant.DISCK.getValue() == cloudStorageConfig.getType()){
                return new DiskCloudStorageService(cloudStorageConfig);
            }
            return null;
        }
    }
}
