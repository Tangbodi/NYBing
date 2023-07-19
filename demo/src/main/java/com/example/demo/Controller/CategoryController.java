package com.example.demo.Controller;

import com.example.demo.Entity.SubCategory;
import com.example.demo.Service.CategoryService;
import com.example.demo.Service.PostCommentsViewService;
import com.example.demo.Service.SubCategoryService;
import com.example.demo.Util.ApiResponse;
import com.example.demo.Util.RedisCache;
import com.example.demo.Validator.ValidString;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import redis.clients.jedis.Jedis;
import com.fasterxml.jackson.core.type.TypeReference;

import java.io.IOException;
import java.util.*;

@RestController
//@CrossOrigin(origins = "http://192.168.1.10:3000/")
public class CategoryController {
    private static final Logger logger = LoggerFactory.getLogger(CategoryController.class);
    private static final ObjectMapper objectMapper = new ObjectMapper();
    private static final String CATEGORY_SUB_CATEGORY_CACHE_KEY = "CATEGORY_SUB_CATEGORY";
    @Autowired
    private CategoryService categoryService;
    @Autowired
    private ApiResponse apiResponse;
    @Autowired
    private SubCategoryService subCategoryService;
    @Autowired
    private PostCommentsViewService postCommentsViewService;

    //------------------------------------------------------------------------------------------
    @GetMapping("/category/sub_category")
    public ResponseEntity<ApiResponse<Map<Integer, Map<String, List<SubCategory>>>>> getAllSubCategories() throws JsonProcessingException {
        Jedis jedis = new Jedis("localhost");
        boolean existsInCache = jedis.exists(CATEGORY_SUB_CATEGORY_CACHE_KEY);
        if(existsInCache){
            logger.info("CATEGORY_SUB_CATEGORY_CACHE exists in Redis cache:::");
            logger.info("Get CATEGORY_SUB_CATEGORY from Redis cache:::");
            String json = jedis.get(CATEGORY_SUB_CATEGORY_CACHE_KEY);
            Map<Integer, Map<String, List<SubCategory>>> res = objectMapper.readValue(json, new TypeReference<Map<Integer, Map<String, List<SubCategory>>>>(){});
            ApiResponse<Map<Integer, Map<String, List<SubCategory>>>> apiResponse = ApiResponse.success(res);
            return ResponseEntity.ok(apiResponse);
        }else{
            logger.info("CATEGORY_SUB_CATEGORY_CACHE doesn't exist in Redis cache:::");
            logger.info("Get CATEGORY_SUB_CATEGORY from MySQL:::");
            Map<Integer, Map<String, List<SubCategory>>> res = categoryService.getAllSubCategories();
            if(res!=null){
                ApiResponse<Map<Integer, Map<String, List<SubCategory>>>> apiResponse = ApiResponse.success(res);
                return ResponseEntity.ok(apiResponse);
            }else{
                ApiResponse errorResponse = ApiResponse.error(500, "Internal Server Error", "Internal Server Error");
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
            }
        }
    }
    //------------------------------------------------------------------------------------------
    //show all posts under specific category
    @GetMapping("/categories/{sub_categoryId}")
    public ResponseEntity<ApiResponse<List<Map<String,Object>>>> PostsCommentsViewWithSubCategoryId(@PathVariable Integer sub_categoryId) throws IOException {
        if(!ValidString.SubCategoryIdEmpty(sub_categoryId) || !ValidString.SubCategoryIdLength(sub_categoryId)){
            ApiResponse errorResponse =ApiResponse.error(404, "No Such Sub Category", "Not Found");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
        }else if(subCategoryService.findSubCategoryById(sub_categoryId)==null){
            ApiResponse errorResponse =ApiResponse.error(404, "No Such Sub Category", "Not Found");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse);
        }else{
            List<Map<String,Object>> combineByTextRender = postCommentsViewService.combineByTextRender(sub_categoryId);
            ApiResponse<List<Map<String,Object>>> apiResponse = ApiResponse.success(combineByTextRender);
            return ResponseEntity.ok(apiResponse);
        }
    }

}
