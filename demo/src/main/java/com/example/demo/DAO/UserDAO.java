package com.example.demo.DAO;

import com.example.demo.DTO.UserDTO;
import com.example.demo.Entity.User;
import com.example.demo.Exception.UserNotFoundException;
import com.example.demo.Repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.mindrot.jbcrypt.BCrypt;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.transaction.Transactional;
import java.time.Instant;

@Component
public class UserDAO {
    private static final Logger logger = LoggerFactory.getLogger(UserDAO.class);

    @Autowired
    private UserRepository userRepository;
    @PersistenceContext
    private EntityManager entityManager;
    public void saveUser(User user)
    {
        logger.info("Saving user into database:::"+user.getUserName());
        userRepository.save(user);
    }
    @Transactional
    public int checkIfUserExists(String userName)
    {
        logger.info("Checking if user exists:::"+userName);
        int result = 0;
        try {
            Query query = this.entityManager.createQuery("SELECT COUNT(u) FROM User u WHERE u.userName = ?1");
            query.setParameter(1, userName);
            query.setMaxResults(1);
            Long resultInLong = (Long) query.getSingleResult();
            result = Math.toIntExact(resultInLong);
        } catch (Exception e) {
            e.printStackTrace();
            result = 0;
        }

        return result;
    }
    @Transactional
    public int checkIfEmailExists(String email){
        logger.info("Checking if email exists:::"+email);
        int result = 0;
        try {
            Query query = this.entityManager.createQuery("SELECT COUNT(u) FROM User u WHERE u.email = ?1");
            query.setParameter(1, email);
            query.setMaxResults(1);
            Long resultInLong = (Long) query.getSingleResult();
            result = Math.toIntExact(resultInLong);
        } catch (Exception e) {
            e.printStackTrace();
            result = 0;
        }

        return result;
    }
    @Transactional
    public int checkIfUserExistsByUsernameAndEmail(String userName,String email){
        logger.info("Checking if user exists:::"+userName +"; "+email);
        int resEmail = 0;
        int resUserName=0;
        try {
            Query query = this.entityManager.createQuery("SELECT COUNT(u) FROM User u WHERE u.email = ?1");
            Query query1 = this.entityManager.createQuery("SELECT COUNT(u) FROM User u WHERE u.userName = ?1");
            query.setParameter(1, email);
            query1.setParameter(1,userName);
            query.setMaxResults(1);
            query1.setMaxResults(1);
            Long resultInLong = (Long) query.getSingleResult();
            Long resultInLong1=(Long)query.getSingleResult();
            resEmail = Math.toIntExact(resultInLong);
            resUserName= Math.toIntExact(resultInLong1);
        } catch (Exception e) {
            e.printStackTrace();
            resEmail = 0;
            resUserName=0;
        }

        System.out.println(resEmail+resUserName);
        return resEmail+resUserName;
//        return userRepository.existsByUserNameAndEmail(userName,email);
    }

    public User getProfileByUserName(String userName) {
        logger.info("Getting user from username:::" + userName);
        return userRepository.findByUserName(userName).orElseThrow(()->new UserNotFoundException(userName));
    }
//    public User getUserProfileById(Long id){
//        logger.info("Getting user from id:: "+id);
//        return userRepository.findById(id).orElseThrow(()->new UserNotFoundException(id));
//    }
    public User updateUserByUserName(UserDTO userDTO, String userName){
        logger.info("Updating info of:: "+userName);
        return userRepository.findByUserName(userName).map(user->{
            user.setFirstName(userDTO.getFirstName());
            user.setLastName(userDTO.getLastName());
            return userRepository.save(user);
        }).orElseThrow(()-> new UserNotFoundException(userName));
    }
    public User updatePasswordByUserName(UserDTO userDTO, String userName){
        logger.info("Updating password of:: "+userName);
        String newPassword = BCrypt.hashpw(userDTO.getNewPassword(),BCrypt.gensalt());
        return userRepository.findByUserName(userName).map(user->{
            user.setPassword(newPassword);
            return userRepository.save(user);
        }).orElseThrow(()-> new UserNotFoundException(userName));
    }
}
