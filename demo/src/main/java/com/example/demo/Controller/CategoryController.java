package com.example.demo.Controller;

import com.example.demo.Entity.Category;
import com.example.demo.Repository.CategoryRepository;
import com.example.demo.Repository.PostRepository;
import com.example.demo.Service.CategoryService;
import com.example.demo.Service.PostService;
import com.example.demo.Util.SessionManagementUtil;
import lombok.Value;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;

@RestController
@CrossOrigin(origins ="http://192.168.1.13:3000/")
public class CategoryController {
    private static final Logger logger = LoggerFactory.getLogger(CategoryController.class);

    @Autowired
    private PostService postService;
    @Autowired
    private CategoryService categoryService;
    @Autowired
    private PostRepository postRepository;
    @GetMapping("/categories")
    public List<Object[]> getTopFivePosts(HttpServletRequest request){

        return postService.getAllTopFivePostsUnderEveryCategory();
    }
    //------------------------------------------------------------------------------------------
    //show all posts under specific category
    @GetMapping("/categories/{categoryId}")
    public List<Map<String, Object>> findAllPostsByCategoryId(HttpServletRequest request,@PathVariable Integer categoryId){

        return postRepository.allPostsUnderOneCategory(categoryId);
//        return postService.findPostsByCategoryId(categoryId);
    }
    @GetMapping("/categories/collection")
    public List<Category> getCategoryCollection(){
        return categoryService.findAllCategories();
    }
}
