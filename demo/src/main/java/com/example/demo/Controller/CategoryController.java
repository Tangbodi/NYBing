package com.example.demo.Controller;

import com.example.demo.DTO.PostsCommentsViewDTO;
import com.example.demo.Entity.PostsCommentsView;
import com.example.demo.Entity.SubCategory;
import com.example.demo.Repository.PostsRepository;
import com.example.demo.Service.CategoryService;
import com.example.demo.Service.PostCommentsViewService;
import com.example.demo.Service.PostService;
import com.example.demo.Service.SubCategoryService;
import com.example.demo.Util.ApiResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.*;

@RestController
@CrossOrigin(origins = "http://192.168.1.10:3000/")
public class CategoryController {
    private static final Logger logger = LoggerFactory.getLogger(CategoryController.class);

    @Autowired
    private CategoryService categoryService;

    @Autowired
    private ApiResponse apiResponse;
    @Autowired
    private SubCategoryService subCategoryService;
    @Autowired
    private PostCommentsViewService postCommentsViewService;

    //------------------------------------------------------------------------------------------
    //show all posts under specific category
    @GetMapping("/categories/{sub_categoryId}")
    public ResponseEntity<ApiResponse<List<Map<String,Object>>>> PostsCommentsViewWithSubCategoryId(@PathVariable Integer sub_categoryId) throws IOException {
        if(subCategoryService.findSubCategoryById(sub_categoryId)==null){
            ApiResponse errorResponse =ApiResponse.error(404, "No Such Sub Category", "Not Found");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse);
        }
        //get all posts with views under specific sub_category
//        List<PostsCommentsView> postsCommentsViewList = postCommentsViewService.findBySubCategoryId(sub_categoryId);
        List<Map<String,Object>> combineByTextRender = postCommentsViewService.combineByTextRender(sub_categoryId);
        ApiResponse<List<Map<String,Object>>> apiResponse = ApiResponse.success(combineByTextRender);
        return ResponseEntity.ok(apiResponse);
    }
    //------------------------------------------------------------------------------------------
    @GetMapping("/category/sub_category")
    public ResponseEntity<ApiResponse<Map<Integer, Map<String, List<SubCategory>>>>> getAllSubCategories() {
        try {
            Map<Integer, Map<String, List<SubCategory>>> res = categoryService.getAllSubCategory();
            ApiResponse<Map<Integer, Map<String, List<SubCategory>>>> apiResponse = ApiResponse.success(res);
            return ResponseEntity.ok(apiResponse);
        } catch (Exception e) {
            ApiResponse errorResponse = ApiResponse.error(500, "Internal Server Error", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
        }
    }
}
