package com.example.demo.Service;

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
public class PostCommentsViewService {
    private static final Logger logger = LoggerFactory.getLogger(PostCommentsViewService.class);
    @Autowired
    private PostsCommentsViewRepository postsCommentsViewRepository;

    @Async("MultiExecutor")
    @Transactional
    public boolean updatePostComments(String postId){
        try{
            logger.info("updatePostComments:::postId:::"+postId);
            postsCommentsViewRepository.findById(postId).map(postsCommentsView->{
                postsCommentsView.setComments(postsCommentsView.getComments()+1);
                postsCommentsView.setLastCommentAt(Instant.now());
                return postsCommentsViewRepository.save(postsCommentsView);
            }).orElseThrow(()-> new PostNotFoundException(postId));
            Thread.sleep(1000);
            return true;
        } catch (InterruptedException | RuntimeException e) {
            logger.error(e.getMessage(),e);
        }
        return false;
    }
    @Async("MultiExecutor")
    @Transactional
    public void updatePostViews(String postId){
        try{
            logger.info("updating views of post:::" + postId);
            postsCommentsViewRepository.findById(postId).map(postViews->{
                postViews.setViews(postViews.getViews()+1);
                return postsCommentsViewRepository.save(postViews);
            }).orElseThrow(()-> new PostNotFoundException(postId));
            Thread.sleep(1000);
        }catch (RuntimeException | InterruptedException e){
            logger.error(e.getMessage(),e);
        }
    }
}
