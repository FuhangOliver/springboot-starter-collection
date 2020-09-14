package com.ciwei.thriftServer;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties("consul")
public class ThriftServerProperties {

    private int providePort;

    public int getProvidePort() {
        return providePort;
    }

    public void setProvidePort(int providePort) {
        this.providePort = providePort;
    }
}
