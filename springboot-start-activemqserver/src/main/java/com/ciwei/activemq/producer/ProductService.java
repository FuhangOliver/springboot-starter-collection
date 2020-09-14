package com.ciwei.activemq.producer;

import javax.jms.Destination;
import javax.jms.JMSException;

/**
 * @author FuHang
 * @date 2019/11/22
 */
public interface ProductService {

    void sendQueueMsg(String msg, Destination destination) throws JMSException;

    void sendQueueObjMsg(Object object,Destination destination) throws JMSException;

    void send2WayQueueMsg(String msg,Destination destination) throws JMSException;

    void sendACKQueueMsg(String msg,Destination destination) throws JMSException;

    void sendTopicMsg(String msg,Destination destination) throws JMSException;

    void sendTopicObjMsg(Object object,Destination destination) throws JMSException;

    void sendDelayTopicMsg(String msg,Destination destination) throws JMSException;

    void sendDelayTopicMsg(String msg, long time,Destination destination) throws JMSException;
}
