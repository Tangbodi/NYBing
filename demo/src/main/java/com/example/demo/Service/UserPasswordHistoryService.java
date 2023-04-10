package com.example.demo.Service;

import com.example.demo.DAO.UserPasswordHistoryDAO;
import com.example.demo.Entity.User;
import com.example.demo.Entity.UserPasswordHistory;
import com.example.demo.Repository.UserPasswordHistoryRepository;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.slf4j.Logger;

import java.time.Instant;

@Service
public class UserPasswordHistoryService {
    private static final Logger logger = LoggerFactory.getLogger(UserPasswordHistoryService.class);

    @Autowired
    private UserPasswordHistoryRepository userPasswordHistoryRepository;
    @Autowired
    private UserPasswordHistoryDAO userPasswordHistoryDAO;
    public UserPasswordHistory saveUserPasswordHistory(User user){
        logger.info("updating user password history ::" + user.getUserName());
        UserPasswordHistory userPasswordHistory = new UserPasswordHistory();
        userPasswordHistory.setUser(user);
        userPasswordHistory.setPasswordHash(user.getPassword());
        userPasswordHistory.setStartDate(Instant.now());
        return userPasswordHistoryDAO.saveUserPasswordHistory(userPasswordHistory);
    }
}
