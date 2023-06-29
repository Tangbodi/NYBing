package com.example.demo.Service;

import com.example.demo.DAO.UserDAO;
import com.example.demo.DTO.LoginDTO;
import com.example.demo.DTO.UserDTO;
import com.example.demo.Entity.User;
import com.example.demo.Exception.AuthException;
import com.example.demo.Exception.UnauthorizedException;
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
import java.util.Optional;

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
    logger.info("Registering user:::"+ newUser.getUserName()+"; "+newUser.getEmail());
    try{
        String password = BCrypt.hashpw( newUser.getPassword(), BCrypt.gensalt());
        newUser.setPassword(password);
        newUser.setRegisteredAt(Instant.now());
        newUser.setVerified("false");
        userDAO.saveUser(newUser);
        Thread.sleep(1000);
        return true;
    } catch (Exception e) {
        e.printStackTrace();
    }
    return false;
}
    public boolean checkIfUserRegistered (User newUser)
    {
        logger.info("Checking if user exists:::" + newUser.getUserName());
        int res = userDAO.checkIfUserExistsByUsernameAndEmail(newUser.getUserName(),newUser.getEmail());
        if(res >0){
            return true;
        }
        return false;
    }
    public Integer authenticate(LoginDTO login, HttpServletRequest request)
    {
        logger.info("Checking if user authenticate:::"+login.getUserName());
        User user = null;
        try {
            user = this.userDAO.getProfileByUserName(login.getUserName());
        } catch (Exception e) {
            logger.error(e.getMessage(),e);
            user = null;
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
        logger.info("Getting user profile by user name:::"+userName);
        return this.userDAO.getProfileByUserName(userName);
    }
    public Optional<User> getUserProfileById(String id){
        logger.info("Getting user profile by id:::"+id);
        return userRepository.findById(id);
    }

    public User updateUserInfoByUserName(UserDTO userDTO, String userName){
        logger.info("Updating info of user:::"+userName);
        return this.userDAO.updateUserByUserName(userDTO,userName);
    }
    public User updatePasswordByUserName(UserDTO userDTO, String userName){
        logger.info("Updating password of user:::"+userName);
        String curPassword = userDAO.getProfileByUserName(userName).getPassword();
        Boolean pwdCheck = false;

        if (BCrypt.checkpw(userDTO.getInputPassword(), curPassword)) {
            pwdCheck = true;
            return this.userDAO.updatePasswordByUserName(userDTO,userName);
        }
        else{
            throw new AuthException(userDTO.getInputPassword());
        }
    }
    @Transactional
    public void updateToken(String token, String email){
        logger.info("Updating token:::"+token);
        try{
            userRepository.findByEmail(email).map(user->{
                user.setToken(token);
                return userRepository.save(user);
            }).orElseThrow(()-> new UserNotFoundException(email));
        }catch (RuntimeException e){
            logger.error(e.getMessage(),e);
        }
    }
    @Transactional
    public void verifyUser(User user){
        logger.info("Verifying user::" +user.getUserName());
        user.setVerified("true");
        user.setToken(null);
        userRepository.save(user);
    }
    @Transactional
    public User getByToken(String token){
        logger.info("Getting user by token:::"+token);
        try{
            User user = userRepository.findByToken(token);
            if(user != null){
                this.verifyUser(user);
            }
            return user;
        }catch (UserNotFoundException e){
            e.getMessage();
        }
        return null;
    }

    @Transactional
    public void ResetPassword(User user, String newPassword){
        logger.info("Resetting password of user:::"+user.getUserName());
//        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
//        String encodePassword = passwordEncoder.encode(newPassword);
//        user.setPassword(encodePassword);
        String password = BCrypt.hashpw(newPassword,BCrypt.gensalt());
        user.setPassword(password);
        user.setToken(null);
        this.userDAO.saveUser(user);
    }

}
