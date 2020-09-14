package com.ciwei.consul;

import com.ciwei.consul.exception.ConsulServiceException;
import com.google.common.net.HostAndPort;
import com.orbitz.consul.Consul;
import com.orbitz.consul.HealthClient;
import com.orbitz.consul.cache.ServiceHealthCache;
import com.orbitz.consul.model.ConsulResponse;
import com.orbitz.consul.model.health.ImmutableServiceHealth;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class ConsulDiscoveryService{

    private final static Logger logger = LoggerFactory.getLogger(ConsulDiscoveryService.class);
    public static byte[] lock = new byte[0];

    @Autowired
    private ConsulProperties consulProperties;

    private Consul consul;

    public Consul getConsul(){
        if (this.consul == null) {
            this.consul = Consul.builder().withConnectTimeoutMillis(20 * 1000).withReadTimeoutMillis(20 * 1000)
                    .withHostAndPort(HostAndPort.fromString(consulProperties.getSearchHost())).build();
        }
        return this.consul;
    }

    //通过微服务名称获取 对应的url
    public RpcServiceUrl getRpcServiceUrl(String microServiceName){
        ServiceNode serviceNode = ServiceNode.getServiceNode(microServiceName);
        if(serviceNode == null){
            synchronized (lock){
                //去consul服务器下载微服务名称对应的url地址 push到rpcServiceUrlMap里面
                downloadServiceUrlByConsul(microServiceName);
                serviceNode = ServiceNode.getServiceNode(microServiceName);
                if(serviceNode == null || serviceNode.getRpcServiceUrlList().isEmpty()){
                    logger.error("consul 上未发现"+ microServiceName +"服务");
                    throw new ConsulServiceException();
                }
            }
        }
        return serviceNode.getServiceUrl(true);
    }

    //从consul服务器下载微服务对应的RPC服务路径
    private void downloadServiceUrlByConsul(String microServiceName){
        HealthClient consulClient = getConsul().healthClient();
        ConsulResponse object= consulClient.getHealthyServiceInstances(microServiceName);
        if(object.getResponse() != null){
            //下载之前先clear掉对应的微服务信息
            if(ServiceNode.getServiceNode(microServiceName) != null){
                ServiceNode.rmServiceNode(microServiceName);
            }
            List<ImmutableServiceHealth> serviceHealths = (List<ImmutableServiceHealth>)object.getResponse();
            serviceHealths.forEach(healthService ->{
                RpcServiceUrl rpcServiceUrl = new RpcServiceUrl(healthService.getService().getAddress(), healthService.getService().getPort());
                //一个node对应一个微服务的分布式集群 serviceName -> rpcServiceUrlList
                ServiceNode serviceNode = ServiceNode.getServiceNode(microServiceName);
                if(serviceNode == null){
                    serviceNode = new ServiceNode(microServiceName);
                    ServiceNode.putServiceNode(microServiceName, serviceNode);
                }
                serviceNode.addRpcServiceUrl(rpcServiceUrl);
            });
            //consul的监听异步操作
            try {
                ServiceHealthCache serviceHealthCache = ServiceHealthCache.newCache(consulClient, microServiceName);
                serviceHealthCache.addListener(map -> {
                    logger.info("监听到微服务{}的变化 {}", microServiceName, map);
                    if(map !=null){
                        ArrayList rpcServiceUrlList = new ArrayList<RpcServiceUrl>();
                        map.forEach((k,v)->{
                            RpcServiceUrl rpcServiceUrl = new RpcServiceUrl(v.getService().getAddress(), v.getService().getPort());
                            rpcServiceUrlList.add(rpcServiceUrl);
                        });
                        ServiceNode.putServiceNode(microServiceName, new ServiceNode(microServiceName, rpcServiceUrlList));
                    }
                });
                serviceHealthCache.start();
            } catch (Exception e) {
                logger.info("serviceHealthCache.start error:",e);
            }
        }
    }
}
