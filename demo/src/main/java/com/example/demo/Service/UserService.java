package com.example.demo.Service;

import com.example.demo.DAO.UserDAO;
import com.example.demo.DTO.LoginDTO;
import com.example.demo.DTO.UserDTO;
import com.example.demo.DTO.UpdatePasswordDTO;
import com.example.demo.Entity.User;
import com.example.demo.Exception.UserNotFoundException;
import com.example.demo.Repository.UserRepository;

import com.example.demo.Validator.ValidString;
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
    @Transactional(rollbackOn = Exception.class)
    public User registerUser(UserDTO userDTO){
        try {
            logger.info("Registering user: {}", userDTO.getUserName());
            String password = BCrypt.hashpw(userDTO.getPassword(), BCrypt.gensalt());
            User newUser = new User();
            newUser.setUserName(userDTO.getUserName());
            newUser.setEmail(userDTO.getEmail());
            newUser.setPassword(password);
            newUser.setRegisteredAt(Instant.now());
            newUser.setVerified("false");
            return userRepository.save(newUser);
        } catch (Exception e) {
            logger.error("Failed to register user", e);
            throw new RuntimeException("Failed to register user", e);
        }

}

    public boolean checkIfUserRegistered (UserDTO userDTO) {
        try {
            logger.info("Checking if user exists:::" + userDTO.getUserName());
            logger.info("encodedEmail:::" + userDTO.getEmail());
            int res = userDAO.checkIfUserExistsByUsernameAndEmail(userDTO.getUserName(), userDTO.getEmail());
            return res>0;
        } catch (Exception e) {
            logger.error("Failed to check if user exists", e);
            throw new RuntimeException("Failed to check if user exists", e);
        }
    }

    public Integer authenticate(LoginDTO login, HttpServletRequest request) {
        try {
            logger.info("Checking if user authenticate: {}", login.getUserName());
            User user = userRepository.findByUserName(login.getUserName()).orElse(null);
            if (user == null) {
                return -1;
            } else {
                if (BCrypt.checkpw(login.getPassword(), user.getPassword())) {
                    if ("true".equals(user.getVerified())) {
                        return 1;
                    } else {
                        UserDTO userDTO = new UserDTO();
                        userDTO.setEmail(user.getEmail());
                        emailValidationService.processEmailValidation(request, userDTO);
                        return 0;
                    }
                } else {
                    return 2;
                }
            }
        } catch (Exception e) {
            logger.error("Failed to authenticate user", e);
            throw new RuntimeException("Failed to authenticate user", e);
        }
    }

    public User getProfileByUserName (String userName) {
        logger.info("Getting user profile by user name:::"+userName);
        try{
            User user = userRepository.findByUserName(userName).orElseThrow(() -> new UserNotFoundException(userName));
            return user;
        }catch (Exception e){
            logger.error("Failed to get user profile", e);
            throw new RuntimeException("Failed to get user profile", e);
        }
    }
    @Transactional(rollbackOn = Exception.class)
    public User updateUserInfoByUserName(UserDTO userDTO, String userName){
        try{
            logger.info("Updating info of user:::"+userName);
            User user = userRepository.findByUserName(userName).orElse(null);
            if(user == null){
                return null;
            }
            else{
                if(!ValidString.UserNameEmpty(userDTO.getFirstName())){
                    user.setFirstName(userDTO.getFirstName());
                }if(!ValidString.UserNameEmpty(userDTO.getLastName())){
                    user.setFirstName(userDTO.getLastName());
                }
                if(!ValidString.UserNameEmpty(userDTO.getMiddleName())){
                    user.setFirstName(userDTO.getMiddleName());
                }
                if(!ValidString.PhoneNumberEmpty(userDTO.getPhone())){
                    user.setPhone(userDTO.getPhone());
                }
            }
            return userRepository.save(user);
        }catch (RuntimeException e){
            logger.error("Failed to update user info", e);
            throw new RuntimeException("Failed to update user info", e);
        }
    }
    @Transactional(rollbackOn = Exception.class)
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
            logger.error("Failed to update password", e);
            throw new RuntimeException("Failed to update password", e);
        }
        return res;
    }
    @Transactional(rollbackOn = Exception.class)
    public boolean updateToken(String token, String email){
        try{
            logger.info("Updating token:::"+token);
            User user =  userRepository.findByEmail(email).orElseThrow(() -> new UserNotFoundException(email));
            user.setToken(token);
            userRepository.save(user);
            return true;
        }catch (RuntimeException e){
            logger.error("Failed to update token", e);
            throw new RuntimeException("Failed to update token", e);
        }
    }
    @Transactional(rollbackOn = Exception.class)
    public boolean verifyUser(User user){
        try{
            logger.info("Verifying user::" +user.getUserName());
            user.setVerified("true");
            user.setToken(null);
            userRepository.save(user);
            logger.info("Saved user successfully:::");
            return true;
        }catch (RuntimeException e){
            logger.error("Failed to verify user", e);
        }
        return false;
    }

    public User getByToken(String token){
        try{
            logger.info("Getting user by token:::"+token);
            User user = userRepository.findByToken(token).orElse(null);
            return user;
        }catch (UserNotFoundException e){
            logger.error("Failed to get user by token", e);
            throw new RuntimeException("Failed to get user by token", e);
        }
    }

    @Transactional(rollbackOn = Exception.class)
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
            logger.error("Failed to reset password", e);
            throw new RuntimeException("Failed to reset password", e);
        }
    }
    @Transactional(rollbackOn = Exception.class)
    public boolean RemoveToken(User user){
        try{
            logger.info("Removing token of user:::"+user.getUserName());
            user.setToken(null);
            userRepository.save(user);
            logger.info("Removed token successfully:::");
            return true;
        }catch (RuntimeException e){
            logger.error("Failed to remove token", e);
        }
        return false;
    }
}
