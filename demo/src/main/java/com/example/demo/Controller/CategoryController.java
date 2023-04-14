package com.example.demo.Controller;

import com.example.demo.DAO.TopFiveArticleWithCategoryDAO;
import com.example.demo.DTO.TopFiveArticleWithCategoryDTO;
import com.example.demo.Entity.Category;
import com.example.demo.Repository.CategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

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
    public List<Category> getAllCategory(){

//        return topFiveArticleWithCategoryDAO.listTopFiveArticleWithCategory();
        return categoryRepository.categoryTopFivePost();
    }
}
