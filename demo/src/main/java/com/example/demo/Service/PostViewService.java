package com.example.demo.Service;

import com.example.demo.Exception.PostNotFoundException;
import com.example.demo.Repository.PostViewsRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@Service
public class PostViewService {
    private static final Logger logger = LoggerFactory.getLogger(PostViewService.class);
    @Autowired
    private PostViewsRepository postViewsRepository;

    @Async("MultiExecutor")
    @Transactional
    public void updatePostViews(String postId){
        try{
            logger.info("updating views of post:::" + postId);
            postViewsRepository.findById(postId).map(postViews->{
            postViews.setViews(postViews.getViews()+1);
            return postViewsRepository.save(postViews);
        }).orElseThrow(()-> new PostNotFoundException(postId));
            Thread.sleep(1000);
        }catch (RuntimeException | InterruptedException e){
            logger.error(e.getMessage(),e);
        }
    }
}
