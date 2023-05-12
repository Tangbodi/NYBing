package com.example.demo.Controller;

import com.example.demo.DTO.LoginDTO;
import com.example.demo.DTO.UserDTO;
import com.example.demo.Entity.User;
import com.example.demo.Exception.*;
import com.example.demo.Repository.UserRepository;
import com.example.demo.Service.AsyncService;
import com.example.demo.Service.EmailValidationService;
import com.example.demo.Service.UserService;
import com.example.demo.Util.SessionManagementUtil;
import com.example.demo.Validator.RegisterUserValidator;
import lombok.Value;
import net.bytebuddy.utility.RandomString;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.javamail.JavaMailSender;

import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.annotation.RequestBody;

import javax.mail.MessagingException;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.Optional;
import java.util.concurrent.Semaphore;


@RestController
@CrossOrigin(origins ="{ORIGINS}")
public class UserController{
    private static final Logger logger = LoggerFactory.getLogger(UserController.class);
    @Autowired
    RegisterUserValidator registerUserValidator;
    @Autowired
    private UserService userService;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private JavaMailSender mailSender;
    @Autowired
    private EmailValidationService emailValidationService;
    @Autowired
    private AsyncService asyncService;


    @GetMapping("/getData")
    public String getData(){
        return "got data from backend";
    }

    @GetMapping("/getuser/{id}")
    public Optional<User> getUserProfileById(@PathVariable String id){
        return userService.getUserProfileById(id);
    }
    @PostMapping("/user/register")
    public ResponseEntity register (@RequestBody User newUser, BindingResult bindingResult, HttpServletRequest request) throws IOException, InterruptedException {

        registerUserValidator.validate(newUser, bindingResult);

            if (this.userService.checkIfUserRegistered(newUser)) {
                throw new UserAlreadyExistsException();
            }
            else {
                asyncService.MultiExecutor("Registering user::"+newUser.getUserName()+"; "+newUser.getEmail());
                userService.registerUser(newUser);
                logger.info("User successfully registered::");
                emailValidationService.processEmailValidation(request,newUser);
            }
        return ResponseEntity.ok().build();
    }

    //------------------------------------------------------------------------------------------
    @PostMapping("/user/login")
    public User UserLogin(@RequestBody LoginDTO login, HttpServletRequest request)throws IOException {

        if (this.userService.authenticate(login)!=null) {
            logger.info("Successfully authenticated::");
            User user = userService.getProfileByUserName(login.getUserName());
            return user;
        }
        else{
            throw new UnauthorizedException();
        }
    }
    //------------------------------------------------------------------------------------------
    @GetMapping("/user/register/email_validation")
    public ResponseEntity showEmailValidationPageViaRegisterLink(@Param(value="token")String token){
         userService.getByToken(token);
         return ResponseEntity.ok().build();
    }
    //------------------------------------------------------------------------------------------
    @GetMapping("/user/login/email_validation")
    public User showEmailValidationViaLoginLink(@Param(value="token")String token){
        return userService.getByToken(token);
    }
    //------------------------------------------------------------------------------------------
    @PutMapping("/user/update/{userName}")
    public User updateUserByUserName(HttpServletRequest request, @RequestBody UserDTO userDTO, @PathVariable String userName) throws InterruptedException {

        if(userDTO.getNewPassword()==null){
            asyncService.MultiExecutor("updating firstname, lastname of user:: "+userDTO.getFirstName()+";"+userDTO.getLastName());
            return userService.updateUserInfoByUserName(userDTO,userName);
        }
        else{
            asyncService.MultiExecutor("updating password of user:: "+userDTO.getInputPassword());
            return userService.updatePasswordByUserName(userDTO,userName);
        }
    }
    //------------------------------------------------------------------------------------------
    @GetMapping("/user/info/{userName}")
    public User getUserByUserName(HttpServletRequest request, @PathVariable String userName) {
//        if (!this.sessionManagementUtil.doesSessionExist(request))
//        {
//            logger.info("Please login to access this page::");
//            throw new AuthException();
//        }
        return userRepository.findByUserName(userName)
                .orElseThrow(() -> new UserNotFoundException(userName));
    }
    //------------------------------------------------------------------------------------------
    @PostMapping("/user/forgot_password")
    public ResponseEntity processForgotPassword(HttpServletRequest request,@RequestBody UserDTO userDTO){

        String email = userDTO.getEmail();
        String token = RandomString.make(15);
        String siteURL = request.getRequestURL().toString();
        siteURL.replace(request.getServletPath(),"");

        try{
            userService.updateToken(token,email);
            String resetPasswordLink = siteURL + "/reset_password?token=" + token;
            emailValidationService.sendForgotPasswordLink(email,resetPasswordLink);

        }catch (UserNotFoundException e){
            e.getMessage();
        }catch (UnsupportedEncodingException | MessagingException ex){
            ex.getMessage();
        }
        return ResponseEntity.ok().build();
    }
    //------------------------------------------------------------------------------------------
    @GetMapping("forgot_password/reset_password")
    public User showResetPasswordForm(@Param(value="token")String token){
        return userService.getByToken(token);
    }
    //------------------------------------------------------------------------------------------
    @PostMapping("forgot_password/reset_password")
    public ResponseEntity enterPassword(@Param(value="token")String token,@RequestParam("password")String password, HttpServletRequest request){
        User user = userService.getByToken(token);
        userService.ResetPassword(user,password);
        return ResponseEntity.ok().build();
    }

}
