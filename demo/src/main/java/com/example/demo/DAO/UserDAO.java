package com.example.demo.DAO;

import com.example.demo.DTO.UserDTO;
import com.example.demo.Entity.User;
import com.example.demo.Exception.UserNotFoundException;
import com.example.demo.Repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import java.time.Instant;

@Component
public class UserDAO {
    private static final Logger logger = LoggerFactory.getLogger(UserDAO.class);

    @Autowired
    private UserRepository userRepository;
    @PersistenceContext
    private EntityManager entityManager;
    public User saveUser(User user)
    {
        logger.info("Saving user object to database::");
        return this.userRepository.save(user);
    }

    public int checkIfUserExists(String userName)
    {
        logger.info("Checking if user exists::"+userName);
        int result = 0;
        try {
            Query query = this.entityManager.createQuery("SELECT COUNT(u) FROM User u WHERE u.userName = ?1");
            query.setParameter(1, userName);
            query.setMaxResults(1);
            Long resultInLong = (Long) query.getSingleResult();
            result = Math.toIntExact(resultInLong);
        } catch (Exception e) {
            logger.error(e.toString());
            result = 0;
        }

        return result;
    }
    public int checkIfEmailExists(String email){
        logger.info("Checking if email exists::"+email);
        int result = 0;
        try {
            Query query = this.entityManager.createQuery("SELECT COUNT(u) FROM User u WHERE u.email = ?1");
            query.setParameter(1, email);
            query.setMaxResults(1);
            Long resultInLong = (Long) query.getSingleResult();
            result = Math.toIntExact(resultInLong);
        } catch (Exception e) {
            logger.error(e.toString());
            result = 0;
        }

        return result;
    }
    public User getProfileByUserName(String userName) {
        logger.info("Getting user from username:: " + userName);
        TypedQuery<User> query = this.entityManager.createQuery("SELECT u FROM User u WHERE u.userName = ?1", User.class);
        query.setParameter(1, userName);
        query.setMaxResults(1);
        return query.getSingleResult();
    }
    public User getUserProfileById(Long id){
        logger.info("Getting user from id:: "+id);
        return userRepository.findById(id).orElseThrow(()->new UserNotFoundException(id));
    }
    public User updateUserById(UserDTO userDTO, Long id){
        return userRepository.findById(id).map(user->{
//            user.setEmail(userDTO.getEmail());
//            user.setPhone(userDTO.getPhone());
//            user.setMiddleName(userDTO.getMiddleName());
            user.setFirstName(userDTO.getFirstName());
            user.setLastName(userDTO.getLastName());
//            user.setPassword(userDTO.getPassword());
            user.setModifyTime(Instant.now());
            return userRepository.save(user);
        }).orElseThrow(()-> new UserNotFoundException(id));
    }
    public User findByResetPasswordToken(String token){
        return userRepository.findByResetPasswordToken(token);
    }
}
