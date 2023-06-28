package com.example.demo.Controller;

import com.example.demo.Entity.Category;
import com.example.demo.Entity.CategorySubMap;
import com.example.demo.Entity.CategorySubMapId;
import com.example.demo.Entity.SubCategory;
import com.example.demo.Repository.PostRepository;
import com.example.demo.Service.CategoryService;
import com.example.demo.Service.PostService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
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

    @GetMapping("/categories")
    public List<Category> getAllCategory(){
        return categoryService.findAllCategories();
    }
    //------------------------------------------------------------------------------------------
    //show all posts under specific category
    @GetMapping("/categories/{categoryId}")
    public List<Map<String, Object>> findAllPostsByCategoryId(HttpServletRequest request,@PathVariable Integer categoryId){

        return postRepository.allPostsUnderOneCategory(categoryId);
//        return postService.findPostsByCategoryId(categoryId);
    }
    @GetMapping("/categories/getSubCategory")
    public Map<String,List<String>> getSubCategory(){
        Map<Integer,List<Integer>> getAllCategorySubMapIds = categoryService.getAllCategorySubMapIds();
        return categoryService.getSubCategory(getAllCategorySubMapIds);
    }
    @GetMapping("/categories/getAllCategorySubMapIds")
    public Map<Integer,List<Integer>> getAllCategorySubMapIds(){
        return categoryService.getAllCategorySubMapIds();
    }

}
