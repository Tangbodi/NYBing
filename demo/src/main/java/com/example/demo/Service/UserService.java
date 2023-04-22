package com.example.demo.Service;

import com.example.demo.DAO.UserDAO;
import com.example.demo.DTO.LoginDTO;
import com.example.demo.DTO.UserDTO;
import com.example.demo.Entity.User;
import com.example.demo.Exception.AuthException;
import com.example.demo.Exception.NotFoundException;
import com.example.demo.Repository.UserRepository;
import org.mindrot.jbcrypt.BCrypt;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import javax.transaction.Transactional;
import java.time.Instant;
import java.util.UUID;

@Service
public class UserService {
    private static final Logger logger = LoggerFactory.getLogger(UserService.class);
    @Autowired
    private UserDAO userDAO;
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private EmailValidationService emailValidationService;
    public boolean registerUser(User newUser) throws IllegalStateException {
        logger.info("Registering user::");
        String password = BCrypt.hashpw( newUser.getPassword(), BCrypt.gensalt());

        newUser.setPassword(password);
        newUser.setRegisteredAt(Instant.now());
        this.userDAO.saveUser(newUser);
        return true;
    }
    public boolean checkIfUserRegistered (User newUser)
    {
        logger.info("Checking if user exists ::" + newUser.getUserName());
        return userDAO.checkIfUserExistsByUsernameAndEmail(newUser.getUserName(),newUser.getEmail()) >0? true: false;

    }
    public User authenticate(LoginDTO login, HttpServletRequest request)
    {
        logger.info("Checking if user authenticate::");
        User user = null;
        try {
            user = this.userDAO.getProfileByUserName(login.getUserName());
        } catch (Exception e) {
            e.getMessage();
            user = null;
        }
        if (user == null) {
            throw new NotFoundException();
        } else {
//            Boolean pwdCheck = false;
            if (BCrypt.checkpw(login.getPassword(), user.getPassword())) {
                //cannot use ==
//                if(user.getVerified().equals("true")){
                    return user;
////                    pwdCheck = true;
//                }
//                else{
//                    //api/user/login/email_validation?token=
//                    emailValidationService.processEmailValidation(request,user);
//                }
            }
            else{
                throw new AuthException(login.getUserName(),login.getPassword());
            }
        }
//        return null;
    }
    public User getProfileByUserName (String userName)
    {
        return this.userDAO.getProfileByUserName(userName);
    }
//    public User getUserProfileById(Long id){
//        return this.userDAO.getUserProfileById(id);
//    }

    public User updateUserInfoByUserName(UserDTO userDTO, String userName){
        logger.info("updating info of:: "+userName);
        return this.userDAO.updateUserByUserName(userDTO,userName);
    }
    public User updatePasswordByUserName(UserDTO userDTO, String userName){
        logger.info("updating password of:: "+userName);
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
        try{
            User user = userRepository.findByEmail(email);
            user.setToken(token);
            this.userDAO.saveUser(user);
        }catch (NotFoundException e){
            e.getMessage();
        }
    }
    @Transactional
    public void verifyUser(User user){
        user.setVerified("true");
        user.setToken(null);
    }
    public User getByToken(String token){
        return userDAO.findByToken(token);
    }

    @Transactional
    public void ResetPassword(User user, String newPassword){
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
