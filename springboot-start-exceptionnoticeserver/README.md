--- 
### 一、怎么引入异常企业微信发送提示
*** 
#### 第一步，pom.xml maven导包，这里的用意是为了兼容 web mvc 程序和普通的微服务都可以使用
```
<!--消息通知-->
<dependency>
    <groupId>com.ciwei</groupId>
    <artifactId>springboot-start-exceptionNoticeServer</artifactId>
    <version>1.0-SNAPSHOT</version>
</dependency>
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-web</artifactId>
    <exclusions>
        <exclusion>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-tomcat</artifactId>
        </exclusion>
    </exclusions>
</dependency>
```
#### 第二步，application.properties 加入通知相关的配置
```
#启用开关 false或不配置的话本项目不会生效
exception.notice.enable = true
#指定异常信息中的项目名，不填的话默认取 spring.application.name的值
exception.notice.projectName = sam
#追踪信息的包含的包名，配置之后只通知此包下的异常信息
exception.notice.included-trace-package = com.ciwei
#异常信息发送的时间周期 以秒为单位 默认值5，异常信息通知并不是立即发送的，默认设置了5s的周期，主要为了防止异常过多通知刷屏，同时钉钉针对异常通知刷屏的情况也增加了限流措施，建议不要修改
exception.notice.period = 5
#需要排除的异常通知，注意 这里是异常类的全路径，可多选
#exception.notice.exclude-exceptions =
#  - com.ciwei.recruit.common.exception.BusinessException
#企业微信配置
#企业微信webhook地址
exception.notice.we-chat.web-hook = https://qyapi.weixin.qq.com/cgi-bin/webhook/send?key=41de4207-a0c8-4790-97b1-05bf9b374fae
#手机号列表，提醒手机号对应的群成员(@某个成员)，@all表示提醒所有人 当msg-type=text时才会生效
exception.notice.we-chat.at-phones = 18163524227
#userid的列表，提醒群中的指定成员(@某个成员)，@all表示提醒所有人 当msg-type=text时才会生效
exception.notice.we-chat.at-user-ids = @all
#消息格式 企业微信支持 （text）、markdown（markdown）、图片（image）、图文（news）四种消息类型 本项目中有 text和markdown两种可选
exception.notice.we-chat.msg-type = markdown
```
#### 第三步，在需要异常通知的类或者方法上面加入 @ExceptionListener 注解