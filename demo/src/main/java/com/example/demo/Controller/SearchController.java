package com.example.demo.Controller;

import com.example.demo.Entity.Post;
import com.example.demo.Service.PostService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@CrossOrigin(origins ="http://192.168.1.13:3000/")
public class SearchController {
    private static final Logger logger = LoggerFactory.getLogger(SearchController.class);
    @Autowired
    private PostService postService;
    @PostMapping("/search/{keyword}")
    public List<Post> findAllPostByKeyword(@PathVariable String keyword){
        if (keyword.trim().isEmpty()) {
            throw new IllegalArgumentException("Keyword cannot be empty or contain only spaces:::");
        }
        keyword = keyword.trim();
        if (!keyword.matches("^[\\p{L}\\p{N}\\s]*$")){
            throw new IllegalArgumentException("Keyword cannot contain special characters or emojis:::");
        }
        return postService.findAllPostByKeyword(keyword);
    }
}
