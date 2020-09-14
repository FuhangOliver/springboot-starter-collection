package com.ciwei.consul;

import com.google.common.net.HostAndPort;
import com.orbitz.consul.AgentClient;
import com.orbitz.consul.Consul;
import com.orbitz.consul.model.agent.ImmutableRegCheck;
import com.orbitz.consul.model.agent.ImmutableRegistration;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Collections;

public class ConsulRegisterService {

    private final static Logger logger = LoggerFactory.getLogger(ConsulRegisterService.class);

    @Autowired
    ConsulProperties consulProperties;

    private Consul buildConsul(String hostIP, int hostPort){
        try{
            return  Consul.builder()
                    .withConnectTimeoutMillis(20*1000)
                    .withReadTimeoutMillis(20*1000)
                    .withHostAndPort(HostAndPort.fromString(hostIP+":"+hostPort))
                    .build();
        }catch (Exception e){
            logger.error(e.toString());
            return null;
        }
    }

    public Boolean register(String registerName, String consulIP, int consulPort){

        Consul consul = this.buildConsul(consulIP, consulPort);
        if(consul==null){
            logger.error("register failed. consulServer is not found @[{}:{}]",consulIP,consulPort);
            return false;
        }
        AgentClient agent = consul.agentClient();

        String microServiceIP;
        int port = consulProperties.getProvidePort();
        String provideIP = consulProperties.getProvideIP();
        //获取微服务应用IP
        if(StringUtils.isEmpty(provideIP) || "0.0.0.0".equals(provideIP)){
            microServiceIP = LocalAddr.getLocalIP();
        }else{
            microServiceIP = provideIP;
        }

        ImmutableRegCheck check = ImmutableRegCheck.builder().tcp(microServiceIP+":"+port).interval(consulProperties.getHeartbeat()+"s").build();
        ImmutableRegistration.Builder builder = ImmutableRegistration.builder();
        builder.id(registerName+"-"+microServiceIP+"."+port).name(registerName).
                tags(Collections.singletonList("tag1")).address(microServiceIP).port(port).addChecks(check);
        agent.register(builder.build());

        logger.info("{}服务注册ip:{} 端口号{} to consul[{}:{}]", registerName, microServiceIP, port, consulIP, consulPort);

        return true;
    }

    public void register(String registerName) {
        if(consulProperties.getRegisterHost() == null){
            logger.error("consulHost config is not existed!!!!");
            return;
        }

        String[] consulList =  consulProperties.getRegisterHost().split(",");

        String[] hostPort = null;
        for(int i=0; i<consulList.length;i++){
            hostPort = consulList[i].split(":");
            switch(hostPort.length){
                case 1:
                    new ConsulRegThread(this,registerName,hostPort[0].trim(),8500)
                            .start();
                    break;
                case 2:
                    new ConsulRegThread(this,registerName,hostPort[0].trim(),Integer.valueOf(hostPort[1].trim()))
                            .start();
                    break;
                default:
                    logger.error("consulHost config error: {}", consulProperties.getRegisterHost());
                    break;
            }
        }
    }
}
