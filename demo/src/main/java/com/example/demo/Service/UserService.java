package com.example.demo.Service;

import com.example.demo.DAO.UserDAO;
import com.example.demo.DAO.UserPasswordHistoryDAO;
import com.example.demo.DTO.LoginDTO;
import com.example.demo.DTO.UserDTO;
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
    @Autowired
    private UserPasswordHistoryDAO userPasswordHistoryDAO;
    @Autowired
    private UserPasswordHistoryService userPasswordHistoryService;
    public boolean registerUser(User newUser) throws IllegalStateException {
        logger.info("Registering user::");
        String password = BCrypt.hashpw( newUser.getPassword(), BCrypt.gensalt());
        newUser.setPassword(password);
        newUser.setRegisteredAt(Instant.now());
        this.userDAO.saveUser(newUser);
//        userPasswordHistoryService.saveUserPasswordHistory(newUser);
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
//    public User getUserProfileById(Long id){
//        return this.userDAO.getUserProfileById(id);
//    }

    public User updateUserByUserName(UserDTO userDTO, String userName){
        return this.userDAO.updateUserByUserName(userDTO,userName);
    }
    public Boolean updatePasswordByUserName(UserDTO userDTO, String userName){

        String curPassword = userDAO.getProfileByUserName(userName).getPassword();
        Boolean pwdCheck = false;

        if (BCrypt.checkpw(userDTO.getInputPassword(), curPassword)) {
            pwdCheck = true;
            logger.info("The password entered is same as current password");
            this.userDAO.updatePasswordByUserName(userDTO,userName);
        }
        else{
            logger.info("The password entered is not the same as current password");
        }
        return pwdCheck;
    }
    @Transactional
    public void updateToken(String token, String email){
        User user = userRepository.findByEmail(email);
        if(user != null){
            user.setToken(token);
            this.userDAO.saveUser(user);
        }
        else{
            throw new UserNotFoundException(email);
        }
    }
    public User getByToken(String token){
        return userDAO.findByToken(token);
    }

    @Transactional
    public void updatePassword(User user, String newPassword){
//        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
//        String encodePassword = passwordEncoder.encode(newPassword);
//        user.setPassword(encodePassword);
        String password = BCrypt.hashpw(newPassword,BCrypt.gensalt());
        user.setPassword(password);
        user.setToken(null);
        this.userDAO.saveUser(user);
    }
    @Transactional
    public void deleteUser(String userName){
        userRepository.deleteByUserName(userName);
    }
}
