package com.example.demo.Service;

import com.example.demo.DAO.PostDAO;
import com.example.demo.DTO.PostDTO;
import com.example.demo.Entity.Post;
import com.example.demo.Entity.User;
import com.example.demo.Exception.NotFoundException;
import com.example.demo.Exception.PostNotFoundException;
import com.example.demo.Repository.PostRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
public class PostService {
    private static final Logger logger = LoggerFactory.getLogger(PostService.class);

    @Autowired
    private PostRepository postRepository;
    @Autowired
    private PostDAO postDAO;

    public List<Map<String, Object>> findPostsByCategoryId(Integer categoryId){
        try{
            return postRepository.findPostsByCategoryId(categoryId);
        }
        catch (Exception e){
            logger.error(e.toString());
        }
        return null;
    }
    public Post findPostByPostId(String postId){
        return postRepository.findById(postId).orElseThrow(() -> new PostNotFoundException(postId));
    }
    public Post savePost(PostDTO postDTO, User user){
        logger.info("saving post into database::");
        Post post  = new Post();
        post.setCategoryId(postDTO.getCategoryId());
        post.setTitle(postDTO.getTitle());
        post.setTextrender(postDTO.getTextrender());
        post.setUserName(postDTO.getUserName());
        post.setIpvFour(postDTO.getIpvFour());
        post.setIpvSix(postDTO.getIpvSix());
        post.setPublishAt(Instant.now());
        post.setUser(user);
        return postRepository.save(post);
    }

}
