package com.ciwei.exception.notice.config;

import com.ciwei.exception.notice.aop.ExceptionNoticeAop;
import com.ciwei.exception.notice.handler.ExceptionNoticeHandler;
import com.ciwei.exception.notice.process.DingTalkNoticeProcessor;
import com.ciwei.exception.notice.process.INoticeProcessor;
import com.ciwei.exception.notice.process.MailNoticeProcessor;
import com.ciwei.exception.notice.process.WeChatNoticeProcessor;
import com.ciwei.exception.notice.properties.DingTalkProperties;
import com.ciwei.exception.notice.properties.ExceptionNoticeProperties;
import com.ciwei.exception.notice.properties.MailProperties;
import com.ciwei.exception.notice.properties.WeChatProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.mail.MailSender;
import org.springframework.util.Assert;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;

/**
 * 异常信息通知配置类
 *
 * @author FuHang
 */
@Configuration
@ConditionalOnProperty(prefix = ExceptionNoticeProperties.PREFIX, name = "enable", havingValue = "true")
@EnableConfigurationProperties(value = ExceptionNoticeProperties.class)
public class ExceptionNoticeAutoConfiguration {

    private final RestTemplate restTemplate = new RestTemplate();

    @Autowired(required = false)
    private MailSender mailSender;

    @Bean(initMethod = "start")
    public ExceptionNoticeHandler noticeHandler(ExceptionNoticeProperties properties) {
        List<INoticeProcessor> noticeProcessors = new ArrayList<>(2);
        INoticeProcessor noticeProcessor;
        DingTalkProperties dingTalkProperties = properties.getDingTalk();
        if (null != dingTalkProperties) {
            noticeProcessor = new DingTalkNoticeProcessor(restTemplate, dingTalkProperties);
            noticeProcessors.add(noticeProcessor);
        }
        WeChatProperties weChatProperties = properties.getWeChat();
        if (null != weChatProperties) {
            noticeProcessor = new WeChatNoticeProcessor(restTemplate, weChatProperties);
            noticeProcessors.add(noticeProcessor);
        }
        MailProperties email = properties.getMail();
        if (null != email && null != mailSender) {
            noticeProcessor = new MailNoticeProcessor(mailSender, email);
            noticeProcessors.add(noticeProcessor);
        }
        Assert.isTrue(noticeProcessors.size() != 0, "Exception notification configuration is incorrect");
        return new ExceptionNoticeHandler(properties, noticeProcessors);
    }

    @Bean
    @ConditionalOnBean(ExceptionNoticeHandler.class)
    public ExceptionNoticeAop exceptionListener(ExceptionNoticeHandler noticeHandler) {
        return new ExceptionNoticeAop(noticeHandler);
    }
}
