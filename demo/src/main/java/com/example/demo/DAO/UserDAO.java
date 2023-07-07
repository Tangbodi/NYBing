package com.example.demo.DAO;

import com.example.demo.Repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.transaction.Transactional;

@Component
public class UserDAO {
    private static final Logger logger = LoggerFactory.getLogger(UserDAO.class);

    @Autowired
    private UserRepository userRepository;
    @PersistenceContext
    private EntityManager entityManager;

    @Transactional
    public int checkIfUserExistsByUsernameAndEmail(String userName,String email){
        logger.info("Checking if user exists:::"+userName +"; "+email);
        int resEmail = 0;
        int resUserName=0;
        try {

            Query userQuery = this.entityManager.createQuery("SELECT COUNT(u) FROM User u WHERE u.userName = ?1");
            Query emailQuery = this.entityManager.createQuery("SELECT COUNT(u) FROM User u WHERE u.email = ?1");
            emailQuery.setParameter(1, email);
            userQuery.setParameter(1,userName);
            emailQuery.setMaxResults(1);
            userQuery.setMaxResults(1);
            Long emailResult = (Long) emailQuery.getSingleResult();
            Long userResult=(Long)userQuery.getSingleResult();
            resEmail = Math.toIntExact(emailResult);
            resUserName= Math.toIntExact(userResult);
        } catch (Exception e) {
            logger.error(e.getMessage(),e);
        }

        System.out.println(resEmail+resUserName);
        return resEmail+resUserName;
//        return userRepository.existsByUserNameAndEmail(userName,email);
    }

}
