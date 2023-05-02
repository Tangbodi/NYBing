package com.example.demo.Controller;

import com.example.demo.Repository.CategoryRepository;
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
@CrossOrigin(origins ="${ORIGINS}")
public class CategoryController {
    private static final Logger logger = LoggerFactory.getLogger(CategoryController.class);

    @Autowired
    private CategoryService categoryService;
    @Autowired
    private PostService postService;
    @Autowired
    private CategoryRepository categoryRepository;
    @Autowired
    private SessionManagementUtil sessionManagementUtil;
    @GetMapping("/categories")
    public List<Object[]> getTopFivePosts(HttpServletRequest request){
        if (!this.sessionManagementUtil.doesSessionExist(request))
        {
            logger.info("Please login to access this page");
        }
        return categoryService.getAllTopFivePostsUnderEveryCategory();
    }
    //------------------------------------------------------------------------------------------
    //show all posts under specific category
    @GetMapping("/categories/{categoryId}")
    public List<Map<String, Object>> findAllPostsByCategoryId(HttpServletRequest request,@PathVariable Integer categoryId){
        if (!this.sessionManagementUtil.doesSessionExist(request))
        {
            logger.info("Please login to access this page");
        }
        return categoryRepository.allPostsUnderOneCategory(categoryId);
//        return postService.findPostsByCategoryId(categoryId);
    }

}
