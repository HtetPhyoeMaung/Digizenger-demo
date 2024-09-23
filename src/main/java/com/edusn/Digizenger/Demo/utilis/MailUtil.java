package com.edusn.Digizenger.Demo.utilis;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;

@Component
public class MailUtil {
    @Autowired
    private  JavaMailSender javaMailSender;

    public  void sendOtpEmail(String email, String otp) throws MessagingException {

        MimeMessage mimeMessage = javaMailSender.createMimeMessage();

        MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage);
        mimeMessageHelper.setTo(email);
        mimeMessageHelper.setSubject("Verify OTP");
        mimeMessageHelper.setText("""
                <div>
                <span>We are from EDUSN ORG. Here is your otp. please use within 1 minute</span>
                <br>
                
                <span>%s</span>
                </div>
                """.formatted(otp),true
        );

        javaMailSender.send(mimeMessage);
    }
}
