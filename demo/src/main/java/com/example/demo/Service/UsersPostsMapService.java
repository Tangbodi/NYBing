package com.example.demo.Service;

import com.example.demo.Entity.Post;
import com.example.demo.Entity.User;
import com.example.demo.Entity.UsersPostsMap;
import com.example.demo.Repository.UsersPostsMapRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@Service
public class UsersPostsMapService {
    private static final Logger logger = LoggerFactory.getLogger(UsersPostsMapService.class);
    @Autowired
    private UsersPostsMapRepository usersPostsMapRepository;
    @Transactional(rollbackOn = Exception.class)
    public boolean saveUsersPostsMap(User user, Post post){
        try{
            logger.info("save UsersPostsMap:::");
            UsersPostsMap usersPostsMap = new UsersPostsMap();
            usersPostsMap.setId(user.getId());
            usersPostsMap.setPostId(post.getId().getPostId());
            usersPostsMapRepository.save(usersPostsMap);
            return true;
        }catch (Exception e){
            logger.error("save UsersPostsMap:::Exception:::"+e);
        }
        return false;
    }
}
