package com.fibo.ddp.common.service.common.message.send;

public interface EmailService {
    /**
     * 发送邮件方法
     * @param to    收件人邮件地址
     * @param subject   主题
     * @param content   邮件内容
     */
    public void sendHtmlMail(String to, String subject, String content);

    /**
     * 发送模板邮件方法
     * @param to    收件人邮件地址
     * @param subject   主题
     * @param templateName  模板名称
     * @param context   追加参数集合
     */
//    public void sendTemplateMail(String to, String subject, String templateName, Context context);

}
