package com.ciwei.thriftServer;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.NestedConfigurationProperty;

/**
 * @author fuhang
 * @description: TODO
 * @date 2019/9/516:11
 */
@ConfigurationProperties("thrift")
public class ThriftServerThreadPoolProperties {
    @NestedConfigurationProperty
    private int selectorThreads = 2;

    @NestedConfigurationProperty
    private int maxWorkerThreads = 20;

    public int getMinWorkerThreads() {
        return selectorThreads;
    }

    public void setMinWorkerThreads(int minWorkerThreads) {
        this.selectorThreads = minWorkerThreads;
    }

    public int getMaxWorkerThreads() {
        return maxWorkerThreads;
    }

    public void setMaxWorkerThreads(int maxWorkerThreads) {
        this.maxWorkerThreads = maxWorkerThreads;
    }

    @Override
    public String toString() {
        return "ThriftServerThreadPoolProperties{" +
                "selectorThreads=" + selectorThreads +
                ", maxWorkerThreads=" + maxWorkerThreads +
                '}';
    }
}
