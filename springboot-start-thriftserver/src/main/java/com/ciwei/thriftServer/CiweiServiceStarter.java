package com.ciwei.thriftServer;

import com.ciwei.consul.ConsulRegisterService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;

import java.io.InputStream;
import java.util.Properties;

public class CiweiServiceStarter {

    private final static Logger logger = LoggerFactory.getLogger(CiweiServiceStarter.class);

    private static Boolean thriftServerFail = Boolean.FALSE;

    public static void run(ApplicationContext context, String registerName){

        String pomUrl = new StringBuilder("/META-INF/maven/com.ciwei/").append(registerName.toLowerCase()).append("/pom.properties").toString();
        InputStream is = CiweiServiceStarter.class.getResourceAsStream(pomUrl);
        try {
            Properties props = new Properties();
            props.load(is);
            is.close();
            logger.info("........{}版本号{}发布准备中........", registerName, props.getProperty("version"));
        } catch (Exception e) {
            logger.warn("........读取不到了pom.properties文件获取不到版本号........");
        }

        ThriftServerStarter thriftServerStarter = context.getBean(ThriftServerStarter.class);

        Thread t = new Thread(() -> {
            try {
                thriftServerStarter.start();
            } catch (Exception e) {
                thriftServerFail = Boolean.TRUE;
                logger.error("......✖✖✖✖✖......{}服务开启失败......✖✖✖✖✖......异常:{}", registerName, e.getMessage());
            }
        });
        t.start();

        while (true){
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            if(thriftServerStarter.isServing()){
                ConsulRegisterService consulRegister = context.getBean(ConsulRegisterService.class);
                consulRegister.register(registerName);
                logger.info("......★★★★......{}服务发布成功......★★★★......", registerName);
                break;
            }
            if(thriftServerFail){
                break;
            }
        }
    }
}
