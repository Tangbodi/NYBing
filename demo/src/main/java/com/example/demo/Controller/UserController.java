package com.example.demo.Controller;

import com.example.demo.DAO.UserPasswordHistoryDAO;
import com.example.demo.DTO.LoginDTO;
import com.example.demo.DTO.UserDTO;
import com.example.demo.Entity.User;
import com.example.demo.Exception.UserNotFoundException;
import com.example.demo.Repository.UserPasswordHistoryRepository;
import com.example.demo.Repository.UserRepository;
import com.example.demo.Service.EmailValidatorService;
import com.example.demo.Service.UserPasswordHistoryService;
import com.example.demo.Service.UserService;
import com.example.demo.Util.SessionManagementUtil;
import com.example.demo.Validator.RegisterUserValidator;
import net.bytebuddy.utility.RandomString;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.mail.javamail.JavaMailSender;

import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.annotation.RequestBody;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.UnsupportedEncodingException;


@RestController
@CrossOrigin(origins = "http://192.168.1.23:3000/")
public class UserController {
    private static final Logger logger = LoggerFactory.getLogger(UserController.class);
//    public static final String url = "api";
    @Autowired
    RegisterUserValidator registerUserValidator;
    @Autowired
    private UserService userService;
    @Autowired
    private SessionManagementUtil sessionManagementUtil;

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private JavaMailSender mailSender;
    @Autowired
    private UserPasswordHistoryService userPasswordHistoryService;
    @Autowired
    private UserPasswordHistoryDAO userPasswordHistoryDAO;
    @Autowired
    private UserPasswordHistoryRepository userPasswordHistoryRepository;
    @Autowired
    private EmailValidatorService emailValidatorService;
    String Token;

    @RequestMapping("/sendEmail")
    public void sendEmail(String recipientEmail){
        Token = emailValidatorService.getRandomToken();
        emailValidatorService.sendSimpleMail();
    }
    @PostMapping("/user/register")
    public int register (@RequestBody User newUser, BindingResult bindingResult) throws IOException {
        registerUserValidator.validate(newUser, bindingResult);

        int res=0;
        if (bindingResult.hasErrors()) {
            res=1;
            logger.info("Password should be minimum eight characters, at least one uppercase letter, one lowercase letter, one number and one special character::"+newUser.getPassword());
        }
        else if (this.userService.checkIfUserRegistered(newUser)) {
            res=2;
            logger.info("Oops. An account with this username already exists::"+newUser.getUserName());
        }

        else {
            userService.registerUser(newUser);
//            userPasswordHistoryService.saveUserPasswordHistory(newUser);
            logger.info("User successfully registered::");
        }
        return res;
    }
    @RequestMapping("/verifyToken")
    public boolean verifyToken(String input){
        int res = input.compareTo(Token);
        return res==0 ? true : false;
    }
    @PostMapping("/user/login")
    public User UserLogin(@RequestBody LoginDTO login, HttpServletRequest request, HttpServletResponse response)throws IOException {

        logger.info( login.getUserName());
        logger.info(login.getPassword());

        if (this.userService.authenticate(login) == true) {
            logger.info("Successfully authenticated::");
            Long id = userService.getProfileByUserName(login.getUserName()).getId();
            this.sessionManagementUtil.createNewSessionForUser(request,id);
            User user = userRepository.findById(id).orElseThrow(()->new UserNotFoundException(id));

            return user;
        }
        else{
            logger.info("Username or Password is wrong::");
        }
        return null;
    }
    @PutMapping("/user/update/{userId}")
    public User updateUserById(HttpServletRequest request, @RequestBody UserDTO userDTO, @PathVariable Long userId){
        if (!this.sessionManagementUtil.doesSessionExist(request))
        {
            logger.info("Please login to access this page");
//            return null;
        }
        logger.info(userDTO.getFirstName());
        logger.info(userDTO.getLastName());
        logger.info(userDTO.getEmail());
        logger.info(userDTO.getPhone());
        return userService.updateUserById(userDTO,userId);
    }
    @GetMapping("/user/info/{userId}")
    public User getUserById(HttpServletRequest request, @PathVariable Long userId) {
        if (!this.sessionManagementUtil.doesSessionExist(request))
        {
            logger.info("Please login to access this page");
        }
        return userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException(userId));
    }

    @PostMapping("/user/forgot_password")
    public String processForgotPassword(HttpServletRequest request,@RequestBody User user){

        String email = user.getEmail();
        String token = RandomString.make(30);
        String siteURL = request.getRequestURL().toString();
        siteURL.replace(request.getServletPath(),"");

        try{
            userService.updateResetPasswordToken(token,email);
            String resetPasswordLink = siteURL + "/reset_password?token=" + token;
            sendEmail(email,resetPasswordLink);
            return "We have sent a reset password link to your email. Please check.";

        }catch (UserNotFoundException e){
           return e.getMessage();
        }catch (UnsupportedEncodingException | MessagingException ex){
            return "Error while sending email";
        }
    }
    public void sendEmail(String recipientEmail,String link) throws MessagingException,UnsupportedEncodingException{
        MimeMessage message = mailSender.createMimeMessage();
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
        mailSender.send(message);
    }
    @GetMapping("forgot_password/reset_password")
    public String showResetPasswordForm(@Param(value="token")String token, Model model){
        User user = userService.getByResetPasswordToken(token);
        if(user == null){
            return "Invalid Token";
        }
        return "successful";
    }
    @PostMapping("forgot_password/reset_password")
    public String enterPassword(@Param(value="token")String token,@RequestBody User user, HttpServletRequest request){
       user = userService.getByResetPasswordToken(token);
        if(user == null){
            return "Invalid Token";
        }
        token = request.getParameter("token");
        String password = request.getParameter("password");

        if(user == null){
            return "Invalid Token";
        }
        else{
            userService.updatePassword(user,password);
            return "You have successfully changed your password";
        }
    }

    @RequestMapping("/logout.html")
    public void logoutUser(HttpServletRequest request)
    {
        logger.info("Logging out::");
        request.getSession().invalidate();
    }
}
