package com.example.demo.Service;

import com.example.demo.DAO.PostDAO;
import com.example.demo.Repository.PostRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class PostService {
    private static final Logger logger = LoggerFactory.getLogger(PostService.class);

    @Autowired
    private PostRepository postRepository;
    @Autowired
    private PostDAO postDAO;

    public List<Map<String, Object>> findPostsByCategoryId(Long categoryId){
        return postDAO.findPostsByCategoryId(categoryId);
    }
//    public Optional<Post> findPostByPostId(Long postId){
//        return postDAO.findPostByPostId(postId);
//    }
}
