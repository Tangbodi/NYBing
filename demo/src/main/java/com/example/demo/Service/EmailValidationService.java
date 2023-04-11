package com.example.demo.Service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.io.UnsupportedEncodingException;

@Service
public class EmailValidationService {

    private static final Logger logger = LoggerFactory.getLogger(EmailValidationService.class);

    @Autowired
    private JavaMailSender javaMailSender;

    public void sendEmailValidationLink(String recipientEmail,String link) throws MessagingException, UnsupportedEncodingException {
        MimeMessage message = javaMailSender.createMimeMessage();
        MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(message);
        mimeMessageHelper.setFrom("contact@dummynode.com","DummyNode Support");
        mimeMessageHelper.setTo(recipientEmail);
        String subject = " Verify your email address so we know it’s really you—and so we can send you important information about your DummyNode account.";
        String content = "<p>Hello,</p>"
                + "<p>We're happy you signed up for DummyNode.</p>"
                + "<p>To start exploring the DummyNode, please confirm your email address.</p>"
                + "<p><a href=\"" + link + "\">Verify email address</a></p>"
                + "<br>"
                + "<p>Welcome to DummyNode! "
                + "The DummyNode Team</p>";
        mimeMessageHelper.setSubject(subject);
        mimeMessageHelper.setText(content,true);
        javaMailSender.send(message);
    }
    public void sendForgotPasswordLink(String recipientEmail,String link) throws MessagingException,UnsupportedEncodingException{
        MimeMessage message = javaMailSender.createMimeMessage();
        MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(message);

        mimeMessageHelper.setFrom("contact@dummynode.com","DummyNode Support");
        mimeMessageHelper.setTo(recipientEmail);
        String subject = "Here's the link to reset your password";
        String content = "<p>Hello,</p>"
                + "<p>You have requested to reset your password.</p>"
                + "<p>Click the link below to change your password:</p>"
                + "<p><a href=\"" + link + "\">Change my password</a></p>"
                + "<br>"
                + "<p>Ignore this email if you do remember your password, "
                + "or you have not made the request.</p>";
        mimeMessageHelper.setSubject(subject);
        mimeMessageHelper.setText(content,true);
        javaMailSender.send(message);
    }
}
