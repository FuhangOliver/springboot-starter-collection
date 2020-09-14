package com.ciwei.thriftServer;

import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;

@Configurable
@EnableConfigurationProperties({ThriftServerProperties.class,ThriftServerThreadPoolProperties.class})
public class ThriftServerConfiguration {

    @Bean
    public ThriftServerStarter pmsThriftServer(){
        return new ThriftServerStarter();
    }

    @Bean
    public CiweiRPC ciweiRPC(){
        return new CiweiRPC();
    }
}
