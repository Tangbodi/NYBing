package com.example.demo.DAO;

import com.example.demo.Entity.User;
import com.example.demo.Entity.UserPasswordHistory;
import com.example.demo.Exception.NotFoundException;
import com.example.demo.Repository.UserPasswordHistoryRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.Instant;

@Component
public class UserPasswordHistoryDAO {
    private static final Logger logger = LoggerFactory.getLogger(UserPasswordHistoryDAO.class);
    @Autowired
    private UserPasswordHistoryRepository userPasswordHistoryRepository;

    public UserPasswordHistory updateUserPasswordHistoryById(User user, String newPassword){
        Long id = user.getId();
        return userPasswordHistoryRepository.findById(id).map(userPasswordHistory->{
            userPasswordHistory.setPasswordHash(newPassword);
            userPasswordHistory.setStartDate(Instant.now());
            return userPasswordHistoryRepository.save(userPasswordHistory);
        }).orElseThrow(()-> new NotFoundException(id));
    }
    public UserPasswordHistory saveUserPasswordHistory(UserPasswordHistory userPasswordHistory){
        return userPasswordHistoryRepository.save(userPasswordHistory);
    }
}
