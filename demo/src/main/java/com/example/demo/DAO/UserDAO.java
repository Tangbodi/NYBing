package com.example.demo.DAO;

import com.example.demo.DTO.UserDTO;
import com.example.demo.Entity.User;
import com.example.demo.Exception.NotFoundException;
import com.example.demo.Repository.UserRepository;
import org.mindrot.jbcrypt.BCrypt;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
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
    public int checkIfUserExistsByUsernameAndEmail(String userName,String email){
        logger.info("Checking if user exists");
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
            logger.error(e.toString());
            resEmail = 0;
            resUserName=0;
        }

        return resEmail+resUserName;
//        return userRepository.existsByUserNameAndEmail(userName,email);
    }

    public User getProfileByUserName(String userName) {
        logger.info("Getting user from username:: " + userName);
        return userRepository.findByUserName(userName).orElseThrow(()->new NotFoundException(userName));
    }
//    public User getUserProfileById(Long id){
//        logger.info("Getting user from id:: "+id);
//        return userRepository.findById(id).orElseThrow(()->new UserNotFoundException(id));
//    }
    public User updateUserByUserName(UserDTO userDTO, String userName){
        return userRepository.findByUserName(userName).map(user->{
            user.setFirstName(userDTO.getFirstName());
            user.setLastName(userDTO.getLastName());
            user.setModifyTime(Instant.now());
            return userRepository.save(user);
        }).orElseThrow(()-> new NotFoundException(userName));
    }
    public User updatePasswordByUserName(UserDTO userDTO, String userName){
        String newPassword = BCrypt.hashpw(userDTO.getNewPassword(),BCrypt.gensalt());
        return userRepository.findByUserName(userName).map(user->{
            user.setPassword(newPassword);
            user.setModifyTime(Instant.now());
            return userRepository.save(user);
        }).orElseThrow(()-> new NotFoundException(userName));
    }
    public User findByToken(String token){
        return userRepository.findByToken(token);
    }
    public void deleteUser(String userName){
        userRepository.deleteByUserName(userName);
    }
}
