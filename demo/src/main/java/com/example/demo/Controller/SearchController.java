package com.example.demo.Controller;

import com.example.demo.Entity.Post;
import com.example.demo.Service.PostService;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin(origins = "http://192.168.1.10:3000/")
public class SearchController {
    private static final Logger logger = LoggerFactory.getLogger(SearchController.class);
    @Autowired
    private PostService postService;
    @GetMapping("/search/{keyword}")
    public String findAllPostByKeyword(@PathVariable String keyword){
        if (keyword.trim().isEmpty()) {
            throw new IllegalArgumentException("Keyword cannot be empty or contain only spaces:::");
        }
        keyword = keyword.trim();
        if (!keyword.matches("^[\\p{L}\\p{N}\\s]*$")){
            throw new IllegalArgumentException("Keyword cannot contain special characters or emojis:::");
        }
        List<Post> postList = postService.findAllPostByKeyword(keyword);
        Gson gson = new GsonBuilder().create();
        String json = gson.toJson(postList);

        return json;
    }
}
