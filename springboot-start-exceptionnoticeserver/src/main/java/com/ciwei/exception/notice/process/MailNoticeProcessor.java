package com.ciwei.exception.notice.process;

import com.ciwei.exception.notice.content.ExceptionInfo;
import com.ciwei.exception.notice.properties.MailProperties;
import org.springframework.mail.MailSender;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;

/**
 * 邮箱异常信息通知具体实现
 *
 * @author FuHang
 */
public class MailNoticeProcessor implements INoticeProcessor {

    private final MailProperties mailProperties;

    private final MailSender mailSender;

    public MailNoticeProcessor(MailSender mailSender, MailProperties emailProperties) {
        Assert.noNullElements(emailProperties.getTo(), "email 'from' property must not be null");
        Assert.noNullElements(emailProperties.getTo(), "email 'to' property must not be null");
        this.mailSender = mailSender;
        this.mailProperties = emailProperties;
    }

    @Override
    public void sendNotice(ExceptionInfo exceptionInfo) {
        SimpleMailMessage mailMessage = new SimpleMailMessage();
        mailMessage.setFrom(mailProperties.getFrom());
        mailMessage.setTo(mailProperties.getTo());
        if (!StringUtils.isEmpty(mailMessage.getCc())){
            mailMessage.setCc(mailMessage.getCc());
        }
        mailMessage.setText(exceptionInfo.createText());
        mailMessage.setSubject(String.format("来自%s项目的异常通知", exceptionInfo.getProject()));
        mailSender.send(mailMessage);
    }

}
