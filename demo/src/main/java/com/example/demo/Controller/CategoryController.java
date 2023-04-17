package com.example.demo.Controller;

import com.example.demo.DAO.TopFiveArticleWithCategoryDAO;
import com.example.demo.Repository.CategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
@CrossOrigin(origins = "http://192.168.1.23:3000/")
public class CategoryController {
    private final CategoryRepository categoryRepository;

    @Autowired
    private TopFiveArticleWithCategoryDAO topFiveArticleWithCategoryDAO;
    public CategoryController(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    @GetMapping("/categories")
    public List<Map<String, Object>> getAllCategory(){
//        TopFiveArticleWithCategoryDTO topFiveArticleWithCategoryDTO = new TopFiveArticleWithCategoryDTO();
//        List<Map<String,Object>> list = categoryRepository.categoryTopFivePost();
//        for(int i=0;i<list.size();i++){
//            Map<String,Object> map = new HashMap<>();
//            map.put(list.get(i).)
//        }
//        return topFiveArticleWithCategoryDAO.listTopFiveArticleWithCategory();
        return categoryRepository.categoryTopFivePost();
    }
}
