package com.example.demo.Service;

import com.example.demo.DAO.UserDAO;
import com.example.demo.DTO.LoginDTO;
import com.example.demo.Entity.User;
import com.example.demo.Exception.UserNotFoundException;
import com.example.demo.Repository.UserRepository;
import org.mindrot.jbcrypt.BCrypt;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.Instant;

@Service

public class UserService {
    private static final Logger logger = LoggerFactory.getLogger(UserService.class);
    @Autowired
    private UserDAO userDAO;
    @Autowired
    private UserRepository userRepository;

    public boolean registerUser(User newUser) throws IllegalStateException {
        logger.info("Registering user::");
        newUser.setPassword(BCrypt.hashpw( newUser.getPassword(), BCrypt.gensalt()));
        newUser.setRegisteredAt(Instant.now());
        this.userDAO.saveUser(newUser);
        return true;
    }
    public boolean checkIfUserRegistered (User newUser)
    {
        logger.info("Checking if user exists ::" + newUser.getUserName());
        int result = this.userDAO.checkIfUserExists(newUser.getUserName());

        if (result >0) {

            return true;
        }
        return false;
    }
    public Boolean authenticate(LoginDTO login)
    {
        logger.info("Checking if user exists on the basis of username::" + login.getUserName());

        User user = null;
        try {
            user = this.userDAO.getProfileByUserName(login.getUserName());
        } catch (Exception e) {
            logger.error(e.toString());
            user = null;
        }

        if (user == null) {
            return false;
        } else {
            Boolean pwdCheck = false;

            if (BCrypt.checkpw(login.getPassword(), user.getPassword())) {
                pwdCheck = true;
            }
            return pwdCheck;
        }

    }
    public User getProfileByUserName (String userName)
    {
        return this.userDAO.getProfileByUserName(userName);
    }
    public User getUserProfileById(Long id){
        return this.userDAO.getUserProfileById(id);
    }
    public User updateUserById(User newUser, Long id){
        newUser.setPassword(BCrypt.hashpw( newUser.getPassword(), BCrypt.gensalt()));
        return this.userDAO.updateUserById(newUser,id);
    }
    @Transactional
    public void updateResetPasswordToken(String token, String email){
        User user = userRepository.findByEmail(email);
        if(user != null){
            user.setResetPasswordToken(token);
            this.userDAO.saveUser(user);
        }
        else{
            throw new UserNotFoundException(email);
        }
    }
    public User getByResetPasswordToken(String token){
        return userDAO.findByResetPasswordToken(token);
    }

    @Transactional
    public void updatePassword(User user, String newPassword){
//        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
//        String encodePassword = passwordEncoder.encode(newPassword);
//        user.setPassword(encodePassword);
        user.setPassword(BCrypt.hashpw(newPassword,BCrypt.gensalt()));
        user.setResetPasswordToken(null);
        this.userDAO.saveUser(user);
    }

}
