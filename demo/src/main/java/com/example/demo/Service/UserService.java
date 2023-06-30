package com.example.demo.Service;

import com.example.demo.DAO.UserDAO;
import com.example.demo.DTO.LoginDTO;
import com.example.demo.DTO.UserDTO;
import com.example.demo.DTO.UserPasswordDTO;
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

public Boolean registerUser(User newUser){
    try{
        logger.info("Registering user:::"+ newUser.getUserName()+"; "+newUser.getEmail());
        String password = BCrypt.hashpw( newUser.getPassword(), BCrypt.gensalt());
        newUser.setPassword(password);
        newUser.setRegisteredAt(Instant.now());
        newUser.setVerified("false");
        userRepository.save(newUser);
        Thread.sleep(1000);
        return true;
    } catch (Exception e) {
        logger.error(e.getMessage(),e);
    }
    return false;
}
    public boolean checkIfUserRegistered (User newUser) {
        logger.info("Checking if user exists:::" + newUser.getUserName());
        int res = userDAO.checkIfUserExistsByUsernameAndEmail(newUser.getUserName(),newUser.getEmail());
        if(res >0){
            return true;
        }
        return false;
    }
    public Integer authenticate(LoginDTO login, HttpServletRequest request)
    {
        User user = null;
        try {
            logger.info("Checking if user authenticate:::"+login.getUserName());
            user = userRepository.findByUserName(login.getUserName()).orElseThrow(() -> new UserNotFoundException(login.getUserName()));
        } catch (Exception e) {
            logger.error(e.getMessage(),e);
        }
        if (user == null) {
            return -1;
        } else {
//            Boolean pwdCheck = false;
            if (BCrypt.checkpw(login.getPassword(), user.getPassword())) {
                //cannot use ==
                if(user.getVerified().equals("true")){
                    return 1;
//                    pwdCheck = true;
                }
                else{
                    //api/user/login/email_validation?token=
                    emailValidationService.processEmailValidation(request,user);
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
            User user = userRepository.findByUserName(userName).orElseThrow(() -> new UserNotFoundException(userName));
            boolean allEmpty=false;
            if(!userDTO.getFirstName().isBlank()){
                user.setFirstName(userDTO.getFirstName());
                allEmpty = true;
            }
            if(!userDTO.getLastName().isBlank()){
                user.setLastName(userDTO.getLastName());
                allEmpty = true;
            }
            if(!userDTO.getPhone().isBlank()){
                user.setPhone(userDTO.getPhone());
                allEmpty = true;
            }
            if(allEmpty == false){
                return null;
            }else{
                return userRepository.save(user);
            }
        } catch (RuntimeException e){
            logger.error(e.getMessage(),e);
        }
        return null;
    }
    @Transactional
    public User updatePasswordByUserName(UserPasswordDTO userPasswordDTO, String userName) {
        try {
            logger.info("Updating password of user:::" + userName);
            User user = userRepository.findByUserName(userName).orElseThrow(() -> new UserNotFoundException(userName));
            String curPassword = user.getPassword();
            if (BCrypt.checkpw(userPasswordDTO.getInputPassword(), curPassword)) {
                String newPassword = BCrypt.hashpw(userPasswordDTO.getNewPassword(), BCrypt.gensalt());
                user.setPassword(newPassword);
                return userRepository.save(user);
            } else {
                return null;
            }
        }catch (RuntimeException e) {
            logger.error(e.getMessage(), e);
        }
        return null;
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
            User user = userRepository.findByToken(token);
            if(user != null){
                this.verifyUser(user);
                return user;
            }else{
                return null;
            }
        }catch (UserNotFoundException e){
            e.getMessage();
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
            return true;
        }catch (Exception e){
            logger.error(e.getMessage(),e);
        }
        return false;
    }

}
