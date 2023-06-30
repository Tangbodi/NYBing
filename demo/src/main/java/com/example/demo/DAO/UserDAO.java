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
            logger.error(e.getMessage(),e);
            resEmail = 0;
            resUserName=0;
        }

        System.out.println(resEmail+resUserName);
        return resEmail+resUserName;
//        return userRepository.existsByUserNameAndEmail(userName,email);
    }

}
