package com.example.demo.Controller;

import com.example.demo.DAO.PostDAO;
import com.example.demo.Repository.CategoryRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@CrossOrigin(origins = "http://192.168.1.23:3000/")
public class CategoryController {
    private static final Logger logger = LoggerFactory.getLogger(CategoryController.class);

    @Autowired
    private PostDAO postDAO;

    @GetMapping("/categories")
    public List<Object[]> getTopFivePosts(){
        return postDAO.getTopFivePosts();
    }

}
