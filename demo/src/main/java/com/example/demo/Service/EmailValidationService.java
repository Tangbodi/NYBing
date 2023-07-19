package com.example.demo.Service;

import com.example.demo.DTO.UserDTO;
import com.example.demo.Exception.UserNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import javax.servlet.http.HttpServletRequest;
import java.io.UnsupportedEncodingException;
import java.util.UUID;

@Service
public class EmailValidationService {

    private static final Logger logger = LoggerFactory.getLogger(EmailValidationService.class);

    @Autowired
    private JavaMailSender javaMailSender;

    @Lazy
    @Autowired
    private UserService userService;

    //------------------------------------------------------------------------------------------
    //generate a random token then send validation email via 192.168.1.10:8080/api/user/register/email_validation?token=
    public boolean processEmailValidation(HttpServletRequest request, UserDTO userDTO){
        try{
            logger.info("Generating email validation link and token:::");
            String email = userDTO.getEmail();
            UUID uuid = UUID.randomUUID();
            String token = uuid.toString();
            String siteURL = request.getRequestURL().toString();
            siteURL.replace(request.getServletPath(),"");
            siteURL = siteURL.replace("http://", "https://");

            userService.updateToken(token,email);
            String emailValidationLink = siteURL + "/email_validation?token=" + token;
            logger.info("emailValidationLink:::"+emailValidationLink);
            return (sendEmailValidationLink(email,emailValidationLink));

        }catch (UserNotFoundException e){
            logger.error("Failed to generate email validation link and token",e);
            throw new RuntimeException("Failed to generate email validation link and token",e);
        }catch (UnsupportedEncodingException | MessagingException e){
            logger.error("Failed to send email validation link",e);
            throw new RuntimeException("Failed to send email validation link",e);
        }
    }
    public boolean sendEmailValidationLink(String recipientEmail,String emailValidationLink) throws MessagingException, UnsupportedEncodingException {
        try{
            logger.info("Editing email sender, receiver, subject, and content:::");
            MimeMessage message = javaMailSender.createMimeMessage();
            MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(message);
            mimeMessageHelper.setFrom("contactus@nybing.com","NYBing");
            mimeMessageHelper.setTo(recipientEmail);
            String subject = " Verify your email to start using NYBing";
            String content = "<p>Hello,</p>"
                    + "<p>Verify your email address so we know it’s really you—and so we can send you important information about your NYBing account.</p>"
                    + "<br>"
                    + "<p >"
                    + "<a href=\"" + emailValidationLink + "\" style=\"background-color: #c67c4b;padding: 10px 15px;color: white; border-radius: 0.8rem; display: inline-block;text-decoration: none;\">Verify email address</a></p>"
                    + "<br>"
                    + "<p>NYBing</p>";
            mimeMessageHelper.setSubject(subject);
            mimeMessageHelper.setText(content,true);
            javaMailSender.send(message);
            logger.info("sent emailValidationLink:::"+emailValidationLink);
            return true;
        }catch (MessagingException e) {
            logger.error("Failed to send email validation link",e);
            throw new RuntimeException("Failed to send email validation link",e);
        }
    }
    public boolean sendForgotPasswordLink(String recipientEmail,String link) throws MessagingException,UnsupportedEncodingException{
        logger.info("Editing email sender, receiver, subject, and content:::");
        try{
            link = link.replace("http://", "https://");
            MimeMessage message = javaMailSender.createMimeMessage();
            MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(message);
            mimeMessageHelper.setFrom("contactus@nybing.com","NYBing");
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
            logger.info("sent link:::" + link);
            return true;
        }catch (MessagingException e) {
            logger.error("Failed to send forgot password link",e);
            throw new RuntimeException("Failed to send forgot password link",e);
        }
    }
}
