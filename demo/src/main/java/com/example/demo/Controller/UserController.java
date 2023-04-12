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
    public ResponseEntity register (@RequestBody User newUser, BindingResult bindingResult, HttpServletRequest request) throws IOException {

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
                emailValidationService.processEmailValidation(request,newUser);
//        finally {
//            permit.release();
//        }
        return ResponseEntity.ok().build();
    }

    //------------------------------------------------------------------------------------------
    //user verify his email
    @GetMapping("/user/register/email_validation")
    public ResponseEntity showEmailValidationPageViaRegisterLink(@Param(value="token")String token){
        try{
            User user = userService.getByToken(token);
            userService.verifyUser(user);
        }catch (NotFoundException e){
            e.getMessage();
        }
        return ResponseEntity.ok().build();
    }
    //------------------------------------------------------------------------------------------
    @PostMapping("/user/login")
    public User UserLogin(@RequestBody LoginDTO login, HttpServletRequest request)throws IOException {
        User user = this.userService.authenticate(login,request);
        if (user!=null) {
            logger.info("Successfully authenticated::");
            String userName = user.getUserName();
            this.sessionManagementUtil.createNewSessionForUser(request,userName);
            return user;
        }
        else{
            return null;
        }
    }
    //------------------------------------------------------------------------------------------
    @GetMapping("/user/login/email_validation")
    public ResponseEntity showEmailValidationViaLoginLink(@Param(value="token")String token){
        try{
            User user = userService.getByToken(token);
            userService.verifyUser(user);
        }catch (NotFoundException e){
            e.getMessage();
        }
        return ResponseEntity.ok().build();
    }
    //------------------------------------------------------------------------------------------
    @PutMapping("/user/update/{userName}")
    public User updateUserByUserName(HttpServletRequest request, @RequestBody UserDTO userDTO, @PathVariable String userName){
        if (!this.sessionManagementUtil.doesSessionExist(request))
        {
            logger.info("Please login to access this page::");
            throw new AuthException();
        }
        if(userDTO.getNewPassword()==null){
            return userService.updateUserInfoByUserName(userDTO,userName);
        }
        else{
            return userService.updatePasswordByUserName(userDTO,userName);
        }
    }
    //------------------------------------------------------------------------------------------
//    @PutMapping("/user/password/update/{userName}")
//    public Boolean updatePasswordByUserName(HttpServletRequest request, @RequestBody UserDTO userDTO, @PathVariable String userName){
//        if (!this.sessionManagementUtil.doesSessionExist(request))
//        {
//            logger.info("Please login to access this page::");
//            throw new AuthException();
//        }
//
//        return userService.updatePasswordByUserName(userDTO,userName);
//    }
    //------------------------------------------------------------------------------------------
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
    //------------------------------------------------------------------------------------------
    @PostMapping("/user/forgot_password")
    public ResponseEntity processForgotPassword(HttpServletRequest request,@RequestBody User user){

        String email = user.getEmail();
        String token = RandomString.make(9);
        String siteURL = request.getRequestURL().toString();
        siteURL.replace(request.getServletPath(),"");

        try{
            userService.updateToken(token,email);
            String resetPasswordLink = siteURL + "/reset_password?token=" + token;
            emailValidationService.sendForgotPasswordLink(email,resetPasswordLink);

        }catch (NotFoundException e){
            e.getMessage();
        }catch (UnsupportedEncodingException | MessagingException ex){
            ex.getMessage();
        }
        return ResponseEntity.ok().build();
    }
    //------------------------------------------------------------------------------------------
    @GetMapping("forgot_password/reset_password")
    public ResponseEntity showResetPasswordForm(@Param(value="token")String token){
        User user = userService.getByToken(token);
        if(user == null){
            throw new NotFoundException();
        }
        return ResponseEntity.ok().build();
    }
    //------------------------------------------------------------------------------------------
    @PostMapping("forgot_password/reset_password")
    public ResponseEntity enterPassword(@Param(value="token")String token,HttpServletRequest request){

        User user = userService.getByToken(token);
        String password = request.getParameter("password");
        userService.ResetPassword(user,password);
        return ResponseEntity.ok().build();
    }
    //------------------------------------------------------------------------------------------
    @RequestMapping("/logout.html")
    public void userLogout(HttpServletRequest request)
    {
        request.getSession().invalidate();
        logger.info("Logged out::");
    }
    //------------------------------------------------------------------------------------------
    @PostMapping("/user/delete/{userName}")
    public void deleteUser(@PathVariable String userName){
        userService.deleteUser(userName);
    }

}
