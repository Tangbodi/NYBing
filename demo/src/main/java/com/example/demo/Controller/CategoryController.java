package com.example.demo.Controller;

import com.example.demo.DAO.PostDAO;
import com.example.demo.Entity.Post;
import com.example.demo.Repository.CategoryRepository;
import com.example.demo.Service.CategoryService;
import com.example.demo.Service.PostService;
import com.example.demo.Util.HttpUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.net.http.HttpRequest;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@CrossOrigin(origins = "http://192.168.1.23:3000/")
public class CategoryController {
    private static final Logger logger = LoggerFactory.getLogger(CategoryController.class);

    @Autowired
    private CategoryService categoryService;
    @Autowired
    private PostService postService;
    @GetMapping("/categories")
    public List<Object[]> getTopFivePosts(HttpServletRequest request){

        return categoryService.getAllTopFivePostsUnderEveryCategory();
    }
    //------------------------------------------------------------------------------------------
    //show all posts under specific category
    @GetMapping("/categories/{categoryId}")
    public List<Map<String, Object>> findAllPostsByCategoryId(@PathVariable Integer categoryId){
        return postService.findPostsByCategoryId(categoryId);
    }

}
