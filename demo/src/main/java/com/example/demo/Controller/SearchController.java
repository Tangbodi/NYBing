package com.example.demo.Controller;

import com.example.demo.DTO.SearchKeywordDTO;
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
//@CrossOrigin(origins = "http://192.168.1.10:3000/")
public class SearchController {
    private static final Logger logger = LoggerFactory.getLogger(SearchController.class);
    @Autowired
    private PostService postService;
    @PostMapping("/search")
    public ResponseEntity<ApiResponse<List<Post>>> findAllPostByKeyword(@RequestBody SearchKeywordDTO keyword) throws Exception {

        if (keyword.getKeyword().trim().isEmpty()) {
            ApiResponse errorResponse = ApiResponse.error(400 , "Keyword Cannot Be Empty Or Contain Only Spaces", "Bad Request");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
        }
        if (!keyword.getKeyword().trim().matches("^[\\p{L}\\p{N}\\s]*$")){
            ApiResponse errorResponse = ApiResponse.error(400 , "Keyword Cannot Contain Special Characters Or Emojis", "Bad Request");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
        }
        List<Post> postList = postService.findAllPostByKeyword(keyword.getKeyword().trim());
        if(postList==null){
            ApiResponse errorResponse = ApiResponse.error(404 , "No Post Found", "Not Found");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse);
        }else {
            ApiResponse<List<Post>> apiResponse = ApiResponse.success(postList);
            return ResponseEntity.ok(apiResponse);
        }
    }
}
