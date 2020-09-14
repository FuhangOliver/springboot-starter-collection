package com.ciwei.consul;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties("consul")
public class ConsulProperties {

    private String registerHost;//consul agent_client服务器列表，格式：192.168.1.2:8500,192.168.1.3:8500

    private String searchHost;//consul查询服务器， 可以配置为负载均衡器IP和端口号，如：192.168.1.7:8500

    private int heartbeat;//consul心跳时间

    private int providePort;//服务提供者端口

    private String provideIP;//服务提供者IP

    private String provideName;//服务提供者名称

    private String provideId;//服务提供者ID

    public int getHeartbeat() {
        return heartbeat;
    }

    public void setHeartbeat(int heartbeat) {
        this.heartbeat = heartbeat;
    }

    public int getProvidePort() {
        return providePort;
    }

    public void setProvidePort(int providePort) {
        this.providePort = providePort;
    }

    public String getProvideName() {
        return provideName;
    }

    public void setProvideName(String provideName) {
        this.provideName = provideName;
    }

    public String getProvideId() {
        return provideId;
    }

    public void setProvideId(String provideId) {
        this.provideId = provideId;
    }

    public String getRegisterHost() {
        return registerHost;
    }

    public void setRegisterHost(String registerHost) {
        this.registerHost = registerHost;
    }

    public String getSearchHost() {
        if(searchHost==null)return null;

        //补充缺省端口号
        if(searchHost.split(":").length<2)
            searchHost = searchHost.trim()+":8500";
        return searchHost;
    }

    public void setSearchHost(String searchHost) {
        this.searchHost = searchHost;
    }

    public String getProvideIP() {
        return provideIP;
    }

    public void setProvideIP(String provideIP) {
        this.provideIP = provideIP;
    }
}
