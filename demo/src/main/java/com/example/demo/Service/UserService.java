package com.example.demo.Service;

import com.example.demo.DAO.UserDAO;
import com.example.demo.DTO.LoginDTO;
import com.example.demo.DTO.UserDTO;
import com.example.demo.DTO.UpdatePasswordDTO;
import com.example.demo.Entity.User;
import com.example.demo.Exception.UserNotFoundException;
import com.example.demo.Repository.UserRepository;

import org.mindrot.jbcrypt.BCrypt;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;

import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import javax.transaction.Transactional;
import java.time.Instant;

@Service
public class UserService {
    private static final Logger logger = LoggerFactory.getLogger(UserService.class);
    @Autowired
    private UserDAO userDAO;
    @Autowired
    private UserRepository userRepository;

    @Lazy
    @Autowired
    private EmailValidationService emailValidationService;

public User registerUser(UserDTO userDTO){
    try{
        logger.info("Registering user:::"+ userDTO.getUserName()+"; "+userDTO.getEmail());
        String password = BCrypt.hashpw(userDTO.getPassword(), BCrypt.gensalt());
        User newUser = new User();
        newUser.setUserName(userDTO.getUserName());
        newUser.setEmail(userDTO.getEmail());
        newUser.setPassword(password);
        newUser.setRegisteredAt(Instant.now());
        newUser.setVerified("false");
        return userRepository.save(newUser);
    } catch (Exception e) {
        logger.error(e.getMessage(),e);
    }
    return null;
}
    public boolean checkIfUserRegistered (UserDTO userDTO) {
        try {
            logger.info("Checking if user exists:::" + userDTO.getUserName());
            logger.info("encodedEmail:::" + userDTO.getEmail());
            int res = userDAO.checkIfUserExistsByUsernameAndEmail(userDTO.getUserName(), userDTO.getEmail());
            if (res > 0) {
                return true;
            }
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }
        return false;
    }
    public Integer authenticate(LoginDTO login, HttpServletRequest request)
    {
        User user = null;
        try {
            logger.info("Checking if user authenticate:::"+login.getUserName());
            user = userRepository.findByUserName(login.getUserName()).orElse(null);
        } catch (Exception e) {
            logger.error(e.getMessage(),e);
        }
        if (user == null) {
            return -1;
        } else {
            if (BCrypt.checkpw(login.getPassword(), user.getPassword())) {
                //cannot use ==
                if(user.getVerified().equals("true")){
                    return 1;
                }
                else{
                    //api/user/login/email_validation?token=
                    UserDTO userDTO = new UserDTO();
                    userDTO.setEmail(user.getEmail());
                    emailValidationService.processEmailValidation(request,userDTO);
                    return 0;
                }
            }
            else{
                return 2;
            }
        }
    }
    public User getProfileByUserName (String userName)
    {
        try{
            logger.info("Getting user profile by user name:::"+userName);
            User user = userRepository.findByUserName(userName).orElseThrow(() -> new UserNotFoundException(userName));
            return user;
        }catch (Exception e){
            logger.error(e.getMessage(),e);
        }
        return null;
    }
    public User getUserProfileById(String id){
        try{
            logger.info("Getting user profile by id:::"+id);
            User user = userRepository.findById(id).orElseThrow(() -> new UserNotFoundException(id));
            return user;
        }catch (Exception e){
            logger.error(e.getMessage(),e);
        }
        return null;
    }
    @Transactional
    public User updateUserInfoByUserName(UserDTO userDTO, String userName){
        try{
            logger.info("Updating info of user:::"+userName);
            User user = userRepository.findByUserName(userName).orElse(null);
            if(user == null){
                return null;
            }
            else{
                if(!userDTO.getFirstName().isBlank()){
                    user.setFirstName(userDTO.getFirstName());
                }
                if(!userDTO.getLastName().isBlank()){
                    user.setLastName(userDTO.getLastName());
                }
                if(!userDTO.getPhone().isBlank()){
                    user.setPhone(userDTO.getPhone());
                }
            }
            return userRepository.save(user);
        }catch (RuntimeException e){
            logger.error(e.getMessage(),e);
        }
        return null;
    }
    @Transactional
    public Integer updatePasswordByUserName(UpdatePasswordDTO updatePasswordDTO, String userName) {
        Integer res =0;
        try {
            logger.info("Updating password of user:::" + userName);
            User user = userRepository.findByUserName(userName).orElse(null);
            if(user ==null){
                logger.info("User not found:::" + userName);
            }else{
                logger.info("Checking if password is correct:::" + updatePasswordDTO.getOldPassword());
                String oldPassword = user.getPassword();
                if (BCrypt.checkpw(updatePasswordDTO.getOldPassword(), oldPassword)) {
                    logger.info("Old password is correct:::" + updatePasswordDTO.getOldPassword());
                    String newPassword = BCrypt.hashpw(updatePasswordDTO.getNewPassword(), BCrypt.gensalt());
                    user.setPassword(newPassword);
                    userRepository.save(user);
                   res = 1;
                }else {
                    logger.info("Old password is incorrect:::" + updatePasswordDTO.getOldPassword());
                    res=-1;
                }
            }
        }catch (RuntimeException e) {
            logger.error(e.getMessage(), e);
        }
        return res;
    }
    @Transactional
    public boolean updateToken(String token, String email){
        try{
            logger.info("Updating token:::"+token);
            User user =  userRepository.findByEmail(email).orElseThrow(() -> new UserNotFoundException(email));
            user.setToken(token);
            userRepository.save(user);
            return true;
        }catch (RuntimeException e){
            logger.error(e.getMessage(),e);
        }
        return false;
    }
    @Transactional
    public boolean verifyUser(User user){
        try{
            logger.info("Verifying user::" +user.getUserName());
            user.setVerified("true");
            user.setToken(null);
            userRepository.save(user);
            logger.info("Saved user successfully:::");
            return true;
        }catch (RuntimeException e){
            logger.error(e.getMessage(),e);
        }
        return false;
    }
    @Transactional
    public User getByToken(String token){
        try{
            logger.info("Getting user by token:::"+token);
            User user = userRepository.findByToken(token).orElse(null);
            if(user != null){
                return user;
            }else{
                return null;
            }
        }catch (UserNotFoundException e){
            logger.error(e.getMessage(),e);
        }
        return null;
    }

    @Transactional
    public boolean ResetPassword(User user, String newPassword){
        try{
            logger.info("Resetting password of user:::"+user.getUserName());
            String password = BCrypt.hashpw(newPassword,BCrypt.gensalt());
            user.setPassword(password);
            user.setToken(null);
            userRepository.save(user);
            logger.info("Reset password successfully:::");
            RemoveToken(user);
            return true;
        }catch (Exception e){
            logger.error(e.getMessage(),e);
        }
        return false;
    }
    @Transactional
    public boolean RemoveToken(User user){
        try{
            logger.info("Removing token of user:::"+user.getUserName());
            user.setToken(null);
            userRepository.save(user);
            logger.info("Removed token successfully:::");
            return true;
        }catch (RuntimeException e){
            logger.error(e.getMessage(),e);
        }
        return false;
    }
}
