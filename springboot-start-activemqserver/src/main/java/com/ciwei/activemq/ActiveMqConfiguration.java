package com.ciwei.activemq;

import com.ciwei.activemq.producer.ProductService;
import com.ciwei.activemq.producer.Sender;
import com.ciwei.activemq.producer.impl.ProductServiceImpl;
import com.ciwei.activemq.producer.queue.QueueSender;
import com.ciwei.activemq.producer.topic.TopicSender;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.context.annotation.Bean;

/**
 * @author fuhang
 */
@Configurable
public class ActiveMqConfiguration {

    @Bean
    public QueueSender queueSenderServer(){
        return new QueueSender();
    }
    @Bean
    public TopicSender topicSender(){
        return new TopicSender();
    }
    @Bean
    public Sender sender(){
        return new Sender();
    }
    @Bean
    public ProductService productService(){return new ProductServiceImpl();}
}
