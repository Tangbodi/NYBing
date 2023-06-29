package com.example.demo.Controller;

import com.example.demo.Entity.Post;
import com.example.demo.Service.PostService;
import com.example.demo.Util.ApiResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin(origins = "http://192.168.1.10:3000/")
public class SearchController {
    private static final Logger logger = LoggerFactory.getLogger(SearchController.class);
    @Autowired
    private PostService postService;
    @PostMapping("/search/{keyword}")
    public ResponseEntity<ApiResponse<List<Post>>> findAllPostByKeyword(@PathVariable String keyword){
        if (keyword.trim().isEmpty()) {
            ApiResponse errorResponse = ApiResponse.error(400 , "Keyword cannot be empty or contain only spaces", "Bad Request");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
        }
        keyword = keyword.trim();
        if (!keyword.matches("^[\\p{L}\\p{N}\\s]*$")){
            ApiResponse errorResponse = ApiResponse.error(400 , "Keyword cannot contain special characters or emojis", "Bad Request");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
        }
        try{
            List<Post> postList = postService.findAllPostByKeyword(keyword);
            ApiResponse<List<Post>> apiResponse = ApiResponse.success(postList);
            return ResponseEntity.ok(apiResponse);
        }catch (Exception e){
            ApiResponse<List<Post>> errorResponse = ApiResponse.error(500, "Internal Server Error", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
        }
    }
}
