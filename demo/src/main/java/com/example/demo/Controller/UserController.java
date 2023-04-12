package com.example.demo.Controller;

import com.example.demo.DAO.UserPasswordHistoryDAO;
import com.example.demo.DTO.LoginDTO;
import com.example.demo.DTO.UserDTO;
import com.example.demo.Entity.User;
import com.example.demo.Exception.*;
import com.example.demo.Repository.UserPasswordHistoryRepository;
import com.example.demo.Repository.UserRepository;
import com.example.demo.Service.EmailValidationService;
import com.example.demo.Service.UserPasswordHistoryService;
import com.example.demo.Service.UserService;
import com.example.demo.Util.SessionManagementUtil;
import com.example.demo.Validator.RegisterUserValidator;
import net.bytebuddy.utility.RandomString;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.javamail.JavaMailSender;

import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.annotation.RequestBody;

import javax.mail.MessagingException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.concurrent.Semaphore;


@RestController
@CrossOrigin(origins = "http://192.168.1.23:3000/")
public class UserController{
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
    private EmailValidationService emailValidationService;

    private final Semaphore permit = new Semaphore(10,true);


//    public UserController(String message) {
//        super(message);
//    }

    @PostMapping("/user/register")
    public ResponseEntity register (@RequestBody User newUser, BindingResult bindingResult, HttpServletRequest request) throws IOException, MessagingException {

        registerUserValidator.validate(newUser, bindingResult);
//        try{
//            permit.acquire();
//            logger.info("Dealing request=================>");
//            Thread.sleep(2000);
            if (bindingResult.hasErrors()) {
                logger.info("Password should be minimum eight characters, at least one uppercase letter, one lowercase letter, one number and one special character::"+newUser.getPassword());
                throw new PasswordException(newUser.getPassword());
            }
            else if (this.userService.checkIfUserRegistered(newUser)) {
                logger.info("Oops. An account with this username already exists::");
                throw new UserAlreadyExistsException();

            }
            else {
                userService.registerUser(newUser);
                logger.info("User successfully registered::");
            }

                processEmailValidation(request,newUser);

//        finally {
//            permit.release();
//        }
        return ResponseEntity.ok().build();
    }

    public String processEmailValidation(HttpServletRequest request,User user){
        String email = user.getEmail();
        String token = RandomString.make(9);
        String siteURL = request.getRequestURL().toString();
        siteURL.replace(request.getServletPath(),"");
        try{
            userService.updateToken(token,email);
            String emailValidationLink = siteURL + "/email_validation?token=" + token;
            emailValidationService.sendEmailValidationLink(email,emailValidationLink);
            return "We have sent an email validation link to your email. Please check.";

        }catch (NotFoundException e){
            return e.getMessage();
        }catch (UnsupportedEncodingException | MessagingException ex){
            return "Error while sending email";
        }
    }

    @GetMapping("/user/register/email_validation")
    public ResponseEntity showEmailValidationPage(@Param(value="token")String token){
        User user = userService.getByToken(token);
        if(user==null){
            throw new NotFoundException(user.getEmail());
        }
        userService.verifyUser(user);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/user/login")
    public User UserLogin(@RequestBody LoginDTO login, HttpServletRequest request, HttpServletResponse response)throws IOException {

        if (this.userService.authenticate(login)) {
            logger.info("Successfully authenticated::");
            String userName = userService.getProfileByUserName(login.getUserName()).getUserName();
            this.sessionManagementUtil.createNewSessionForUser(request,userName);
            User user = userRepository.findByUserName(userName).orElseThrow(()->new NotFoundException(userName));
            return user;
        }
        else{
            logger.info("Username or Password is wrong::");
            throw new AuthException(login.getUserName(),login.getPassword());
        }
    }
    @PutMapping("/user/update/{userName}")
    public User updateUserByUserName(HttpServletRequest request, @RequestBody UserDTO userDTO, @PathVariable String userName){
        if (!this.sessionManagementUtil.doesSessionExist(request))
        {
            logger.info("Please login to access this page::");
            throw new AuthException();
        }
        logger.info(userDTO.getFirstName());
        logger.info(userDTO.getLastName());
        return userService.updateUserByUserName(userDTO,userName);
    }
    @PutMapping("/user/password/update/{userName}")
    public Boolean updatePasswordByUserName(HttpServletRequest request, @RequestBody UserDTO userDTO, @PathVariable String userName){
        if (!this.sessionManagementUtil.doesSessionExist(request))
        {
            logger.info("Please login to access this page::");
            throw new AuthException();
        }
        logger.info(userDTO.getInputPassword());
        logger.info(userDTO.getNewPassword());
        return userService.updatePasswordByUserName(userDTO,userName);
    }
    @GetMapping("/user/info/{userName}")
    public User getUserByUserName(HttpServletRequest request, @PathVariable String userName) {
        if (!this.sessionManagementUtil.doesSessionExist(request))
        {
            logger.info("Please login to access this page::");
            throw new AuthException();
        }
        return userRepository.findByUserName(userName)
                .orElseThrow(() -> new NotFoundException(userName));
    }

    @PostMapping("/user/forgot_password")
    public String processForgotPassword(HttpServletRequest request,@RequestBody User user){

        String email = user.getEmail();
        String token = RandomString.make(9);
        String siteURL = request.getRequestURL().toString();
        siteURL.replace(request.getServletPath(),"");

        try{
            userService.updateToken(token,email);
            String resetPasswordLink = siteURL + "/reset_password?token=" + token;
            emailValidationService.sendForgotPasswordLink(email,resetPasswordLink);
            return "We have sent a reset password link to your email. Please check.";

        }catch (NotFoundException e){
            return e.getMessage();
        }catch (UnsupportedEncodingException | MessagingException ex){
            return "Error while sending email";
        }
    }
    @GetMapping("forgot_password/reset_password")
    public String showResetPasswordForm(@Param(value="token")String token, Model model){
        User user = userService.getByToken(token);
        if(user == null){
            return "Invalid Token";
        }
        return "successful";
    }
    @PostMapping("forgot_password/reset_password")
    public String enterPassword(@Param(value="token")String token,@RequestBody User user, HttpServletRequest request){
       user = userService.getByToken(token);
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
    @PostMapping("/user/delete/{userName}")
    public void deleteUser(@PathVariable String userName){
        userService.deleteUser(userName);
    }

}
