package com.example.demo.Controller;

import com.example.demo.DAO.UserDAO;
import com.example.demo.DTO.LoginDTO;
import com.example.demo.DTO.ResetPasswordDTO;
import com.example.demo.DTO.UserDTO;
import com.example.demo.DTO.UpdatePasswordDTO;
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

import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.util.HtmlUtils;

import javax.mail.MessagingException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.UUID;


@RestController
//@CrossOrigin(origins = "http://192.168.1.10:3000/")
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
        String encodedEmail = HtmlUtils.htmlEscape(userDTO.getEmail());
        //if all are blank
        if(userDTO.getEmail().isBlank() || userDTO.getUserName().isBlank()||userDTO.getPassword().isBlank()){
            ApiResponse errorResponse = ApiResponse.error(406,"Username, Email, Or Password Is Blank","Not Acceptable");
            return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).body(errorResponse);
        }//return true if name contains special characters or whitespaces
        else if(ValidString.verifyName(userDTO.getUserName())){
            ApiResponse errorResponse = ApiResponse.error(406,"Username Can't Contain Special Characters, Or Whitespaces","Not Acceptable");
            return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).body(errorResponse);
        }//return false if email doesn't match regex
        else if(!ValidString.verifyEmail(encodedEmail)){
            ApiResponse errorResponse = ApiResponse.error(406,"Invalid email address","Not Acceptable");
            return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).body(errorResponse);
        }//return true if password contains whitespaces
        else if(!ValidString.verifyPassword(userDTO.getPassword())){
            ApiResponse errorResponse = ApiResponse.error(406,"Password Can't Contain Whitespaces And Must Has At Least 1 Special Character","Not Acceptable");
            return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).body(errorResponse);
        }
        else if(userDTO.getPassword().length()<8){
            ApiResponse errorResponse = ApiResponse.error(406,"Password Must Be At Least 8 Characters Long","Not Acceptable");
            return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).body(errorResponse);
        }else{
            userDTO.setUserName(ValidString.fixUsername(userDTO.getUserName()));
        }
        //------------------check if user already exists------------------
        if(userService.checkIfUserRegistered(userDTO)){
            ApiResponse errorResponse = ApiResponse.error(409, "Username or Email Already Exists", "CONFLICT");
            return ResponseEntity.status(HttpStatus.CONFLICT).body(errorResponse);
        }else {
            userDTO.setEmail(encodedEmail);
            User user = userService.registerUser(userDTO);
            if(user!=null){
                logger.info("User successfully registered:::");
                if(emailValidationService.processEmailValidation(request,userDTO)){
                    logger.info("Email verification link sent:::");
                    ApiResponse apiResponse = ApiResponse.success(user);
                    return ResponseEntity.ok(apiResponse);
                }else{
                    ApiResponse<User> errorResponse = ApiResponse.error(500, "Verification Link Couldn't Send", "Internal Server Error");
                    return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
                }
            }else{
                ApiResponse errorResponse = ApiResponse.error(500, "Internal Server Error", "Internal Server Error");
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
            }

        }
}

    //------------------------------------------------------------------------------------------
    @PostMapping("/user/login")
    public ResponseEntity<ApiResponse<User>> UserLogin(@RequestBody LoginDTO loginDTO, HttpServletRequest request)throws IOException {
        //1---user
        //-1---no user
        //0---verify
        //2---wrong username or password
        if(loginDTO.getUserName().isBlank()||loginDTO.getPassword().isBlank()){
            ApiResponse errorResponse = ApiResponse.error(406,"Username Or Password Is Blank","Not Acceptable");
            return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).body(errorResponse);
        }
        Integer res = userService.authenticate(loginDTO,request);
        if(res==1) {
            User user = userService.getProfileByUserName(loginDTO.getUserName());
            ApiResponse<User> apiResponse = ApiResponse.success(user);
            return ResponseEntity.ok(apiResponse);
        }else if(res==-1){
            ApiResponse errorResponse = ApiResponse.error(404, "User Not Exists", "Not Found");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse);
        } else if(res==0){
            ApiResponse errorResponse = ApiResponse.error(401, "Account Is Not Verified, Email Verification Link Sent", "Unauthorized");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(errorResponse);
        }else {
            ApiResponse errorResponse = ApiResponse.error(401, "Wrong Username Or Password Entered", "Unauthorized");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(errorResponse);
        }
    }


    //------------------------------------------------------------------------------------------
    @PutMapping("/user/update/{userName}")
    public ResponseEntity<ApiResponse<User>> updateUserInfo(@RequestBody UserDTO userDTO, @PathVariable String userName){
        //if all are blank
        if(userDTO.getFirstName().isBlank() && userDTO.getLastName().isBlank() && userDTO.getPhone().isBlank()&&userDTO.getMiddleName().isBlank()){
            ApiResponse errorResponse = ApiResponse.error(406, "No Changes Were Found", "Not Acceptable");
            return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).body(errorResponse);
        }//return true if name contains special characters or whitespaces
        if(ValidString.verifyName(userDTO.getFirstName())
                ||ValidString.verifyName(userDTO.getMiddleName())
                ||ValidString.verifyName(userDTO.getLastName())){
            ApiResponse errorResponse = ApiResponse.error(406, "Name Cannot Contain Whitespaces Or Special Characters", "Not Acceptable");
            return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).body(errorResponse);
        }//return false if phone number doesn't match regex
        else if(!ValidString.verifyPhoneNumber(userDTO.getPhone())){
            ApiResponse errorResponse = ApiResponse.error(406, "Invalid phone number", "Not Acceptable");
            return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).body(errorResponse);
        }else{
            userDTO.setFirstName(ValidString.fixUsername(userDTO.getFirstName()));
            userDTO.setMiddleName(ValidString.fixUsername(userDTO.getMiddleName()));
            userDTO.setLastName(ValidString.fixUsername(userDTO.getLastName()));
        }
        User user = userService.updateUserInfoByUserName(userDTO,userName);
        if(user == null){
            ApiResponse errorResponse = ApiResponse.error(404, "User Not Exists", "Not Found");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
        }else{
            ApiResponse apiResponse = ApiResponse.success(user);
            return ResponseEntity.ok(apiResponse);
        }
    }
    //------------------------------------------------------------------------------------------
    @PutMapping("/user/password/update/{userName}")
    public ResponseEntity<ApiResponse<User>> updateUserPassword(@RequestBody UpdatePasswordDTO updatePasswordDTO, @PathVariable String userName){
        //if all are blank
        if(updatePasswordDTO.getNewPassword().isBlank() || updatePasswordDTO.getInputPassword().isBlank()){
            ApiResponse errorResponse = ApiResponse.error(406, "Old Password Or New Password Is Blank", "Not Acceptable");
            return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).body(errorResponse);
        }//return true if password contains whitespaces
        else if(!ValidString.verifyPassword(updatePasswordDTO.getNewPassword())){
            ApiResponse errorResponse = ApiResponse.error(406, "Password Can't Contain Whitespaces And Must Has At Least 1 Special Character", "Not Acceptable");
            return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).body(errorResponse);
        }else if(updatePasswordDTO.getNewPassword().length()<8){
            ApiResponse errorResponse = ApiResponse.error(406, "Password Must Be At Least 8 Characters Long", "Not Acceptable");
            return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).body(errorResponse);
        }
        Integer res = userService.updatePasswordByUserName(updatePasswordDTO,userName);
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
                ApiResponse errorResponse = ApiResponse.error(404, "User Not Exists", "Not Found");
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse);
            }else{
                ApiResponse<User> apiResponse = ApiResponse.success(user);
                return ResponseEntity.ok(apiResponse);
            }
    }
    //------------------------------------------------------------------------------------------
    @PostMapping("/user/forgot_password")
    public ResponseEntity<ApiResponse<String>> processForgotPassword(HttpServletRequest request,@RequestBody UserDTO userDTO) throws MessagingException, UnsupportedEncodingException {
        String encodedEmail =HtmlUtils.htmlEscape(userDTO.getEmail());
          if(!ValidString.verifyEmail(encodedEmail)){
            ApiResponse errorResponse = ApiResponse.error(406,"Invalid email address","Not Acceptable");
            return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).body(errorResponse);
        }
        UUID uuid = UUID.randomUUID();
        String token = uuid.toString();
        String siteURL = request.getRequestURL().toString();
        siteURL.replace(request.getServletPath(),"");

        if(!userService.updateToken(token,encodedEmail)) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }else{
            String resetPasswordLink = siteURL + "/reset_password?token=" + token;
            if(emailValidationService.sendForgotPasswordLink(encodedEmail,resetPasswordLink)){
                logger.info("Email has been sent out:::");
                ApiResponse<String> apiResponse = ApiResponse.success(resetPasswordLink);
                return ResponseEntity.ok(apiResponse);
            }else{
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
            }
        }
    }

    //------------------------------------------------------------------------------------------
    @PutMapping("/user/forgot_password/enter_password")
    public ResponseEntity<ApiResponse> enterPassword(@RequestBody ResetPasswordDTO resetPasswordDTO) {

        String password = resetPasswordDTO.getPassword();
        String confirmPassword = resetPasswordDTO.getConfirmPassword();
        logger.info("password is:::"+password);
        logger.info("confirmPassword is:::"+confirmPassword);
        logger.info("token is:::"+resetPasswordDTO.getToken());
        User user = userService.getByToken(resetPasswordDTO.getToken());
        if(password.isBlank() || confirmPassword.isBlank()||password==null||confirmPassword==null){
            ApiResponse errorResponse = ApiResponse.error(406, "Password Cannot Be Blank", "Not Acceptable");
        }
        else if(!ValidString.verifyPassword(password)){
            ApiResponse errorResponse = ApiResponse.error(406, "Password Can't Contain Whitespaces And Must Has At Least 1 Special Character", "Not Acceptable");
        }
        else if(!password.equals(confirmPassword)) {
            ApiResponse errorResponse = ApiResponse.error(406, "Password And Confirm Password Do Not Match", "Not Acceptable");
        }else if(password.length()<8){
            ApiResponse errorResponse = ApiResponse.error(406, "Password Must Be At Least 8 Characters Long", "Not Acceptable");
        }else{
            if(userService.ResetPassword(user, password)) {
                ApiResponse<User> apiResponse = ApiResponse.success(null);
                return ResponseEntity.ok(apiResponse);
            }else{
                ApiResponse errorResponse = ApiResponse.error(500, "Internal Server Error","Internal Server Error");
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
            }
        }
        return null;
    }

}
