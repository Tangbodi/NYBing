package com.example.demo.Controller;

import com.example.demo.DTO.LoginDTO;
import com.example.demo.DTO.ResetPasswordDTO;
import com.example.demo.DTO.UserDTO;
import com.example.demo.DTO.UpdatePasswordDTO;
import com.example.demo.Entity.User;
import com.example.demo.Service.EmailValidationService;

import com.example.demo.Service.UserService;

import com.example.demo.Util.ApiResponse;
import com.example.demo.Validator.ValidString;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.util.HtmlUtils;

import javax.mail.MessagingException;
import javax.servlet.http.HttpServletRequest;
import java.io.UnsupportedEncodingException;
import java.util.UUID;


@RestController
//@CrossOrigin(origins = "http://192.168.1.10:3000/")
public class UserController{
    private static final Logger logger = LoggerFactory.getLogger(UserController.class);

    @Autowired
    private UserService userService;

    @Autowired
    private EmailValidationService emailValidationService;


    @GetMapping("/getData")
    public String getData(){
        return "got data from backend";
    }

    @PostMapping("/user/register")
    public ResponseEntity register (@RequestBody UserDTO userDTO, HttpServletRequest request){
        String encodedEmail = HtmlUtils.htmlEscape(userDTO.getEmail());
        //if all are blank
        if(!ValidString.UserNameEmpty(userDTO.getUserName()) || !ValidString.PasswordEmpty(userDTO.getPassword()) || !ValidString.EmailEmpty(encodedEmail)){
            ApiResponse errorResponse = ApiResponse.error(406,"Username, Email, Or Password Is Blank:::","Not Acceptable");
            return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).body(errorResponse);
        }//return false if name is too long or too short
        else if(!ValidString.UserNameLength(userDTO.getUserName())){
            ApiResponse errorResponse = ApiResponse.error(406,"Username Needs To Be Between 1 To 18 Characters","Not Acceptable");
            return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).body(errorResponse);
        }//return false if name contains special characters or whitespaces
        else if(!ValidString.validUsername(userDTO.getUserName())){
            ApiResponse errorResponse = ApiResponse.error(406,"Username Can't Contain Special Characters, Or Whitespaces","Not Acceptable");
            return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).body(errorResponse);
        }else if(!ValidString.EmailLength(encodedEmail)){
            ApiResponse errorResponse = ApiResponse.error(406,"Invalid email address","Not Acceptable");
            return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).body(errorResponse);
        }
        else if(!ValidString.validEmail(encodedEmail)){
            ApiResponse errorResponse = ApiResponse.error(406,"Invalid email address","Not Acceptable");
            return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).body(errorResponse);
        }
        else if(!ValidString.PasswordLength(userDTO.getPassword())){
            ApiResponse errorResponse = ApiResponse.error(406,"Password Must Be Between 8 Characters And 30 Characters Long","Not Acceptable");
            return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).body(errorResponse);
        }
            //return false if password contains whitespaces
        else if(!ValidString.validPassword(userDTO.getPassword())){
            ApiResponse errorResponse = ApiResponse.error(406,"Password Must Has At Least 1 Special Character","Not Acceptable");
            return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).body(errorResponse);
        }
        else{
            userDTO.setUserName(ValidString.fixName(userDTO.getUserName()));
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
    public ResponseEntity UserLogin(@RequestBody LoginDTO loginDTO, HttpServletRequest request){
        //1---user
        //-1---no user
        //0---verify
        //2---wrong username or password
        if(!ValidString.UserNameEmpty(loginDTO.getUserName()) || !ValidString.PasswordEmpty(loginDTO.getPassword())){
            ApiResponse errorResponse = ApiResponse.error(406,"Username Or Password Is Blank","Not Acceptable");
            return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).body(errorResponse);
        }else if(!ValidString.validUsername(loginDTO.getUserName())) {
            ApiResponse errorResponse = ApiResponse.error(404, "User Doesn't Exist", "Not Found");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse);
        }else if(!ValidString.validPassword(loginDTO.getPassword())){
            ApiResponse errorResponse = ApiResponse.error(400, "Wrong Username Or Password Entered", "Bad Request");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
        }
        Integer res = userService.authenticate(loginDTO,request);
        if(res==1) {
            User user = userService.getProfileByUserName(loginDTO.getUserName());
            ApiResponse<User> apiResponse = ApiResponse.success(user);
            return ResponseEntity.ok(apiResponse);
        }else if(res==-1){
            ApiResponse errorResponse = ApiResponse.error(404, "User Doesn't Exist", "Not Found");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse);
        } else if(res==0){
            ApiResponse errorResponse = ApiResponse.error(401, "Account Is Not Verified, Email Verification Link Sent", "Unauthorized");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(errorResponse);
        }else {
            ApiResponse errorResponse = ApiResponse.error(400, "Wrong Username Or Password Entered", "Bad Request");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
        }
    }


    //------------------------------------------------------------------------------------------
    @PutMapping("/user/update/{userName}")
    public ResponseEntity updateUserInfo(@RequestBody UserDTO userDTO, @PathVariable String userName){
        //if all are blank
        if(!ValidString.UserNameEmpty(userDTO.getFirstName())
                && !ValidString.UserNameEmpty(userDTO.getLastName())
                && !ValidString.PhoneNumberEmpty(userDTO.getPhone())){
            ApiResponse errorResponse = ApiResponse.error(400, "No Changes Were Found", "Bad Request");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
        }else if((ValidString.UserNameEmpty(userDTO.getFirstName()) && !ValidString.UserNameLength(userDTO.getFirstName()))
                || (ValidString.UserNameEmpty(userDTO.getLastName()) && !ValidString.UserNameLength(userDTO.getLastName()))) {
            ApiResponse errorResponse = ApiResponse.error(406,"Name Needs To Be Between 1 To 18 Characters","Not Acceptable");
            return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).body(errorResponse);
        }
        //return true if name contains special characters or whitespaces
        else if((ValidString.UserNameEmpty(userDTO.getFirstName()) && !ValidString.validUsername(userDTO.getFirstName()))
                || (ValidString.UserNameEmpty(userDTO.getLastName()) && !ValidString.validUsername(userDTO.getLastName()))){
            ApiResponse errorResponse = ApiResponse.error(406, "Name Cannot Contain Whitespaces Or Special Characters", "Not Acceptable");
            return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).body(errorResponse);
        }
        //return false if phone number doesn't match regex
        else if((ValidString.PhoneNumberEmpty(userDTO.getPhone()) && !ValidString.PhoneNumberLength(userDTO.getPhone()))){
            ApiResponse errorResponse = ApiResponse.error(406, "Invalid phone number", "Not Acceptable");
            return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).body(errorResponse);
        }
