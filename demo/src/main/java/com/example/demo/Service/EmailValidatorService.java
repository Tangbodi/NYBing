package com.example.demo.Service;

import net.bytebuddy.asm.MemberSubstitution;
import net.bytebuddy.utility.RandomString;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.io.UnsupportedEncodingException;
import java.util.Random;

@Service
public class EmailValidatorService {

    private static final Logger logger = LoggerFactory.getLogger(EmailValidatorService.class);

    @Autowired
    private JavaMailSender javaMailSender;

    public void sendSimpleMail(String recipientEmail,String link) throws MessagingException, UnsupportedEncodingException {
        MimeMessage message = javaMailSender.createMimeMessage();
        MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(message);

        try{
            mimeMessageHelper.setFrom("contact@dummynode.com","DummyNode Support");
            mimeMessageHelper.setTo(recipientEmail);
            String subject = "Here's the link to verify your email";
            String content = "<p>Hello,</p>"
                    + "<p>We're happy you signed up for DummyNode.</p>"
                    + "<p>To start exploring the DummyNode, please confirm your email address.</p>"
                    + "<p><a href=\"" + link + "\">Verify Now</a></p>"
                    + "<br>"
                    + "<p>Welcome to DummyNode! "
                    + "The DummyNode Team</p>";
            mimeMessageHelper.setSubject(subject);
            mimeMessageHelper.setText(content,true);
            javaMailSender.send(message);
        }catch (MessagingException e){
            logger.error("The exception happens during sending email!",e);
        }
    }
    public String getRandomToken(){
        String token = RandomString.make(6);
        return token;
    }
}
