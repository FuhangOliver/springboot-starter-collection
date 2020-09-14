package com.ciwei.activemq.producer.impl;

import com.ciwei.activemq.producer.ProductService;
import org.apache.activemq.ScheduledMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.core.JmsMessagingTemplate;
import org.springframework.stereotype.Service;

import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.TextMessage;


/**
 * @author zhanghang
 * @date 2019/6/11
 */
@Service
public class ProductServiceImpl implements ProductService {
    private final Logger logger = LoggerFactory.getLogger(ProductServiceImpl.class);

    @Autowired
    private JmsMessagingTemplate jmsMessagingTemplate;


    @Override
    public void sendQueueMsg(String msg,Destination destination) throws JMSException {
        this.jmsMessagingTemplate.convertAndSend(destination,msg);
    }

    @Override
    public void sendQueueObjMsg(Object object, Destination destination) throws JMSException {
        this.jmsMessagingTemplate.convertAndSend(destination,object);
    }

    @Override
    public void send2WayQueueMsg(String msg,Destination destination) throws JMSException {
        this.jmsMessagingTemplate.convertAndSend(destination,msg);
    }

    @Override
    public void sendACKQueueMsg(String msg,Destination destination) throws JMSException {
        this.jmsMessagingTemplate.convertAndSend(destination,msg);
    }
    
    @Override
    public void sendTopicMsg(String msg,Destination destination) throws JMSException {
        this.jmsMessagingTemplate.convertAndSend(destination,msg);
    }

    @Override
    public void sendTopicObjMsg(Object object,Destination destination) throws JMSException {
        this.jmsMessagingTemplate.convertAndSend(destination,object);
    }

    /**
     * 使用延时队列需要在activemq.xml中的<broker></broker>标签里添加schedulerSupport="true"，如下：
     * <broker xmlns="http://activemq.apache.org/schema/core" brokerName="localhost" dataDirectory="${activemq.data}" schedulerSupport="true"></broker>
     * @return
     */
    @Override
    public void sendDelayTopicMsg(String msg,Destination destination) throws JMSException {
        this.jmsMessagingTemplate.getJmsTemplate().send(destination, session -> {
            TextMessage tx = session.createTextMessage(msg);
            tx.setLongProperty(ScheduledMessage.AMQ_SCHEDULED_DELAY,10 * 1000);
            return tx;
        });
    }

    /**
     * 使用延时队列需要在activemq.xml中的<broker></broker>标签里添加schedulerSupport="true"，如下：
     * <broker xmlns="http://activemq.apache.org/schema/core" brokerName="localhost" dataDirectory="${activemq.data}" schedulerSupport="true"></broker>
     * @return
     */
    @Override
    public void sendDelayTopicMsg(String msg,long time,Destination destination) throws JMSException {
        this.jmsMessagingTemplate.getJmsTemplate().send(destination, session -> {
            TextMessage tx = session.createTextMessage(msg);
            tx.setLongProperty(ScheduledMessage.AMQ_SCHEDULED_DELAY,time);
            return tx;
        });
    }

}
