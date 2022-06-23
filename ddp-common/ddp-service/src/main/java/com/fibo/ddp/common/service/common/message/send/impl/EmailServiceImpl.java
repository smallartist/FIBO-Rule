package com.fibo.ddp.common.service.common.message.send.impl;

import com.fibo.ddp.common.service.common.message.send.EmailService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.mail.internet.MimeMessage;

@Service("emailService")
@Slf4j
public class EmailServiceImpl implements EmailService {

    @Resource
    private JavaMailSender mailSender;

    @Value("${spring.mail.username}")
    private String fromAddr;

    @Override
    public void sendHtmlMail(String to, String subject, String content) {
        log.info("开始发送邮件，to:{}, subject:{}, content:{}", to, subject, content);
        MimeMessage message = mailSender.createMimeMessage();
        try {
            //true表示需要创建一个multipart message
            MimeMessageHelper helper = new MimeMessageHelper(message,true, "utf-8");
            helper.setFrom(fromAddr);
            String[] split = to.split(",");
            helper.setTo(split);
            helper.setSubject(subject);
            helper.setText(content,true);
            mailSender.send(message);
            log.info("邮件发送成功");
        }catch (Exception e){
            log.error("邮件发送失败",e);
        }
    }

    public String getFromAddr() {
        return fromAddr;
    }

    public void setFromAddr(String fromAddr) {
        this.fromAddr = fromAddr;
    }
}