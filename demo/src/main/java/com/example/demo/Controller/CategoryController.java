package com.example.demo.Controller;

import com.example.demo.Entity.Category;
import com.example.demo.Entity.CategorySubMap;
import com.example.demo.Entity.CategorySubMapId;
import com.example.demo.Entity.SubCategory;
import com.example.demo.Repository.PostRepository;
import com.example.demo.Service.CategoryService;
import com.example.demo.Service.PostService;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
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
    public String getAllCategory(){
        Gson gson = new GsonBuilder().create();
        List<Category> categoryList = categoryService.findAllCategories();
        String json = gson.toJson(categoryList);
        return json;
    }
    //  public List<Object[]> getTopFivePosts(HttpServletRequest request){
    //
    //        return postService.getAllTopFivePostsUnderEveryCategory();
    //    }
    //------------------------------------------------------------------------------------------
    //show all posts under specific category
    @GetMapping("/categories/{categoryId}")
    public String findAllPostsByCategoryId(HttpServletRequest request,@PathVariable Integer categoryId){
        Gson gson = new GsonBuilder().create();
        List<Map<String, Object>> list = postRepository.allPostsUnderOneCategory(categoryId);
        String json = gson.toJson(list);
        return json;
//        return postService.findPostsByCategoryId(categoryId);
    }
    @GetMapping("/categories/getSubCategory")
    public Map<String,List<String>>  getSubCategory(){
        Map<Integer,List<Integer>> getAllCategorySubMapIds = categoryService.getAllCategorySubMapIds();
        return categoryService.getSubCategory(getAllCategorySubMapIds);
    }
    @GetMapping("/categories/getAllCategorySubMapIds")
    public Map<Integer,List<Integer>> getAllCategorySubMapIds(){
        return categoryService.getAllCategorySubMapIds();
    }

    @GetMapping("/category/sub_category")
    public String getAllSubCategory(){
        Map<Integer,Map<String, List<SubCategory>>> res = categoryService.getAllSubCategory();
        Gson gson = new GsonBuilder().create();
        String json = gson.toJson(res);
        return json;
    }
}
