# 开发一个spring-boot-starter的步骤
1. 新建一个maven项目
2. 需要一个配置类RedisAutoConfiguration，在配置类里面装配好需要提供出去的类
3. 使用@Enable配合@Import导入需要装配的类  或者  META-INF/spring.factories中配置
org.springframework.boot.autoconfigure.EnableAutoConfiguration=com.FuHang.demo.redis.RedisAutoConfiguration

## redis分布式锁starter--基于Redisson
### 配置文件
        redisson.lock.server.address=127.0.0.1:6379
        redisson.lock.server.type=standalone
### 更新记录
1. 改变配置方式，增加对不同Redis连接方式的支持
<br/>去除以下方法RedissonManager(String redisIp, String redisPort)

            public RedissonManager (String redisIp, String redisPort) {
                try {
                    String redisAddr = new StringBuilder("redis://")
                            .append(redisIp).append(":").append(redisPort)
                            .toString();
                    config.useSingleServer().setAddress(redisAddr);
                    redisson = (Redisson) Redisson.create(config);
                    LOGGER.info("初始化Redisson结束,redisAddress:" + redisAddr);
                } catch (Exception e) {
                    LOGGER.error("Redisson init error", e);
                    e.printStackTrace();
                }
            }
## 怎么集成到项目中
### 第一步，pom.xml maven导包
```
<!--分布式锁redisson版本-->
<dependency>
    <groupId>com.ciwei</groupId>
    <artifactId>redis-distributed-lock-starter</artifactId>
    <version>1.0-SNAPSHOT</version>
</dependency>
```
### 第二步，启动类上面添加开启注解
```java
@EnableRedissonLock
@SpringBootApplication
public class App
{
    public static void main( String[] args )  {
        CiweiServiceStarter.run(SpringApplication.run(App.class, args), "sam");
    }
}
```
### 第三步，application.properties添加配置文件
```
#如果是redis单节点
redisson.lock.server.address=172.16.2.2:6379
redisson.lock.server.password=mypassword
redisson.lock.server.database=1
redisson.lock.server.type=standalone

#如果是redis集群，则采用这种配置方式
#redisson.lock.server.address=172.16.2.2:8001,172.16.2.2:8002,172.16.2.2:8003
#redisson.lock.server.password=
#redisson.lock.server.database=1
#redisson.lock.server.type=cluster
```
### 第四步，在代码中使用，@DistributedLock可配置释放锁的时间和等待锁的时间
```java
package com.ciwei.sam.timeTask;

import com.ciwei.lock.redisson.annotation.DistributedLock;
import com.ciwei.sam.dao.SpecialActivityMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

/**
 * @author fuhang
 * @description: 定时更新专场的状态
 * @date 2020/9/9 15:02
 */
@Component
public class UpdateSpecialActStatusTask {

    private static final Logger logger = LoggerFactory.getLogger(UpdateSpecialActStatusTask.class);

    @Autowired
    private SpecialActivityMapper specialActivityMapper;

    private static final String SAM_UPDATE_STATUS_REDIS_LOCK = "SAM_UPDATE_STATUS_REDIS_LOCK";

    /**
     * 定时更新专场的状态，每一分钟执行一次
     *
     * @return
     */
    @Scheduled(cron="0 */1 * * * ?")
    @DistributedLock(value = SAM_UPDATE_STATUS_REDIS_LOCK)
    public void scheduledUpdateStatus(){
        logger.info("scheduled update special activity status ---> start");
        this.specialActivityMapper.scheduledUpdateSpecialActStatus();
        logger.info("scheduled update special activity status ---> end");
    }
}
```
