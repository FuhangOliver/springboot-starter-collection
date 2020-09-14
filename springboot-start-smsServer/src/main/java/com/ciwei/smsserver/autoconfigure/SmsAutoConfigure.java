package com.ciwei.smsserver.autoconfigure;

import com.aliyuncs.DefaultAcsClient;
import com.aliyuncs.IAcsClient;
import com.aliyuncs.profile.DefaultProfile;
import com.ciwei.smsserver.annotion.EnabledSmsAutoConfiguration;
import com.ciwei.smsserver.entity.SmsProperties;
import com.ciwei.smsserver.entity.SmsType;
import com.ciwei.smsserver.service.AliSmsService;
import com.ciwei.smsserver.service.SmsService;
import com.ciwei.smsserver.service.TencentSmsService;
import com.ciwei.smsserver.service.UcpassSmsService;
import com.ciwei.smsserver.ucpass.client.JsonReqClient;
import com.github.qcloudsms.SmsMultiSender;
import com.github.qcloudsms.SmsSingleSender;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 *功能描述
 * @author FuHang
 * @date 2019/8/5
 */
@Configuration
@ConditionalOnBean(annotation = EnabledSmsAutoConfiguration.class)
@EnableConfigurationProperties(SmsProperties.class)
public class SmsAutoConfigure {

    @Configuration
    @ConditionalOnClass({SmsSingleSender.class,SmsMultiSender.class})
    public static class TentcentSmsServiceConfiguration{

        @Bean
        public SmsService tencentSmsService(SmsProperties smsProperties){
            if(SmsType.TENCENT.equals(smsProperties.getSmsType())){
                return new TencentSmsService(smsProperties);
            }
            return null;
        }
    }

    @Configuration
    @ConditionalOnClass({IAcsClient.class})
    public static class AliSmsServiceConfiguration {

        @Bean
        public SmsService aliSmsService(SmsProperties smsProperties){
            if (SmsType.ALI.equals(smsProperties.getSmsType())){
                DefaultProfile defaultProfile = DefaultProfile.getProfile(smsProperties.getRegionId(),
                        smsProperties.getAccessKey(),smsProperties.getSecurityKey());
                return new AliSmsService(
                        new DefaultAcsClient(defaultProfile),
                        smsProperties);
            }
            return null;
        }
    }

    @Configuration
    @ConditionalOnClass({JsonReqClient.class})
    public static class UCPassSmsServiceConfiguration{

        @Bean
        public SmsService ucpassSmsService(SmsProperties smsProperties){
            if (SmsType.UCPASS.equals(smsProperties.getSmsType())){
                return new UcpassSmsService(
                    new JsonReqClient(smsProperties),
                    smsProperties);
            }
            return null;
        }
    }
}
