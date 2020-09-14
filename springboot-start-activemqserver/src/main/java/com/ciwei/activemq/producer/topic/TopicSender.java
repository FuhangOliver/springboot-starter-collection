package com.ciwei.activemq.producer.topic;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jms.core.JmsTemplate;

import javax.jms.BytesMessage;
import javax.jms.MapMessage;
import java.io.Serializable;

public class TopicSender {
    private final Logger logger = LoggerFactory.getLogger(TopicSender.class);
    
    @Autowired
    @Qualifier("jmsTopicTemplate")
    private JmsTemplate jmsTemplate;

    /**
     * 向指定Destination发送text消息
     * @param message
     */
    public void sendTxtMessage(String topicName,final String message){
        if(null == topicName){
            return;
        }
        jmsTemplate.send(topicName, session -> session.createTextMessage(message));
    }

    /**
     * 向指定Destination发送map消息
     * @param message
     */
    public void sendMapMessage(String topicName,final String message){
        if(null == topicName){
            return;
        }
        jmsTemplate.send(topicName, session -> {
            MapMessage mapMessage = session.createMapMessage();
            mapMessage.setString("msgId",message);
            return mapMessage;
        });
    }

    /**
     * 向指定Destination发送序列化的对象
     * @param object object 必须序列化
     */
    public void sendObjectMessage(String topicName,final Serializable object){
        if(null == topicName){
            return;
        }
        jmsTemplate.send(topicName, session -> session.createObjectMessage(object));
    }

    /**
     * 向指定Destination发送字节消息
     * @param bytes
     */
    public void sendBytesMessage(String topicName,final byte[] bytes){
        if(null == topicName){
            return;
        }
        jmsTemplate.send(topicName, session -> {
            BytesMessage bytesMessage = session.createBytesMessage();
            bytesMessage.writeBytes(bytes);
            return bytesMessage;

        });
    }

}
