package com.edusn.Digizenger.Demo.config;


import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;

import java.util.Properties;

@Configuration
public class MailConfig {
    @Value("${spring.mail.host}")
    private String mailHost;
    @Value("${spring.mail.port}")
    private int mailPort;
    @Value("${spring.mail.username}")
    private String mailUsername;
    @Value("${spring.mail.password}")
    private String mailPassword;

    @Bean
    public JavaMailSender getJavaMailSender() {
        JavaMailSenderImpl mailSender = new JavaMailSenderImpl();

        // Set the mail server host, port, username, and password
        mailSender.setHost(mailHost);
        mailSender.setPort(mailPort);  // Use 465 for SSL or 587 for TLS

        // Set the email credentials (use app password for Gmail)
        mailSender.setUsername(mailUsername);
        mailSender.setPassword(mailPassword);  // Replace with your app password

        // Set additional mail properties
        Properties props = mailSender.getJavaMailProperties();
        props.put("mail.transport.protocol", "smtp");
        props.put("mail.smtp.starttls.required", "true");  // Optional but recommended for Gmail
        props.put("mail.debug", "true");  // Enables debug mode for emails, useful for troubleshooting

        return mailSender;
    }

}
