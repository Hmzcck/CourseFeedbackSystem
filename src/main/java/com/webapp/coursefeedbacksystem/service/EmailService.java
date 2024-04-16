package com.webapp.coursefeedbacksystem.service;

import com.webapp.coursefeedbacksystem.dto.Email;
import com.webapp.coursefeedbacksystem.dto.EmailServerConfig;

import java.util.Properties;

import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.stereotype.Service;

@Service
public class EmailService {

    private JavaMailSenderImpl emailSender;

    public EmailService(JavaMailSender emailSender) {
        this.emailSender = (JavaMailSenderImpl) emailSender;
    }

    public boolean sendEmail(Email request) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom("coursefeedbacksystem@gmail.com");
        message.setTo(request.toEmail);
        message.setSubject(request.subject);
        message.setText(request.body);
        emailSender.send(message);
        return true;
    }

    public void updateEmailServerConfig(EmailServerConfig config) {
        emailSender.setHost(config.getHost());
        emailSender.setPort(config.getPort());
        emailSender.setUsername(config.getUsername());
        emailSender.setPassword(config.getPassword());

        Properties props = emailSender.getJavaMailProperties();
        props.put("mail.transport.protocol", "smtp");
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.debug", "true"); 

        emailSender.setJavaMailProperties(props);
    }
}
