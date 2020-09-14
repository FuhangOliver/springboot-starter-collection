package com.ciwei.consul;

import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;

@Configurable
@EnableConfigurationProperties(ConsulProperties.class)
public class ConsulConfiguration {

    @Bean
    public ConsulRegisterService consulRegisterService(){
        return new ConsulRegisterService();
    }

    @Bean
    public ConsulDiscoveryService consulDiscoveryService(){
        return new ConsulDiscoveryService();
    }

}
