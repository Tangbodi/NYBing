package com.example.demo.Controller;

import com.example.demo.DAO.UserDAO;
import com.example.demo.DTO.LoginDTO;
import com.example.demo.DTO.UserDTO;
import com.example.demo.Entity.SubCategory;
import com.example.demo.Entity.User;
import com.example.demo.Exception.*;
import com.example.demo.Repository.UserRepository;
import com.example.demo.Service.EmailValidationService;
import com.example.demo.Service.UserService;

import com.example.demo.Util.ApiResponse;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import net.bytebuddy.utility.RandomString;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.javamail.JavaMailSender;

import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.annotation.RequestBody;

import javax.mail.MessagingException;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.List;
import java.util.Map;
import java.util.Optional;


@RestController
@CrossOrigin(origins = "http://192.168.1.10:3000/")
public class UserController{
    private static final Logger logger = LoggerFactory.getLogger(UserController.class);
    @Autowired
    private UserService userService;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private JavaMailSender mailSender;
    @Autowired
    private EmailValidationService emailValidationService;

    @Autowired
    private UserDAO userDAO;
    @GetMapping("/getData")
    public String getData(){
        return "got data from backend";
    }

    @GetMapping("/getuser/{id}")
    public ResponseEntity< ApiResponse<Optional<User>>> getUserProfileById(@PathVariable String id){
        try{
            Optional<User> user = userService.getUserProfileById(id);
            ApiResponse<Optional<User>> apiResponse = ApiResponse.success(user);
            return ResponseEntity.ok(apiResponse);
        }catch (Exception e){
            ApiResponse<Optional<User>> errorResponse = ApiResponse.error(500, "Internal Server Error", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
        }
    }
    @PostMapping("/user/register")
    public ResponseEntity register (@RequestBody User newUser, BindingResult bindingResult, HttpServletRequest request) throws IOException, InterruptedException {

            try {
                if(userService.checkIfUserRegistered(newUser)){
                    throw new UserAlreadyExistsException();
                }else {
                    userService.registerUser(newUser);
                    logger.info("User successfully registered::");
                    emailValidationService.processEmailValidation(request,newUser);
                }
            }catch (Exception e){
                logger.error(e.getMessage(),e);
            }

        return ResponseEntity.ok().build();
    }

    //------------------------------------------------------------------------------------------
    @PostMapping("/user/login")
    public ResponseEntity<ApiResponse<User>> UserLogin(@RequestBody LoginDTO login, HttpServletRequest request)throws IOException {
        //1---user
        //-1---no user
        //0---verify
        //2---username or password error
        if (userService.authenticate(login,request)==1) {
            logger.info("Successfully authenticated::");
            User user = userService.getProfileByUserName(login.getUserName());
            ApiResponse<User> apiResponse = ApiResponse.success(user);
            return ResponseEntity.ok(apiResponse);
        }
        else if(userService.authenticate(login,request)==-1){
            ApiResponse<User> errorResponse = ApiResponse.error(404, "No such user exists", "Not Found");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse);
        }
        else if(userService.authenticate(login,request)==0){
            ApiResponse<User> errorResponse = ApiResponse.error(401, "You haven't verified your email account", "Unauthorized");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(errorResponse);
        }
        else{
            ApiResponse<User> errorResponse = ApiResponse.error(401, "Username or Password is wrong", "Unauthorized");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(errorResponse);
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
    public ResponseEntity<ApiResponse<User>> showEmailValidationViaLoginLink(@Param(value="token")String token){
        User user = userService.getByToken(token);
        ApiResponse<User> apiResponse = ApiResponse.success(user);
        return ResponseEntity.ok(apiResponse);
    }
    //------------------------------------------------------------------------------------------
    @PutMapping("/user/update/{userName}")
    public ResponseEntity updateUserByUserName(HttpServletRequest request, @RequestBody UserDTO userDTO, @PathVariable String userName) throws InterruptedException {

        if(userDTO.getNewPassword()==null){
            User user = userService.updateUserInfoByUserName(userDTO,userName);
            ApiResponse<User> apiResponse = ApiResponse.success(user);
            return ResponseEntity.ok(apiResponse);
        }
        else{
            User user = userService.updatePasswordByUserName(userDTO,userName);
            ApiResponse<User> apiResponse = ApiResponse.success(user);
            return ResponseEntity.ok(apiResponse);
        }
    }
    //------------------------------------------------------------------------------------------
    @GetMapping("/user/info/{userName}")
    public ResponseEntity< ApiResponse<Optional<User>>> getUserByUserName(HttpServletRequest request, @PathVariable String userName) {
        try{
            Optional<User> user = userRepository.findByUserName(userName);
            ApiResponse<Optional<User>> apiResponse = ApiResponse.success(user);
            return ResponseEntity.ok(apiResponse);
        }catch (Exception e){
            ApiResponse<Optional<User>> errorResponse = ApiResponse.error(500, "Internal Server Error", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
        }
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
            return ResponseEntity.ok().build();
        }catch (UserNotFoundException e){
            logger.error(e.getMessage(),e);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }catch (UnsupportedEncodingException | MessagingException ex){
            logger.error(ex.getMessage(),ex);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ex.getMessage());
        }

    }
    //------------------------------------------------------------------------------------------
    @GetMapping("forgot_password/reset_password")
    public ResponseEntity<ApiResponse<User>> showResetPasswordForm(@Param(value="token")String token){
        try{
            User user = userService.getByToken(token);
            ApiResponse<User> apiResponse = ApiResponse.success(user);
            return ResponseEntity.ok(apiResponse);
        }catch (Exception e){
            ApiResponse<User> errorResponse = ApiResponse.error(500, "Internal Server Error", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
        }
    }
    //------------------------------------------------------------------------------------------
    @PostMapping("forgot_password/reset_password")
    public ResponseEntity<ApiResponse<User>> enterPassword(@Param(value="token")String token,@RequestParam("password")String password, HttpServletRequest request){
        try{
            User user = userService.getByToken(token);
            userService.ResetPassword(user,password);
            ApiResponse<User> apiResponse = ApiResponse.success(user);
            return ResponseEntity.ok(apiResponse);
        }catch (Exception e){
            ApiResponse<User> errorResponse = ApiResponse.error(500, "Internal Server Error", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
        }
    }
}
