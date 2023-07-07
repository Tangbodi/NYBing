package com.example.demo.Controller;

import com.example.demo.DAO.UserDAO;
import com.example.demo.DTO.LoginDTO;
import com.example.demo.DTO.UserDTO;
import com.example.demo.DTO.UserPasswordDTO;
import com.example.demo.Entity.User;
import com.example.demo.Exception.*;
import com.example.demo.Repository.UserRepository;
import com.example.demo.Service.EmailValidationService;
import com.example.demo.Service.UserService;

import com.example.demo.Util.ApiResponse;
import com.example.demo.Validator.ValidString;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
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
import java.io.IOException;
import java.io.UnsupportedEncodingException;
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
        else if(ValidString.verifyPassword(userDTO.getPassword())){
            ApiResponse errorResponse = ApiResponse.error(406,"Password Can't Contain Whitespaces","Not Acceptable");
            return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).body(errorResponse);
        }else{
            userDTO.setUserName(ValidString.fixUsername(userDTO.getUserName()));
        }
        //------------------check if user already exists------------------
        if(userService.checkIfUserRegistered(userDTO)){
            ApiResponse<User> errorResponse = ApiResponse.error(409, "Username or Email Already Exists", "CONFLICT");
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
        System.out.println(res);
        if(res==1) {
            logger.info("Successfully authenticated::");
            User user = userService.getProfileByUserName(loginDTO.getUserName());
            ApiResponse<User> apiResponse = ApiResponse.success(user);
            return ResponseEntity.ok(apiResponse);
        }else if(res==-1){
            ApiResponse<User> errorResponse = ApiResponse.error(404, "User Not Exists", "Not Found");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse);
        } else if(res==0){
            ApiResponse<User> errorResponse = ApiResponse.error(401, "Account Is Not Verified, Email Verification Link Sent", "Unauthorized");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(errorResponse);
        }else {
            ApiResponse<User> errorResponse = ApiResponse.error(401, "Wrong Username Or Password Entered", "Unauthorized");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(errorResponse);
        }
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
        //if all are blank
        if(userDTO.getFirstName().isBlank() && userDTO.getLastName().isBlank() && userDTO.getPhone().isBlank()&&userDTO.getMiddleName().isBlank()){
            ApiResponse<User> errorResponse = ApiResponse.error(406, "No Changes Were Found", "Not Acceptable");
            return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).body(errorResponse);
        }//return true if name contains special characters or whitespaces
        if(ValidString.verifyName(userDTO.getFirstName())
                ||ValidString.verifyName(userDTO.getMiddleName())
                ||ValidString.verifyName(userDTO.getLastName())){
            ApiResponse<User> errorResponse = ApiResponse.error(406, "Name Cannot Contain Whitespaces Or Special Characters", "Not Acceptable");
            return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).body(errorResponse);
        }//return false if phone number doesn't match regex
        else if(!ValidString.verifyPhoneNumber(userDTO.getPhone())){
            ApiResponse<User> errorResponse = ApiResponse.error(406, "Invalid phone number", "Not Acceptable");
            return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).body(errorResponse);
        }else{
            userDTO.setFirstName(ValidString.fixUsername(userDTO.getFirstName()));
            userDTO.setMiddleName(ValidString.fixUsername(userDTO.getMiddleName()));
            userDTO.setLastName(ValidString.fixUsername(userDTO.getLastName()));
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
        //if all are blank
        if(userPasswordDTO.getNewPassword().isBlank() || userPasswordDTO.getInputPassword().isBlank()){
            ApiResponse errorResponse = ApiResponse.error(406, "Old Password Or New Password Is Blank", "Not Acceptable");
            return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).body(errorResponse);
        }//return true if password contains whitespaces
        else if(ValidString.verifyPassword(userPasswordDTO.getNewPassword())){
            ApiResponse errorResponse = ApiResponse.error(406, "Password Cannot Contain Whitespaces", "Not Acceptable");
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
//    @GetMapping("/user/forgot_password/reset_password")
//    public ResponseEntity  showResetPasswordForm(@Param(value="token")String token){
//        logger.info("showResetPasswordForm endpoint called");
//        if(token==null){
//            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
//        }
//            User user = userService.getByToken(token);
//            if(user != null) {
//                return ResponseEntity.ok().build();
//            }else{
//
//                return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
//            }
//    }
    //------------------------------------------------------------------------------------------
//    @GetMapping("/user/forgot_password/reset_password")
//    @PostMapping("/user/forgot_password/reset_password")
//    public ResponseEntity enterPassword(@Param(value="token")String token){
//        logger.info("enterPassword endpoint called");
//        if(token==null){
//            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
//        }
////            User user = userService.getByToken(token);
////            if(user != null){
////                if(userService.ResetPassword(user,password)){
////                    return ResponseEntity.ok().build();
////                }else{
////                    return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
////                }
////            }else{
////                return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
////            }
//        return ResponseEntity.ok().build();
//        }
        // @PostMapping("/forgot_password")
    //    public ModelAndView processForgotPassword(HttpServletRequest request,@RequestParam("email")String email) throws MessagingException, UnsupportedEncodingException {
    //        String encodedEmail =HtmlUtils.htmlEscape(email);
    //        ModelAndView modelAndView = new ModelAndView();
    //          if(!ValidString.verifyEmail(encodedEmail)){
    //                modelAndView.addObject("error","Invalid Email Address");
    ////            ApiResponse errorResponse = ApiResponse.error(406,"Invalid email address","Not Acceptable");
    ////            return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).body(errorResponse);
    //        }
    //        UUID uuid = UUID.randomUUID();
    //        String token = uuid.toString();
    //        String siteURL = request.getRequestURL().toString();
    //        siteURL.replace(request.getServletPath(),"");
    //
    //        if(!userService.updateToken(token,encodedEmail)) {
    //            modelAndView.addObject("error","Email Address Not Found");
    ////            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    //        }else{
    //            String resetPasswordLink = siteURL + "/reset_password?token=" + token;
    //            if(emailValidationService.sendForgotPasswordLink(encodedEmail,resetPasswordLink)){
    //                logger.info("Email has been sent out:::");
    ////                ApiResponse<String> apiResponse = ApiResponse.success(resetPasswordLink);
    ////                return ResponseEntity.ok(apiResponse);
    //                modelAndView.addObject("message","Email Sent Successfully");
    //            }else{
    //                modelAndView.addObject("error","Email Sent Failed");
    ////                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
    //            }
    //        }
    //        modelAndView.setViewName("forgot_password_form");
    //        return modelAndView;
    //    }
    //    //------------------------------------------------------------------------------------------
    //    @GetMapping("/forgot_password")
    //    public ModelAndView showForgotPasswordForm() {
    //        ModelAndView mv = new ModelAndView("forgot_password_form");
    //        return mv;
    //    }
    //    //------------------------------------------------------------------------------------------
        @GetMapping("/user/forgot_password/reset_password")
        public ModelAndView showResetPasswordForm(@RequestParam(value="token")String token){
            ModelAndView mv = new ModelAndView();

            if(token==null){
    //            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
                mv.addObject("message", "Invalid Token");
            }
                User user = userService.getByToken(token);
                if(user != null) {
                    mv.addObject("token", token);
    //                return ResponseEntity.ok().build();
                }else{
                    mv.addObject("message", "Invalid Token");
    //                return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
                }
                mv.setViewName("reset_password_form");
                return mv;
        }
    //    //------------------------------------------------------------------------------------------

    @PostMapping("/user/forgot_password/reset_password")
        public ModelAndView enterPassword(@Param(value="token")String token, @RequestParam("password")String password, Model model) {
    //        ModelAndView mv = new ModelAndView();
    //        if(token==null){
    ////            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    //            model.addAttribute("message", "Invalid Token");
    //        }
    //        model.addAttribute("token",token);
            ModelAndView mv = new ModelAndView();
            User user = userService.getByToken(token);
            if (user != null) {
                if (userService.ResetPassword(user, password)) {
    //                    return ResponseEntity.ok().build();
                    mv.addObject("message", "Password Reset Successfully");
                    return new ModelAndView("forgot_password_form");
    //                    return new ModelAndView("forgot_password_form");
                } else {
    //                    return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
                    mv.addObject("message", "Internal Server Error");
                    return new ModelAndView("forgot_password_form");
                }
    //            }else{
    ////                return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    //                model.addAttribute("message", "Invalid Token");
    //                return new ModelAndView("forgot_password_form");
    //            }
            }
            mv.setViewName("forgot_password_form");
            return mv;
        }
}
