package com.example.demo.Controller;

import com.example.demo.DAO.UserDAO;
import com.example.demo.DTO.LoginDTO;
import com.example.demo.DTO.UserDTO;
import com.example.demo.DTO.UserPasswordDTO;
import com.example.demo.Entity.SubCategory;
import com.example.demo.Entity.User;
import com.example.demo.Exception.*;
import com.example.demo.Repository.UserRepository;
import com.example.demo.Service.EmailValidationService;
import com.example.demo.Service.UserService;

import com.example.demo.Util.ApiResponse;
import net.bytebuddy.utility.RandomString;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.javamail.JavaMailSender;

import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.annotation.RequestBody;

import javax.mail.MessagingException;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.List;
import java.util.Map;
import java.util.UUID;


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

    @PostMapping("/user/register")
    public ResponseEntity<ApiResponse<User>> register (@RequestBody UserDTO userDTO, HttpServletRequest request){

        if(userDTO.getEmail().isBlank() ||  userDTO.getUserName().isBlank()||userDTO.getPassword().isBlank()){
            ApiResponse errorResponse = ApiResponse.error(406,"Username, Email, Or Password Is Blank","Not Acceptable");
            return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).body(errorResponse);
        }
        if(!userDTO.getUserName().trim().matches("^[\\p{L}\\p{N}\\s]*$")){
            logger.info("Username is :::"+userDTO.getUserName());
            ApiResponse errorResponse = ApiResponse.error(406,"Username Can't Contain Special Characters","Not Acceptable");
            return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).body(errorResponse);
        }
        String encodedEmail = org.springframework.web.util.HtmlUtils.htmlEscape(userDTO.getEmail());
        userDTO.setEmail(encodedEmail);
        if(userService.checkIfUserRegistered(userDTO)){
            ApiResponse<User> errorResponse = ApiResponse.error(409, "User Already Exists", "CONFLICT");
            return ResponseEntity.status(HttpStatus.CONFLICT).body(errorResponse);
        }else {
            User user = userService.registerUser(userDTO);
            if(user!=null){
                logger.info("User successfully registered:::");
            }else{
                ApiResponse<User> errorResponse = ApiResponse.error(500, "Internal Server Error", "Internal Server Error");
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
            }
            if(emailValidationService.processEmailValidation(request,userDTO)){
                logger.info("Email verification link sent:::");
                ApiResponse<User> apiResponse = ApiResponse.success(user);
                return ResponseEntity.ok(apiResponse);
            }else{
                ApiResponse<User> errorResponse = ApiResponse.error(500, "Verification Link Couldn't Send", "Internal Server Error");
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
        }
    }
}

    //------------------------------------------------------------------------------------------
    @PostMapping("/user/login")
    public ResponseEntity<ApiResponse<User>> UserLogin(@RequestBody LoginDTO login, HttpServletRequest request)throws IOException {
        //1---user
        //-1---no user
        //0---verify
        //2---wrong username or password
        Integer res = userService.authenticate(login,request);
        if(res==1) {
            logger.info("Successfully authenticated::");
            User user = userService.getProfileByUserName(login.getUserName());
            ApiResponse<User> apiResponse = ApiResponse.success(user);
            return ResponseEntity.ok(apiResponse);
        }
        if(res==-1){
            ApiResponse<User> errorResponse = ApiResponse.error(404, "User Not Exists", "Not Found");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse);
        }
        if(res==0){
            ApiResponse<User> errorResponse = ApiResponse.error(401, "Account Is Not Verified, Email Verification Link Sent", "Unauthorized");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(errorResponse);
        }
        ApiResponse<User> errorResponse = ApiResponse.error(401, "Wrong Username Or Password Entered", "Unauthorized");
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(errorResponse);

    }
    //------------------------------------------------------------------------------------------
    @GetMapping("/user/register/email_validation")
    public ResponseEntity showEmailValidationPageViaRegisterLink(@Param(value="token")String token){
         if(userService.getByToken(token)==null){
                return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
         }
         else{
             return ResponseEntity.ok().build();
         }
    }
    //------------------------------------------------------------------------------------------
    @GetMapping("/user/login/email_validation")
    public ResponseEntity<ApiResponse<User>> showEmailValidationViaLoginLink(@Param(value="token")String token){
        if(userService.getByToken(token)==null){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        else{
            User user = userService.getByToken(token);
            ApiResponse<User> apiResponse = ApiResponse.success(user);
            return ResponseEntity.ok(apiResponse);
        }
    }
    //------------------------------------------------------------------------------------------
    @PutMapping("/user/update/{userName}")
    public ResponseEntity<ApiResponse<User>> updateUserInfo(@RequestBody UserDTO userDTO, @PathVariable String userName){
        if(userDTO.getFirstName().isBlank() && userDTO.getLastName().isBlank() && userDTO.getPhone().isBlank()&&userDTO.getMiddleName().isBlank()){
            ApiResponse<User> errorResponse = ApiResponse.error(406, "No Changes Were Found", "Not Acceptable");
            return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).body(errorResponse);
        }
        if(!userDTO.getFirstName().trim().matches("\"^[\\\\p{L}\\\\p{N}\\\\s]*$\"")
                ||!userDTO.getMiddleName().trim().matches("\"^[\\\\p{L}\\\\p{N}\\\\s]*$\"")
                ||!userDTO.getLastName().trim().matches("\"^[\\\\p{L}\\\\p{N}\\\\s]*$\"")
                ||!userDTO.getPhone().trim().matches("\"^[\\\\p{L}\\\\p{N}\\\\s]*$\"")){
            ApiResponse<User> errorResponse = ApiResponse.error(406, "Name Or Phone Cannot Contain Special Characters Or Emojis", "Not Acceptable");
            return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).body(errorResponse);
        }
        User user = userService.updateUserInfoByUserName(userDTO,userName);
        if(user == null){
            ApiResponse<User> errorResponse = ApiResponse.error(404, "User Not Exists", "Not Found");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
        }else{
            ApiResponse<User> apiResponse = ApiResponse.success(user);
            return ResponseEntity.ok(apiResponse);
        }
    }
    //------------------------------------------------------------------------------------------
    @PutMapping("/user/password/update/{userName}")
    public ResponseEntity<ApiResponse<User>> updateUserPassword(@RequestBody UserPasswordDTO userPasswordDTO, @PathVariable String userName){
        if(userPasswordDTO.getNewPassword().isBlank() || userPasswordDTO.getInputPassword().isBlank()){
            ApiResponse errorResponse = ApiResponse.error(406, "Old Password Or New Password Is Blank", "Not Acceptable");
            return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).body(errorResponse);
        }
        Integer res = userService.updatePasswordByUserName(userPasswordDTO,userName);
        //0---no user
        //1---success
        //-1---wrong password
        //null---internal server error
        if(res == 0){
            ApiResponse errorResponse = ApiResponse.error(404, "User Not Exists","Not Found");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        if(res == -1){
            ApiResponse errorResponse = ApiResponse.error(400, "Wrong Password Entered","Bad Request");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
        }
        if(res == 1){
            ApiResponse<User> apiResponse = ApiResponse.success(null);
            return ResponseEntity.ok(apiResponse);
        }
        ApiResponse errorResponse = ApiResponse.error(500, "Internal Server Error","Internal Server Error");
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
    }
    //------------------------------------------------------------------------------------------
    @GetMapping("/user/info/{userName}")
    public ResponseEntity<ApiResponse<User>> getUserByUserName(HttpServletRequest request, @PathVariable String userName) {

            User user = userService.getProfileByUserName(userName);
            if(user == null){
                ApiResponse<User> errorResponse = ApiResponse.error(404, "User Not Exists", "Not Found");
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse);
            }else{
                ApiResponse<User> apiResponse = ApiResponse.success(user);
                return ResponseEntity.ok(apiResponse);
            }
    }
    //------------------------------------------------------------------------------------------
    @PostMapping("/user/forgot_password")
    public ResponseEntity processForgotPassword(HttpServletRequest request,@RequestBody UserDTO userDTO){
        String encodedEmail = org.springframework.web.util.HtmlUtils.htmlEscape(userDTO.getEmail());
        UUID uuid = UUID.randomUUID();
        String token = uuid.toString();
        String siteURL = request.getRequestURL().toString();
        siteURL.replace(request.getServletPath(),"");

        if(!userService.updateToken(token,encodedEmail)) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }else{
            try{
                String resetPasswordLink = siteURL + "/reset_password?token=" + token;
                if(emailValidationService.sendForgotPasswordLink(encodedEmail,resetPasswordLink)){
                    logger.info("Email has been sent out:::");
                    return ResponseEntity.ok().build();
                }
            }catch (UserNotFoundException e){
                logger.error(e.getMessage(),e);
            }catch (UnsupportedEncodingException | MessagingException ex){
                logger.error(ex.getMessage(),ex);
            }
        }
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
    }
    //------------------------------------------------------------------------------------------
    @GetMapping("forgot_password/reset_password")
    public ResponseEntity showResetPasswordForm(@Param(value="token")String token){
            User user = userService.getByToken(token);
            if(user != null) {
                return ResponseEntity.ok().build();
            }else{
                return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
            }
    }
    //------------------------------------------------------------------------------------------
    @PostMapping("forgot_password/reset_password")
    public ResponseEntity enterPassword(@Param(value="token")String token,@RequestParam("password")String password){

            User user = userService.getByToken(token);
            if(user != null){
                if(userService.ResetPassword(user,password)){
                    return ResponseEntity.ok().build();
                }else{
                    return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
                }
            }else{
                return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
            }
        }
}
