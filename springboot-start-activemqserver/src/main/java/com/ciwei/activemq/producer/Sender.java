package com.ciwei.activemq.producer;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.core.JmsMessagingTemplate;

import javax.jms.Destination;

public class Sender {

    @Autowired
    private JmsMessagingTemplate jmsMessagingTemplate;

    public void sendTemple(Destination destination, final String message) {
        jmsMessagingTemplate.convertAndSend(destination, message);
    }
    public void sendTemple(Destination destination, final Object message) {
        jmsMessagingTemplate.convertAndSend(destination, message);
    }
}
