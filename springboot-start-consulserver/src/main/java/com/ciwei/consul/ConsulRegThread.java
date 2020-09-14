package com.ciwei.consul;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ConsulRegThread extends Thread {
    private final static Logger logger = LoggerFactory.getLogger(ConsulRegisterService.class);
    private ConsulRegisterService registerService;
    private String registerName;
    private String hostIP;
    private int hostPort;

    private int MIN_TIMEOUT = 10*1000;//初始10秒
    private int MAX_TIMEOUT = 30*60*1000; //最大30分钟

    public ConsulRegThread(ConsulRegisterService registerService, String registerName, String hostIP, int hostPort){
        this.registerService = registerService;
        this.registerName = registerName;
        this.hostIP = hostIP;
        this.hostPort = hostPort;
    }

    @Override
    public void run() {
        int sleepTimeout = MIN_TIMEOUT;
        while(false == registerService.register(registerName,hostIP,hostPort))
        {
            try {
                //再注册 直到注册成功
                Thread.sleep(sleepTimeout);
                if(sleepTimeout<MAX_TIMEOUT){
                    sleepTimeout = sleepTimeout * 3;
                }
            } catch (InterruptedException e) {
            }
        }


    }
}
