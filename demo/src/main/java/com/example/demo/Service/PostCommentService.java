package com.example.demo.Service;

import com.example.demo.Entity.PostComment;
import com.example.demo.Exception.PostNotFoundException;
import com.example.demo.Repository.PostCommentsRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.Instant;

@Service
@Async
public class PostCommentService {
    private static final Logger logger = LoggerFactory.getLogger(PostViewService.class);
    @Autowired
    private PostCommentsRepository postCommentsRepository;

    @Async("MultiExecutor")
    @Transactional
    public void updatePostComments(String postId){
        try{
            postCommentsRepository.findById(postId).map(postComments->{
                postComments.setComments(postComments.getComments()+1);
                postComments.setLastComment(Instant.now());
                return postCommentsRepository.save(postComments);
            }).orElseThrow(()-> new PostNotFoundException(postId));
            Thread.sleep(1000);
        } catch (InterruptedException | RuntimeException e) {
            logger.error(e.getMessage(),e);
            //try{
            //Integer i = Integer.valueOf(s);
        //}
            //logger.error("Failed to format {}", s, e);
        }
    }

}
