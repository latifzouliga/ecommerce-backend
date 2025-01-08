package com.latif.ecommercebackend.service.impl;

import com.latif.ecommercebackend.exceptions.EmailFailureException;
import com.latif.ecommercebackend.model.VerificationToken;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailServiceImpl {

    @Value("${email.from}")
    private String fromAddress;
    @Value("${app.frontend.url}")
    private String url;
    private final JavaMailSender javaMailSender;



    public EmailServiceImpl(JavaMailSender javaMailSender) {
        this.javaMailSender = javaMailSender;
    }

    private SimpleMailMessage makeMailMessage(){
        SimpleMailMessage simpleMailMessage = new SimpleMailMessage();
        simpleMailMessage.setFrom(fromAddress);
        return simpleMailMessage;
    }

    public void sendVerificationEmail(VerificationToken verificationToken) throws EmailFailureException {
        SimpleMailMessage message = makeMailMessage();
        message.setTo(verificationToken.getUser().getEmail());
        message.setSubject("Verify your email to activate your account");
        message.setText("Please follow the link below to verify your email to activate you account.\n" +
                url + "/auth/verify?token="+verificationToken.getToken());

        try {
            javaMailSender.send(message);
        }catch (MailException exception){
            throw new EmailFailureException("Email can not be sent");
        }
    }
}
