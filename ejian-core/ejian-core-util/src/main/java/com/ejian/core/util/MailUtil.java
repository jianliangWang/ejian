package com.ejian.core.util;

import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;

/**
 * 邮件工具类
 */
public class MailUtil {

    private static JavaMailSender javaMailSender;

    public static void setJavamailSender(JavaMailSender jsInstance){
        javaMailSender = jsInstance;
    }

    public static void sendEmail(String subject, String from, String to, String text) {

        SimpleMailMessage message = new SimpleMailMessage();
        message.setSubject(subject);
        message.setFrom(from);
        message.setTo(to);
        message.setText(text);
        javaMailSender.send(message);
    }
}