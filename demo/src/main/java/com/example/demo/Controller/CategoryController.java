package com.example.demo.Controller;

import com.example.demo.Entity.SubCategory;
import com.example.demo.Repository.PostRepository;
import com.example.demo.Service.CategoryService;
import com.example.demo.Service.PostService;
import com.example.demo.Util.ApiResponse;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.*;

@RestController
@CrossOrigin(origins = "http://192.168.1.10:3000/")
public class CategoryController {
    private static final Logger logger = LoggerFactory.getLogger(CategoryController.class);

    @Autowired
    private PostService postService;
    @Autowired
    private CategoryService categoryService;
    @Autowired
    private PostRepository postRepository;

    @Autowired
    private ApiResponse apiResponse;
    @GetMapping("/categories")
    public ResponseEntity<ApiResponse< List<Object[]>>> getAllCategory(){
        try {
            List<Object[]> res= postService.getAllTopFivePostsUnderEveryCategory();
            ApiResponse< List<Object[]>> list = apiResponse = ApiResponse.success(res);
            return ResponseEntity.ok(apiResponse);

        } catch (Exception e) {
            ApiResponse< List<Object[]>> errorResponse = ApiResponse.error(500, "Internal Server Error", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
        }
    }
    //  public List<Object[]> getTopFivePosts(HttpServletRequest request){
    //
    //        return postService.getAllTopFivePostsUnderEveryCategory();
    //    }
    //------------------------------------------------------------------------------------------
    //show all posts under specific category
    @GetMapping("/categories/{sub_categoryId}")
    public ResponseEntity<ApiResponse< List<Map<String, Object>>>> findAllPostsByCategoryId(HttpServletRequest request,@PathVariable Integer sub_categoryId){
        try {
            List<Map<String, Object>> list = postRepository.allPostsUnderOneCategory(sub_categoryId);
            ApiResponse<List<Map<String, Object>>> apiResponse = ApiResponse.success(list);
            return ResponseEntity.ok(apiResponse);
        } catch (Exception e) {
            ApiResponse<List<Map<String, Object>>> errorResponse =ApiResponse.error(500, "Internal Server Error", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
        }
    }
    @GetMapping("/categories/getSubCategory")
    public Map<String,List<String>>  getSubCategory(){
        Map<Integer,List<Integer>> getAllCategorySubMapIds = categoryService.getAllCategorySubMapIds();
        return categoryService.getSubCategory(getAllCategorySubMapIds);
    }
    @GetMapping("/categories/getAllCategorySubMapIds")
    public String getAllCategorySubMapIds(){
        Map<Integer,List<Integer>> map = categoryService.getAllCategorySubMapIds();
        Gson gson = new GsonBuilder().create();
        String json = gson.toJson(map);
        return json;
    }

    @GetMapping("/category/sub_category")
    public ResponseEntity<ApiResponse<Map<Integer, Map<String, List<SubCategory>>>>> getAllSubCategory() {
        try {
            Map<Integer, Map<String, List<SubCategory>>> res = categoryService.getAllSubCategory();
            ApiResponse<Map<Integer, Map<String, List<SubCategory>>>> apiResponse = ApiResponse.success(res);
            return ResponseEntity.ok(apiResponse);
        } catch (Exception e) {
            ApiResponse<Map<Integer, Map<String, List<SubCategory>>>> errorResponse = ApiResponse.error(500, "Internal Server Error", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
        }
    }
}