//        else if((ValidString.PhoneNumberEmpty(userDTO.getPhone()) && !ValidString.validPhoneNumber(userDTO.getPhone()))){
//            ApiResponse errorResponse = ApiResponse.error(406, "Invalid phone number", "Not Acceptable");
//            return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).body(errorResponse);
//        }
        else{
            userDTO.setFirstName(ValidString.fixName(userDTO.getFirstName()));
            userDTO.setLastName(ValidString.fixName(userDTO.getLastName()));
        }
        User user = userService.updateUserInfoByUserName(userDTO,userName);
        if(user == null){
            ApiResponse errorResponse = ApiResponse.error(404, "User Doesn't Exist", "Not Found");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
        }else{
            ApiResponse apiResponse = ApiResponse.success(user);
            return ResponseEntity.ok(apiResponse);
        }
    }
    //------------------------------------------------------------------------------------------
    @PutMapping("/user/password/update/{userName}")
    public ResponseEntity updateUserPassword(@RequestBody UpdatePasswordDTO updatePasswordDTO, @PathVariable String userName){

        if(!ValidString.PasswordEmpty(updatePasswordDTO.getOldPassword()) || !ValidString.PasswordEmpty(updatePasswordDTO.getNewPassword())){
            ApiResponse errorResponse = ApiResponse.error(400, "Old Password Or New Password Is Blank", "Bad Request");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
        }else if(!ValidString.PasswordLength(updatePasswordDTO.getOldPassword()) || !ValidString.PasswordLength(updatePasswordDTO.getNewPassword())){
            ApiResponse errorResponse = ApiResponse.error(406, "Password Must Be At Least 8 Characters Long", "Not Acceptable");
            return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).body(errorResponse);
        }//return true if password contains whitespaces
        else if(!ValidString.validPassword(updatePasswordDTO.getOldPassword()) || !ValidString.validPassword(updatePasswordDTO.getNewPassword())){
            ApiResponse errorResponse = ApiResponse.error(406, "Password Must Has At Least 1 Special Character And Can't Contain Whitespaces", "Not Acceptable");
            return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).body(errorResponse);
        }else {
            //NEVER TOUCH THIS
        }
        Integer res = userService.updatePasswordByUserName(updatePasswordDTO,userName);
        //0---no user
        //1---success
        //-1---wrong password
        //null---internal server error
        if(res == 0){
            ApiResponse errorResponse = ApiResponse.error(404, "User Doesn't Exist","Not Found");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }else if(res == -1){
            ApiResponse errorResponse = ApiResponse.error(400, "Wrong Password Entered","Bad Request");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
        }else if(res == 1){
            logger.info("Password Updated Successfully");
            ApiResponse<User> apiResponse = ApiResponse.success(null);
            return ResponseEntity.ok(apiResponse);
        }else {
            //NEVER TOUCH THIS
        }
        ApiResponse errorResponse = ApiResponse.error(500, "Internal Server Error","Internal Server Error");
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
    }
    //------------------------------------------------------------------------------------------
    @GetMapping("/user/info/{userName}")
    public ResponseEntity getUserByUserName(@PathVariable String userName) {
        if(!ValidString.UserNameEmpty(userName) || !ValidString.UserNameLength(userName) || !ValidString.validUsername(userName)){
                ApiResponse errorResponse = ApiResponse.error(404, "User Not Exists", "Not Found");
                return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).body(errorResponse);
        }
        userName = ValidString.fixName(userName);
        User user = userService.getProfileByUserName(userName);
        if(user == null){
            ApiResponse errorResponse = ApiResponse.error(404, "User Doesn't Exist", "Not Found");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse);
        }else{
            ApiResponse<User> apiResponse = ApiResponse.success(user);
            return ResponseEntity.ok(apiResponse);
        }
    }
    //------------------------------------------------------------------------------------------
    @PostMapping("/user/forgot_password")
    public ResponseEntity processForgotPassword(HttpServletRequest request, @RequestBody UserDTO userDTO) throws MessagingException, UnsupportedEncodingException {
          if(!ValidString.EmailEmpty(userDTO.getEmail()) || !ValidString.EmailLength(userDTO.getEmail()) || !ValidString.validEmail(userDTO.getEmail())){
            ApiResponse errorResponse = ApiResponse.error(404,"User Doesn't Exist","Not Found");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse);
        }
        String encodedEmail =HtmlUtils.htmlEscape(userDTO.getEmail());
        UUID uuid = UUID.randomUUID();
        String token = uuid.toString();
        String siteURL = request.getRequestURL().toString();
        siteURL.replace(request.getServletPath(),"");

        if(!userService.updateToken(token,encodedEmail)) {
            ApiResponse errorResponse = ApiResponse.error(404, "User Doesn't Exist", "Not Found");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse);
        }else{
            String resetPasswordLink = siteURL + "/reset_password?token=" + token;
//            kafkaPasswordResetProducerService.sendPasswordResetRequest(encodedEmail);
            if(emailValidationService.sendForgotPasswordLink(encodedEmail,resetPasswordLink)){
                logger.info("Email has been sent out:::");
                ApiResponse<String> apiResponse = ApiResponse.success(resetPasswordLink);
                return ResponseEntity.ok(apiResponse);
            }

                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();

        }
    }

    //------------------------------------------------------------------------------------------
    @PutMapping("/user/forgot_password/enter_password")
    public ResponseEntity<ApiResponse> enterPassword(@RequestBody ResetPasswordDTO resetPasswordDTO) {
        if(!ValidString.PasswordEmpty(resetPasswordDTO.getPassword()) || !ValidString.PasswordEmpty(resetPasswordDTO.getConfirmPassword())){
            ApiResponse errorResponse = ApiResponse.error(400, "Password Or Confirm Password Is Blank", "Bad Request");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
        }else if(!ValidString.PasswordLength(resetPasswordDTO.getPassword()) || !ValidString.PasswordLength(resetPasswordDTO.getConfirmPassword())){
            ApiResponse errorResponse = ApiResponse.error(406, "Password Must Be At Least 8 Characters Long", "Not Acceptable");
            return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).body(errorResponse);
        }//return true if password contains whitespaces
        else if(!ValidString.validPassword(resetPasswordDTO.getPassword()) || !ValidString.validPassword(resetPasswordDTO.getConfirmPassword())){
            ApiResponse errorResponse = ApiResponse.error(406, "Password Must Has At Least 1 Special Character And Can't Contain Whitespaces", "Not Acceptable");
            return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).body(errorResponse);
        }
        String password = resetPasswordDTO.getPassword();
        String confirmPassword = resetPasswordDTO.getConfirmPassword();
        logger.info("password is:::"+password);
        logger.info("confirmPassword is:::"+confirmPassword);
        logger.info("token is:::"+resetPasswordDTO.getToken());
        User user = userService.getByToken(resetPasswordDTO.getToken());
        if(userService.ResetPassword(user, password)) {
            ApiResponse<User> apiResponse = ApiResponse.success(null);
            return ResponseEntity.ok(apiResponse);
        }else{
            ApiResponse errorResponse = ApiResponse.error(500, "Internal Server Error","Internal Server Error");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
        }

    }

}
