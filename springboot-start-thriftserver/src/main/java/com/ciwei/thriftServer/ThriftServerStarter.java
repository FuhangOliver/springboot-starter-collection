package com.ciwei.thriftServer;

import org.apache.thrift.TMultiplexedProcessor;
import org.apache.thrift.TProcessor;
import org.apache.thrift.TProcessorFactory;
import org.apache.thrift.protocol.TCompactProtocol;
import org.apache.thrift.server.TServer;
import org.apache.thrift.server.TThreadedSelectorServer;
import org.apache.thrift.transport.TFramedTransport;
import org.apache.thrift.transport.TNonblockingServerSocket;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.util.ClassUtils;

import java.lang.reflect.Constructor;
import java.util.Map;
import java.util.Set;

public class ThriftServerStarter {
    private final Logger logger = LoggerFactory.getLogger(ThriftServerStarter.class);


    @Autowired
    ApplicationContext applicationContext;

    @Autowired
    private ThriftServerProperties thriftServerProperties;

    @Autowired
    private ThriftServerThreadPoolProperties thriftServerThreadPoolProperties;

    public boolean isServing() {
        if(tServer == null){
            return false;
        }else{
            return tServer.isServing();
        }
    }

    private TServer tServer;

    public void start() throws Exception {
        TMultiplexedProcessor processor = new TMultiplexedProcessor();
        registerThriftService(processor);
        TNonblockingServerSocket socket = new TNonblockingServerSocket(thriftServerProperties.getProvidePort());
        TThreadedSelectorServer.Args arg = new TThreadedSelectorServer.Args(socket).selectorThreads(thriftServerThreadPoolProperties.getMinWorkerThreads()).workerThreads(thriftServerThreadPoolProperties.getMaxWorkerThreads());
        logger.info("<=========={}{}",thriftServerThreadPoolProperties.toString(),"==========>");
        arg.protocolFactory(new TCompactProtocol.Factory());
        arg.transportFactory(new TFramedTransport.Factory());
        arg.processorFactory(new TProcessorFactory(processor));
        TThreadedSelectorServer server = new TThreadedSelectorServer(arg);
        this.tServer = server;
        server.serve();
    }

    private void registerThriftService(TMultiplexedProcessor processor){
        //通过@RpcService注解获取当前微服务所有的服务bean, 通过反射获取服务接口命名空间 和 实例化thrift生成的Java类里面的内部类Processor对象
        Map<String, Object> thriftSericeMap = applicationContext.getBeansWithAnnotation(RpcService.class);
        Set<Map.Entry<String, Object>> beanEntrySet = thriftSericeMap.entrySet();
        for (Map.Entry<String, Object> entry : beanEntrySet){
            Object bean = entry.getValue();
            Class<?> ifaceClass = getIfaceClass(bean);

            TProcessor processItem = getServiceProcessor(bean, ifaceClass);
            String serviceName = ifaceClass.getEnclosingClass().getName();
            processor.registerProcessor(serviceName, processItem);
        }
    }

    private static Class<?> getIfaceClass(Object bean){
        Class<?>[] allInterfaces = ClassUtils.getAllInterfaces(bean);
        for (Class<?> item : allInterfaces){
            if (!item.getSimpleName().equals("Iface")){
                continue;
            }
            return item;
        }
        return null;
    }

    //比如 processItem = new PayChannelService.Processor<>(payChannelServiceImpl);
    private static TProcessor getServiceProcessor(Object bean, Class<?> ifaceClazz){
        try{
            Class<TProcessor> processorClazz = null;
            Class<?> clazz = ifaceClazz.getDeclaringClass();
            Class<?>[] classes = clazz.getDeclaredClasses();
            for (Class<?> innerClazz : classes){
                if (!innerClazz.getName().endsWith("$Processor")){
                    continue;
                }
                if (!TProcessor.class.isAssignableFrom(innerClazz)){
                    continue;
                }
                processorClazz = (Class<TProcessor>) innerClazz;
                break;
            }
            if (processorClazz == null){
                throw new IllegalStateException("No TProcessor Found.");
            }
            Constructor<TProcessor> contructor = processorClazz.getConstructor(ifaceClazz);
            return BeanUtils.instantiateClass(contructor, bean);
        } catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }
}
