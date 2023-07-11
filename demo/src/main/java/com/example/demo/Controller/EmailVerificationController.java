package com.example.demo.Controller;

import com.example.demo.DTO.ResetPasswordDTO;
import com.example.demo.Entity.User;
import com.example.demo.Repository.UserRepository;
import com.example.demo.Service.EmailValidationService;
import com.example.demo.Service.UserService;
import com.example.demo.Util.ApiResponse;
import com.example.demo.Validator.ValidString;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

@Controller
public class EmailVerificationController {
    private static final Logger logger = LoggerFactory.getLogger(UserController.class);
    @Autowired
    private UserService userService;


    //------------------------------------------------------------------------------------------
    @GetMapping("/user/register/email_validation")
    public void showEmailValidationPageViaRegisterLink(HttpServletRequest request,@RequestParam(value="token")String token,HttpServletResponse response) throws IOException {
        boolean isRedirected = true;
        User user = userService.getByToken(token);
        HttpSession session = request.getSession();
        session.setAttribute("isRedirected",isRedirected);
        String redirectURL;
        if(user!=null){
            userService.verifyUser(user);
            redirectURL = "https://www.nybing.com/email-verified";
        }else{
            redirectURL = "https://www.nybing.com/link-expired";
        }
        isRedirected = (boolean) session.getAttribute("isRedirected");//true
        if(isRedirected){
            isRedirected = false;
            logger.info("isRedirected:::"+isRedirected);
            session.setAttribute("isRedirected",isRedirected);//false
            response.sendRedirect(redirectURL);
        }
    }
    //------------------------------------------------------------------------------------------
    @GetMapping("/user/login/email_validation")
    public void showEmailValidationViaLoginLink(HttpServletRequest request,@RequestParam(value="token")String token,HttpServletResponse response) throws IOException {

        boolean isRedirected = true;
        User user = userService.getByToken(token);
        HttpSession session = request.getSession();
        session.setAttribute("isRedirected",isRedirected);
        String redirectURL;
        if(user!=null){
            userService.verifyUser(user);
            redirectURL = "https://www.nybing.com/email-verified";

        }else{
             redirectURL = "https://www.nybing.com/link-expired";

        }
        isRedirected = (boolean) session.getAttribute("isRedirected");//true
        if(isRedirected){
            isRedirected = false;
            logger.info("isRedirected:::"+isRedirected);
            session.setAttribute("isRedirected",isRedirected);//false
            response.sendRedirect(redirectURL);
        }

    }
    @GetMapping("/user/forgot_password/reset_password")
    public void showResetPasswordForm(HttpServletRequest request,@RequestParam(value="token")String token,HttpServletResponse response) throws IOException {
        boolean isRedirected = true;
        HttpSession session = request.getSession();
        session.setAttribute("isRedirected",isRedirected);//true
        logger.info("isRedirected:::"+isRedirected);
        User user = userService.getByToken(token);
        String redirectURL;
        if(user != null) {
            redirectURL = "https://www.nybing.com/reset-password?token=" + token;
        }else{
            redirectURL = "https://www.nybing.com/link-expired";
        }
        isRedirected = (boolean) session.getAttribute("isRedirected");//true
        if(isRedirected){
            isRedirected = false;
            logger.info("isRedirected:::"+isRedirected);
            session.setAttribute("isRedirected",isRedirected);//false
            response.sendRedirect(redirectURL);
        }
    }
    //------------------------------------------------------------------------------------------
}
