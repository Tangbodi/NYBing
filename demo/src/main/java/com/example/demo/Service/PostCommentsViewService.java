package com.example.demo.Service;

import com.example.demo.DTO.PostDTO;
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
import java.util.List;
import java.util.Map;

@Service
public class PostCommentsViewService {
    private static final Logger logger = LoggerFactory.getLogger(PostCommentsViewService.class);
    @Autowired
    private PostsCommentsViewRepository postsCommentsViewRepository;

    @Async("MultiExecutor")
    @Transactional(rollbackOn = Exception.class)
    public PostsCommentsView updatePostComments(String postId){
        try{
            logger.info("updatePostComments:::postId:::"+postId);
            postsCommentsViewRepository.findById(postId).map(postsCommentsView->{
                postsCommentsView.setComments(postsCommentsView.getComments()+1);
                postsCommentsView.setLastCommentAt(Instant.now());
                return postsCommentsViewRepository.save(postsCommentsView);
            }).orElseThrow(()-> new PostNotFoundException(postId));
            Thread.sleep(1000);
        } catch (InterruptedException | RuntimeException e) {
            logger.error("Error while updating comments:::"+e.getMessage(),e);
        }
        return null;
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
            logger.error("Error while updating views:::"+e.getMessage(),e);
        }
    }
    @Transactional
    public List<Map<String,Object>> combineByTextRender (Integer sub_categoryId){
        try{
            logger.info("combineByTextRender:::sub_categoryId:::"+sub_categoryId);
            return postsCommentsViewRepository.combineByTextRender(sub_categoryId);
        }catch (RuntimeException e){
            logger.error("Error while combining text and render:::"+e.getMessage(),e);
            throw new RuntimeException(e.getMessage());
        }
    }
    @Transactional(rollbackOn = Exception.class)
    public PostsCommentsView savePostCommentsView(String uuId, PostDTO postDTO, Instant time){
        try{
            logger.info("Setting PostView:::"+uuId);
            PostsCommentsView postCommentsView = new PostsCommentsView();
            logger.info("Setting PostsCommentsView uuid:::");
            postCommentsView.setId(uuId.toString());
            logger.info("Setting PostsCommentsView subCategory:::");
            postCommentsView.setSubCategoryId(postDTO.getSubCategoryId());
            logger.info("Setting PostsCommentsView views:::");
            postCommentsView.setViews(0);
            logger.info("Setting PostsCommentsView lastCommentAt:::");
            postCommentsView.setLastCommentAt(time);
            logger.info("Setting PostsCommentsView title:::");
            postCommentsView.setTitle(postDTO.getTitle());
            logger.info("Setting PostsCommentsView username:::");
            postCommentsView.setUserName(postDTO.getUserName());
            logger.info("Setting PostsCommentsView comments:::");
            postCommentsView.setComments(0);
            logger.info("Saving PostsCommentsView:::");
            return postsCommentsViewRepository.save(postCommentsView);
        }catch (RuntimeException e){
            logger.error("Error while saving postCommentsView:::"+e.getMessage(),e);
        }
        return null;
    }

}
