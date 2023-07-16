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
import java.util.List;
import java.util.Map;

@Service
public class PostCommentsViewService {
    private static final Logger logger = LoggerFactory.getLogger(PostCommentsViewService.class);
    @Autowired
    private PostsCommentsViewRepository postsCommentsViewRepository;

    @Async("MultiExecutor")
    @Transactional
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
            logger.error(e.getMessage(),e);
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
            logger.error(e.getMessage(),e);
        }
    }
    public List<PostsCommentsView> findBySubCategoryId(Integer sub_categoryId){
        try{
            logger.info("findBySubCategoryId:::sub_categoryId:::"+sub_categoryId);
            List<PostsCommentsView> list = postsCommentsViewRepository.findBySubCategoryId(sub_categoryId);
            return list;
        }catch (Exception e){
            logger.error(e.getMessage(),e);
        }
        return null;
    }
    public List<Map<String,Object>> combineByTextRender (Integer sub_categoryId){

        return postsCommentsViewRepository.combineByTextRender(sub_categoryId);
    }

}
