package com.ciwei.activemq.config;

import org.apache.activemq.ActiveMQSession;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.jms.annotation.EnableJms;
import org.springframework.jms.config.DefaultJmsListenerContainerFactory;
import org.springframework.jms.config.JmsListenerContainerFactory;
import org.springframework.jms.core.JmsTemplate;

import javax.jms.ConnectionFactory;

@Configuration
@EnableJms
public class JmsConfig {
    @Bean
    @Primary
    public JmsTemplate jmsQueueTemplate(ConnectionFactory connectionFactory) {
        JmsTemplate jmsTemplate = new JmsTemplate(connectionFactory);
        jmsTemplate.setPubSubDomain(false);
        return jmsTemplate;
    }

    @Bean
    public JmsTemplate jmsTopicTemplate(ConnectionFactory connectionFactory) {
        JmsTemplate jmsTemplate = new JmsTemplate(connectionFactory);
        jmsTemplate.setPubSubDomain(true);
        return jmsTemplate;
    }

    @Bean
    public JmsListenerContainerFactory<?> queueListenerFactory(ConnectionFactory connectionFactory) {
        DefaultJmsListenerContainerFactory factory = new DefaultJmsListenerContainerFactory();
        factory.setPubSubDomain(false);
        factory.setConnectionFactory(connectionFactory);
        return factory;
    }

    @Bean
    public JmsListenerContainerFactory<?> queueListenerACKFactory(ConnectionFactory connectionFactory) {
        DefaultJmsListenerContainerFactory factory = new DefaultJmsListenerContainerFactory();
        factory.setPubSubDomain(false);
        factory.setConnectionFactory(connectionFactory);
        /**
         * ack设置为activemq独有的单条确认模式：4,
         * 至于为什么不设置为客户端手动确认：2,因为客户端手动确认会失效,
         * 原因为spring框架会判断是否是2，如果是2会spring框架会帮你确认,
         * 可查看AbstractMessageListenerContainer[commitIfNecessary()]
         */
        factory.setSessionAcknowledgeMode(ActiveMQSession.INDIVIDUAL_ACKNOWLEDGE);
        return factory;
    }

    @Bean
    public JmsListenerContainerFactory<?> topicListenerFactory(ConnectionFactory connectionFactory) {
        DefaultJmsListenerContainerFactory factory = new DefaultJmsListenerContainerFactory();
        factory.setPubSubDomain(true);
        factory.setConcurrency("1");
        factory.setConnectionFactory(connectionFactory);
        return factory;
    }
}

