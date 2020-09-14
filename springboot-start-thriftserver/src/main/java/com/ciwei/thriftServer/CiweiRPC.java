package com.ciwei.thriftServer;

import com.ciwei.consul.ConsulDiscoveryService;
import com.ciwei.consul.RpcServiceUrl;
import org.apache.thrift.TServiceClient;
import org.apache.thrift.protocol.TCompactProtocol;
import org.apache.thrift.protocol.TMultiplexedProtocol;
import org.apache.thrift.protocol.TProtocol;
import org.apache.thrift.transport.TFramedTransport;
import org.apache.thrift.transport.TSocket;
import org.apache.thrift.transport.TTransport;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;

/**
 *  RPC客户端调用
 */
public class CiweiRPC {

    @Autowired
    ApplicationContext applicationContext;

    public <T extends TServiceClient> T createClient(Class<T> tz) {

        String microServiceName = tz.getName().split("\\.")[2];
        ConsulDiscoveryService consulDiscoveryService = applicationContext.getBean(ConsulDiscoveryService.class);
        RpcServiceUrl rpcServiceUrl = consulDiscoveryService.getRpcServiceUrl(microServiceName);

        TTransport transport = new TFramedTransport(new TSocket(rpcServiceUrl.getHost(), rpcServiceUrl.getPort()));
        TCompactProtocol protocol = new TCompactProtocol(transport);

        String className = tz.getName().substring(0, tz.getName().lastIndexOf("$Client"));
        TMultiplexedProtocol multiplexedProtocol = new TMultiplexedProtocol(protocol, className);
        try {
            transport.open();
            return tz.getDeclaredConstructor(TProtocol.class).newInstance(multiplexedProtocol);
        } catch (Exception var8) {
            throw new RuntimeException(var8);
        }
    }

    public <T extends TServiceClient> T createClient(String microServiceName, Class<T> tz) {

        ConsulDiscoveryService consulDiscoveryService = applicationContext.getBean(ConsulDiscoveryService.class);
        RpcServiceUrl rpcServiceUrl = consulDiscoveryService.getRpcServiceUrl(microServiceName);

        TTransport transport = new TFramedTransport(new TSocket(rpcServiceUrl.getHost(), rpcServiceUrl.getPort()));
        TCompactProtocol protocol = new TCompactProtocol(transport);

        String className = tz.getName().substring(0, tz.getName().lastIndexOf("$Client"));
        TMultiplexedProtocol multiplexedProtocol = new TMultiplexedProtocol(protocol, className);
        try {
            transport.open();
            return tz.getDeclaredConstructor(TProtocol.class).newInstance(multiplexedProtocol);
        } catch (Exception var8) {
            throw new RuntimeException(var8);
        }
    }

    public <T extends TServiceClient> T createClientPython(String microServiceName, Class<T> tz) {
        ConsulDiscoveryService consulDiscoveryService = applicationContext.getBean(ConsulDiscoveryService.class);
        RpcServiceUrl rpcServiceUrl = consulDiscoveryService.getRpcServiceUrl(microServiceName);
        TTransport transport =new TSocket(rpcServiceUrl.getHost(), rpcServiceUrl.getPort());
        TProtocol protocol = new TCompactProtocol(transport);
        try {
            transport.open();
            return tz.getDeclaredConstructor(TProtocol.class).newInstance(protocol);
        } catch (Exception var8) {
            throw new RuntimeException(var8);
        }
    }

}
