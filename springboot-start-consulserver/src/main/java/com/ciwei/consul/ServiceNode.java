package com.ciwei.consul;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author liuqi
 * @date 2019/12/23 15:42
 * @desc consul每个节点对应的微服务名称和对应的url集合以及 每个微服务调用的url集合
 */
public class ServiceNode {

    private final static Logger logger = LoggerFactory.getLogger(ServiceNode.class);

    private String serviceName;

    private List<RpcServiceUrl> rpcServiceUrlList;

    private int index;

    public ServiceNode(String serviceName){
        this.serviceName = serviceName;
        this.index = 0;
        this.rpcServiceUrlList = new ArrayList<>();
    }

    public ServiceNode(String serviceName, List<RpcServiceUrl> rpcServiceUrlList){
        this.serviceName = serviceName;
        this.rpcServiceUrlList = rpcServiceUrlList;
        this.index = 0;
    }

    public void addRpcServiceUrl(RpcServiceUrl rpcServiceUrl){
        this.rpcServiceUrlList.add(rpcServiceUrl);
    }

    public RpcServiceUrl getServiceUrl(Boolean isUpdate){
        int i = index;
        if (isUpdate) {
            index = (index + 1) % rpcServiceUrlList.size();
        }
        return rpcServiceUrlList.get(i);
    }


    public List<RpcServiceUrl> getRpcServiceUrlList() {
        return rpcServiceUrlList;
    }

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }




    private static ConcurrentHashMap<String, ServiceNode> serviceNodeMap = new ConcurrentHashMap<>();
    public static ServiceNode getServiceNode(String serviceName){
         return serviceNodeMap.get(serviceName);
    }
    public static void putServiceNode(String serviceName, ServiceNode serviceNode){
         serviceNodeMap.put(serviceName,serviceNode);
    }
    public static byte[] lock = new byte[0];
    public static void rmServiceNode(String serviceName){
        synchronized(lock) {
            if(serviceNodeMap.containsKey(serviceName)){
                serviceNodeMap.remove(serviceName);
            }
        }
    }



}
