package com.example.demo.Service;

import com.example.demo.Entity.PostViewsComment;
import com.example.demo.Exception.PostNotFoundException;
import com.example.demo.Repository.PostViewsCommentRepository;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@Service
@Async
public class PostViewsCommentService {
    private static final Logger logger = LoggerFactory.getLogger(PostViewsCommentService.class);
    @Autowired
    private PostViewsCommentRepository postViewsCommentRepository;

    @Async("MultiExecutor")
    @Transactional
    public void updatePostViews(String postId){
        logger.info("updating views data of post:::" + postId);
        try{
            postViewsCommentRepository.findById(postId).map(postViewsComment->{
            postViewsComment.setViews(postViewsComment.getViews()+1);
            return postViewsCommentRepository.save(postViewsComment);
        }).orElseThrow(()-> new PostNotFoundException(postId));
            Thread.sleep(1000);
        }catch (InterruptedException e){
            e.getMessage();
        }
    }
}
