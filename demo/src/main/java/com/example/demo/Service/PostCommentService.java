package com.example.demo.Service;

import com.example.demo.Entity.PostsCommentsView;
import com.example.demo.Exception.PostNotFoundException;
import com.example.demo.Repository.PostsCommentsViewRepository;
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
    private PostsCommentsViewRepository postsCommentsViewRepository;

    @Async("MultiExecutor")
    @Transactional
    public PostsCommentsView updatePostComments(String postId){
        try{
            postsCommentsViewRepository.findById(postId).map(postsCommentsView->{
                postsCommentsView.setComments(postsCommentsView.getComments()+1);
                postsCommentsView.setLastCommentAt(Instant.now());
                return postsCommentsViewRepository.save(postsCommentsView);
            }).orElseThrow(()-> new PostNotFoundException(postId));
            Thread.sleep(1000);
        } catch (InterruptedException | RuntimeException e) {
            logger.error(e.getMessage(),e);
            //try{
            //Integer i = Integer.valueOf(s);
        //}
            //logger.error("Failed to format {}", s, e);
        }
        return null;
    }

}
